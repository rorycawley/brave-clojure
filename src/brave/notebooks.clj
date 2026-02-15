(ns brave.notebooks
  "Utilities for discovering chapter notebooks and building Clerk export config."
  (:require
    [clojure.java.io :as io]))


(def chapter-file-pattern
  #"^ch(\d{2})_.+\.clj$")


(def toc-file-name
  "index.clj")


(defn chapter-file-name?
  [filename]
  (boolean (re-matches chapter-file-pattern filename)))


(defn select-chapter-paths
  "Filter a collection of paths down to chapter notebooks and sort by filename."
  [paths]
  (->> paths
       (map str)
       (filter #(chapter-file-name? (.getName (io/file %))))
       sort
       vec))


(defn chapter-paths
  "Return sorted chapter notebook file paths under `notebook-dir`."
  ([]
   (chapter-paths "notebooks"))
  ([notebook-dir]
   (->> (file-seq (io/file notebook-dir))
        (filter #(.isFile %))
        (map #(.getPath %))
        select-chapter-paths)))


(defn toc-path
  "Return the notebook path for the table-of-contents notebook."
  ([]
   (toc-path "notebooks"))
  ([notebook-dir]
   (str (io/file notebook-dir toc-file-name))))


(defn export-config
  "Return Clerk build config for table-of-contents + chapter notebooks.
   Throws when no chapter notebooks are present."
  ([]
   (export-config "notebooks" "public"))
  ([notebook-dir out-path]
   (let [chapter-notebooks (chapter-paths notebook-dir)
         index-notebook (toc-path notebook-dir)]
     (when (empty? chapter-notebooks)
       (throw
         (ex-info "No chapter notebooks found. Expected files like ch01_*.clj"
                  {:notebook-dir notebook-dir
                   :pattern chapter-file-pattern})))
     {:paths (into [index-notebook] chapter-notebooks)
      :index index-notebook
      :out-path out-path})))
