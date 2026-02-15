(ns export-clerk
  (:require
    [brave.notebooks :as notebooks]
    [nextjournal.clerk :as clerk]))


(defn -main
  [& _]
  (println "Exporting notebooks (index + chapters)...")
  (let [{:keys [paths index out-path] :as config} (notebooks/export-config)]
    (println "Notebook count:" (count paths))
    (println "Index notebook:" index)
    (println "Output path:" out-path)
    (clerk/build! config)))
