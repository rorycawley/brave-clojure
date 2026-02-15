(ns brave.smoke-test
  (:require
    [brave.scratch :as scratch]
    [clojure.test :refer [deftest is testing]]))


(deftest smoke
  (testing "basic sanity check"
    (is (= 2 (inc 1)))))


(deftest hello-test
  (testing "hello returns greeting message"
    (is (= "Hello, Brave Clojure!" (scratch/hello)))))
