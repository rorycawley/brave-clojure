(ns user
  (:require
    [clojure.tools.namespace.repl :as nsr]))


(defn refresh
  []
  (nsr/refresh))
