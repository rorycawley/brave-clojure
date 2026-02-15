(ns ch09-concurrent-parallel-programming
  (:require
    [nextjournal.clerk :as clerk]))


(clerk/md "# Chapter 9 - Concurrent and Parallel Programming")

(clerk/md "Notes and exercises for Chapter 9 of *Clojure for the Brave and True*.")

(clerk/md "## Summary")


(clerk/md "- Concurrency vs parallelism
- Coordination primitives
- Throughput and safety")


(clerk/md "## Scratch")
(future (+ 40 2))
