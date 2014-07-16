(ns podload.downloader
  (:require [feedparser-clj.core :as feedparser]
            [me.raynes.moments :as m]
            [flatland.chronicle :as c]
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
   copies infile to outfile, and adds the album tag to it"
  [infile outfile album-name]
  (let [f (Mp3File.  infile)
        ;; XXX BLEAH! JAVA!!
        t (if (.hasId3v2Tag f)
            (.getId3v2Tag f)
            (let [t1 (ID3v24Tag.)]
              (.setId3v2Tag f t1)
              t1))]
    (.setAlbum t album-name)
    (.save f outfile)))


(defn do-everything!
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
      (finally
        (clojure.java.io/delete-file tempfile true)))))


(defn schedule-one!
  [{:keys [name frequency] :as config}]
  (log/info "Scheduling " name)
  (m/schedule executor frequency #(do-everything! config)))

(defn schedule-all!
  [configs]
  (doseq [c configs]
    (schedule-one! c)))







