(ns clerk
  (:require [nextjournal.clerk :as clerk]))

(defn -main [& _]
  (clerk/serve! {:watch-paths ["notebooks"]
                 :browse? true}))
