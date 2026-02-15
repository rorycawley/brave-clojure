(ns index
  (:require
    [brave.notebooks :as notebooks]
    [clojure.string :as str]
    [nextjournal.clerk :as clerk]))


(defn- chapter-entry
  [path]
  (let [filename (last (str/split path #"/"))
        [_ chapter slug] (re-matches #"^ch(\d{2})_(.+)\.clj$" filename)
        title (->> (str/split slug #"_")
                   (map str/capitalize)
                   (str/join " "))
        href (str/replace path #"^notebooks/" "")]
    {:chapter chapter
     :title title
     :file filename
     :href href}))


(def chapter-entries
  (mapv chapter-entry (notebooks/chapter-paths)))


(clerk/md "# Clojure for the Brave and True")
(clerk/md "## Table of Contents")
(clerk/md "This index links to one notebook per chapter.")


(clerk/table
  (mapv #(select-keys % [:chapter :title :file]) chapter-entries))


^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
  [:ol
   (for [{:keys [chapter title href]} chapter-entries]
     [:li {:key href}
      [:a {:href href}
       (str "Chapter " chapter ": " title)]])])
