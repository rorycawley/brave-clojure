(ns export-clerk
  (:require [babashka.fs :as fs]
            [nextjournal.clerk :as clerk]))

(defn- notebook-files []
  (->> (fs/glob "notebooks" "*.clj")
       (map str)
       sort))

(defn -main [& _]
  (let [paths (notebook-files)]
    (println "Exporting notebooks:" paths)
    (clerk/build! {:paths paths
                   :out-path "public"})))
