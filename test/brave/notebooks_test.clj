(ns brave.notebooks-test
  (:require
    [brave.notebooks :as notebooks]
    [clojure.test :refer [deftest is testing]]))


(deftest select-chapter-paths-test
  (testing "it keeps only chapter notebook files and sorts by chapter number"
    (is (= ["notebooks/ch01_building_running_and_repl.clj"
            "notebooks/ch02_how_to_use_emacs.clj"
            "notebooks/ch10_clojure_metaphysics.clj"]
           (notebooks/select-chapter-paths
             ["notebooks/index.clj"
              "notebooks/ch10_clojure_metaphysics.clj"
              "notebooks/ch02_how_to_use_emacs.clj"
              "notebooks/ch01_building_running_and_repl.clj"
              "notebooks/ch13_appendix.md"])))))


(deftest export-config-test
  (testing "it builds Clerk export options from chapter notebooks"
    (is (= {:paths ["notebooks/index.clj"
                    "notebooks/ch01_building_running_and_repl.clj"
                    "notebooks/ch13_multimethods_protocols_and_records.clj"]
            :index "notebooks/index.clj"
            :out-path "public"}
           (with-redefs [notebooks/chapter-paths (constantly ["notebooks/ch01_building_running_and_repl.clj"
                                                              "notebooks/ch13_multimethods_protocols_and_records.clj"])
                         notebooks/toc-path (constantly "notebooks/index.clj")]
             (notebooks/export-config "notebooks" "public")))))

  (testing "it errors when no chapter notebooks are found"
    (is (thrown-with-msg?
          clojure.lang.ExceptionInfo
          #"No chapter notebooks found"
          (with-redefs [notebooks/chapter-paths (constantly [])]
            (notebooks/export-config "notebooks" "public"))))))
