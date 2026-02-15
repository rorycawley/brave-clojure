(ns ch04-core-functions-in-depth
  (:require
    [nextjournal.clerk :as clerk]))


(clerk/md "# Chapter 4 - Core Functions in Depth")

(clerk/md "Notes and exercises for Chapter 4 of *Clojure for the Brave and True*.")

(clerk/md "## Summary")


(clerk/md "- Sequence utilities
- Collection operations
- Function composition")


(clerk/md "## Scratch")


(->> [1 2 3 4 5]
     (filter odd?)
     (map #(* % %)))
