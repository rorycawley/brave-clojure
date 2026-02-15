(ns export-clerk
  (:require
    [nextjournal.clerk :as clerk]))


(defn -main
  [& _]
  (println "Exporting Clerk notebooks...")
  (clerk/build!
    {:paths ["notebooks/dashboard.clj"
             "notebooks/ch00_template.clj"
             "notebooks/ch01_clerk_examples.clj"]
     :index "notebooks/dashboard.clj"
     :out-path "public"}))
