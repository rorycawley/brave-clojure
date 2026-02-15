(ns ch10-clojure-metaphysics
  (:require
    [nextjournal.clerk :as clerk]))


(clerk/md "# Chapter 10 - Clojure Metaphysics: Atoms, Refs, Vars, and Cuddle Zombies")

(clerk/md "Notes and exercises for Chapter 10 of *Clojure for the Brave and True*.")

(clerk/md "## Summary")


(clerk/md "- Shared state primitives
- STM and coordinated updates
- Identity vs value")


(clerk/md "## Scratch")


(let [counter (atom 0)]
  (swap! counter inc)
  @counter)
