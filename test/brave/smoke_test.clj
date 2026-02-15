(ns brave.smoke-test
  (:require
    [clojure.test :refer [deftest is]]))


(deftest smoke
  (is (= 2 (+ 1 1))))
