(ns ch08-writing-macros
  (:require
    [nextjournal.clerk :as clerk]))


(clerk/md "# Chapter 8 - Writing Macros")

(clerk/md "Notes and exercises for Chapter 8 of *Clojure for the Brave and True*.")

(clerk/md "## Summary")


(clerk/md "- Syntax-quote and unquote
- Hygienic expansion patterns
- When to avoid macros")


(clerk/md "## Scratch")
(clerk/code "(defmacro unless [pred a b]\n  `(if (not ~pred) ~a ~b))")
