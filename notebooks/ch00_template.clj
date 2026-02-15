(ns ch00-template
  (:require [nextjournal.clerk :as clerk]))

(clerk/md "# Chapter 00 — Template")

(clerk/md "## Notes")
(clerk/md "- What did I learn?\n- What surprised me?\n- What should go into src/?")

(clerk/md "## Examples")
(+ 1 2 3)

(clerk/md "## Next steps")
(clerk/md "- …")
