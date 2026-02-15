(ns brave.smoke-test
  (:require
    [clojure.test :refer [deftest is testing]]))


(deftest smoke
  (testing "basic sanity check"
    (is (= 2 (inc 1)))))
