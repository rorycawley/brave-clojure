(ns ch03-do-things-crash-course
  (:require
    [nextjournal.clerk :as clerk]))


(clerk/md "# Chapter 3 - Do Things: A Clojure Crash Course")

(clerk/md "Notes and exercises for Chapter 3 of *Clojure for the Brave and True*.")

(clerk/md "## Summary")


(clerk/md "- Data literals and evaluation
- Control flow basics
- First small functions")


(clerk/md "## Scratch")
(map inc [1 2 3])
(str "It was the panda " "in the library " "with a dust buster")

(if true
  "By Zeus's hammer!"
  "By Aquaman's trident!")

(if false
  "By Zeus's hammer!"
  "By Aquaman's trident!")

(if false
  "By Odin's Elbow!")

(if true
  (do (println "Success!")
      "By Zeus's hammer!")
  (do (println "Failure!")
      "By Aquaman's trident!"))


(when true
  (println "Success!")
  "abra cadabra")

(if "bears eat beets"
  "bears beets Battlestar Galactica")

(= 1 1)

(= nil nil)

(= 1 2)

(or false nil :large_I_mean_venti :why_cant_I_just_say_large)
(or (= 0 1) (= "yes" "no"))
(or nil)
(and :free_wifi :hot_coffee)
(and :feelin_super_cool nil false)
(def failed-protagonist-names
  ["Larry Potter" "Doreen the Explorer" "The Incredible Bulk"])
failed-protagonist-names

(defn error-message
  [severity]
  (str "OH GOD! IT'S A DISASTER! WE'RE "
       (if (= severity :mild)
         "MILDLY INCONVENIENCED!"
         "DOOOOOOOMED!")))

(error-message :mild)
; => "OH GOD! IT'S A DISASTER! WE'RE MILDLY INCONVENIENCED!"

93
1.2
1/5
"Lord Voldemort"
"\"He who must not be named\""
"\"Great cow of Moscow!\" - Hermes Conrad"

(def name "Chewbacca")
(str "\"Uggllglglglglglglglll\" - " name)
{:first-name "Charlie"
 :last-name "McFishwich"}
{"string-key" +}
{:name {:first "John" :middle "Jacob" :last "Jingleheimerschmidt"}}
(hash-map :a 1 :b 2)
(get {:a 0 :b 1} :b)
(get {:a 0 :b {:c "ho hum"}} :b)
(get {:a 0 :b 1} :c)
(get {:a 0 :b 1} :c "unicorns?")
(get-in {:a 0 :b {:c "ho hum"}} [:b :c])
({:name "The Human Coffeepot"} :name)
(:a {:a 1 :b 2 :c 3})
(get {:a 1 :b 2 :c 3} :a)
(:d {:a 1 :b 2 :c 3} "No gnome knows homes like Noah knows")
[3 2 1]
(get [3 2 1] 0)
(get ["a" {:name "Pugsley Winterbottom"} "c"] 1)
(vector "creepy" "full" "moon")
(conj [1 2 3] 4)
'(1 2 3 4)


(nth '(:a :b :c) 0)
(nth '(:a :b :c) 2)
(conj '(1 2 3) 4)

#{20 :icicle "kurt vonnegut"}
(hash-set 1 1 2 2)
(conj #{:a :b} :b)
(set [3 3 3 4 4])

(contains? #{:a :b} :a)
(contains? #{:a :b} 3)
(contains? #{nil} nil)

(:a #{:a :b})
(:x #{:a :b})
(get #{:a nil} nil)
(get #{:a :b} "kurt vonnegut")

(+ 1 2 3 4)
(first [1 2 3 4])


