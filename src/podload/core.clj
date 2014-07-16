(ns podload.core
  (:require [clojure.tools.cli :as cli]
            [clojure.edn :as edn]
            [clojure.tools.trace :as trace]
            [taoensso.timbre :as log]
            [environ.core :as env]
            [podload.downloader :as dl])
  (:gen-class))



;; IMPORTANT: This bare exec is here to dothis FIRST before running anything, at compile time
(log/merge-config! {:appenders {:spit {:enabled? true
                                       :fmt-output-opts {:nofonts? true}}
                                :standard-out {:enabled? true
                                               ;; nrepl/cider/emacs hates the bash escapes.
                                               :fmt-output-opts {:nofonts? true}}}
                    ;; TODO: should only be in dev profile/mode
                    :shared-appender-config {:spit-filename "podload.log"}})


;; IMPORTANT: enables the very very awesome use of clojure.tools.trace/trace-vars , etc
;; and logs the output of those traces to whatever is configured for timbre at the moment!
(alter-var-root #'clojure.tools.trace/tracer (fn [_]
                                               (fn [name value]
                                                 (log/debug name value))))



(defn process-config
  [config-path]
  (->> config-path
       slurp
       edn/read-string
       dl/schedule-all!))


(defn -main [config-path & args]
  (log/info "Loading config file " config-path)
  (process-config config-path))




(comment
  ;; this should work but it does not
  (let [[opts args banner] (cli/cli args
                                    ["-h"
                                     "--help" "pass config filename"
                                     :default false
                                     :flag true])]
    (when (:help opts)
      (println banner))))


(comment
  ;; testing

  (process-config "test-config.edn")
  
  )