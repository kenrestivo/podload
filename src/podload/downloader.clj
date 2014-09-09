(ns podload.downloader
  (:require [feedparser-clj.core :as feedparser]
            [me.raynes.moments :as m]
            [flatland.chronicle :as c]
            [clj-time.local :as l]
            [utilza.misc :as umisc]
            [taoensso.timbre :as log]
            [clj-http.client :as client]
            [clj-time.core :as t]
            [utilza.repl :as urepl])
  (:import  [com.mpatric.mp3agic ID3v2 ID3v24Tag Mp3File]))




(def executor (m/executor 10))


(defn get-latest-url
  "Fetches a podcast supplied as an url, and Returns url of latest enclosure in it."
  [podcast-url]
  (->>  podcast-url
        feedparser/parse-feed
        :entries
        (mapcat :enclosures)
        (map :url)
        first))


(defn write-file!
  "Takes input stream, and output filename, and does the deed."
  [in out]
  (with-open [w (clojure.java.io/output-stream out)]
    (.write w in)))



(defn download-latest!
  "Takes podcast url, grabs latest enclosure, and dumps it to outfile path"
  [podcast-url outfile]
  (-> podcast-url
      get-latest-url
      (client/get  {:as :byte-array})
      :body
      (write-file! outfile)))



(defn tag-and-copy!
  "Takes an infile, an outfile, and an album name,
   brutally removes any tags (because mp3agic was puking on NotSupportedExceptions),
   copies infile to outfile, and adds the album tag to it"
  [infile outfile album-name]
  (let [f (Mp3File.  infile)
        t (ID3v24Tag.)]
    (when (.hasId3v1Tag f)
      (.removeId3v1Tag f))
    (when (.hasId3v2Tag f)
      (.removeId3v2Tag f))
    (when (.hasCustomTag f)
      (.removeCustomTag f))
    (.setId3v2Tag f t)
    (.setAlbum t album-name)
    (.save f outfile)))


(defn do-everything!
  "Takes a config, downloads the rss feed, finds the most recent file,
   downloads it, tags it, and moves it where it needs to go."
  [{:keys [name feed-url filename tag dest-dir] :as config}]
  (log/info "Beginning " name)
  (let [tempfile  (str "/tmp/" (java.util.UUID/randomUUID))
        destfile (str dest-dir "/" filename)]
    ;; clean up old one first
    (clojure.java.io/delete-file destfile true) 
    (try
      (download-latest! feed-url tempfile)
      (tag-and-copy! tempfile destfile tag)
      (log/info "Completed downloading " name)
      (catch  Exception e
        (log/error e name))
      (finally
        (clojure.java.io/delete-file tempfile true)))))


(defn schedule-local
  "Hack until moments supports local time.
   Schedule a task to run based on a Chronicle specification."
  [executor spec f]
  (let [[start & rest] (c/times-for spec (l/local-now))]
    (m/schedule-at executor start (#'me.raynes.moments/chronicle-scheduler executor f rest))))

(defn format-schedule
  [frequency]
  (->> (l/local-now)
       (c/times-for frequency)
       (map #(l/format-local-time % :mysql))
       (take 5)
       (umisc/inter-str "\n")))

(defn schedule-one!
  [{:keys [name frequency] :as config}]
  (log/info "Scheduling " name " at:\n"  (format-schedule frequency) " etc etc...")
  (schedule-local executor frequency #(do-everything! config)))

(defn schedule-all!
  [configs]
  (doseq [c configs]
    (schedule-one! c)))

(defn seed!
  "Start off with forcing all the configs to run."
  [configs]
  (doseq [c configs]
    (do-everything! c)))





