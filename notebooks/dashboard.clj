(ns dashboard
  (:require
    [babashka.fs :as fs]
    [clojure.string :as str]
    [nextjournal.clerk :as clerk]))


(defn- chapter-files
  []
  (->> (fs/glob "notebooks" "*.clj")
       (map str)
       (remove #(str/ends-with? % "dashboard.clj"))
       sort))


(defn- link
  [path]
  ;; Clerk renders markdown links nicely
  (clerk/md (format "- [%s](%s)"
                    (fs/file-name path)
                    (str/replace path #"^notebooks/" ""))))


(clerk/md "# Dashboard")

(clerk/md "This is the landing page for chapter notebooks.")

(clerk/md "## Chapters")


(into [:div]
      (map link (chapter-files)))
