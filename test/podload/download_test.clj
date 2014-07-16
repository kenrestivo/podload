(ns podload.download-test
  (:require [clojure.test :refer :all]
            [podload.download :refer :all]))

(comment
  (download-latest! "http://www.islaearth.org/radio/feed.rss" "/mnt/sdcard/tmp/isla.mp3")
  (download-latest! "http://feeds.feedburner.com/davidpakmanshow" "/mnt/sdcard/tmp/pakman.mp3")
  (download-latest! "http://feeds.feedburner.com/radiocurious-podcast" "/mnt/sdcard/tmp/curious.mp3")
  )


(comment
  (tag-and-copy! "/mnt/sdcard/tmp/pakman.mp3" "/mnt/sdcard/tmp/pakmantag.mp3" "DavidPakman")
  (tag-and-copy! "/mnt/sdcard/tmp/isla.mp3" "/mnt/sdcard/tmp/islatag.mp3" "IslaEarth")
  (tag-and-copy! "/mnt/sdcard/tmp/curious.mp3" "/mnt/sdcard/tmp/curioustag.mp3" "RadioCurious")
  )

(comment
  

  (range 2 7)


  
  (do-everything! test)
  
  (schedule-one! test)

  ;; TODO: pull from config, and you're there

  
  (urepl/massive-spew "/tmp/foo.edn" *1)

  ;; beautiful, alan, thanks, great for testing!
  (take 10 (c/times-for some-spec-map
                        (t/now)))


  (seq (.getQueue executor))
  
  )