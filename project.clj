(defproject podload "0.1.0"
  :description "Podcast downloader for Airtime"
  :url "http://restivo.org"
  :main podload.core
  :plugins [[lein-bin "0.3.4"]
            [lein-environ "0.5.0"]]
  :bin {:name "podload" }
  :profiles {:uberjar {:aot :all}}
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojars.scsibug/feedparser-clj "0.4.0"]
                 [me.raynes/moments "0.1.1"]
                 [org.clojure/tools.trace "0.7.6"]
                 [clj-http "0.9.2"]
                 [com.mpatric/mp3agic "0.8.2"]
                 [utilza "0.1.56"]
                 [com.taoensso/timbre "3.2.1"]
                 [environ "0.5.0"]
                 [me.raynes/conch "0.8.0"]
                 [org.clojure/tools.cli "0.2.4"]])


