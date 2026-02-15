(ns ch01-clerk-examples
  "A cookbook of Clerk visualization examples.
   Open this file in Cursive, start a REPL, and evaluate forms
   inside the (comment ...) blocks one at a time to explore."
  (:require
    [nextjournal.clerk :as clerk]))


;; # Chapter 01 — Clerk Visualization Examples
;;
;; This notebook is a **cookbook** of Clerk viewers. Each section has:
;;
;; - A **live example** that renders when Clerk loads the file
;; - A `(comment ...)` block with more examples to **eval from your REPL**
;;
;; **How to use:** place your cursor on a form inside a `comment` block
;; and evaluate it with Cursive (Ctrl+Enter / Cmd+Enter).

;; ---
;; ## 1. Markdown

(clerk/md
  "### Hello from Clerk!
This is **bold**, this is *italic*, and this is `inline code`.")

(comment
  ;; Headers
  (clerk/md "# Heading 1\n## Heading 2\n### Heading 3")

  ;; Bullet list
  (clerk/md
    "- Apples
- Bananas
- Cherries")

  ;; Numbered list
  (clerk/md
    "1. First item
2. Second item
3. Third item")

  ;; Links and images
  (clerk/md "[Clerk docs](https://book.clerk.vision)")

  ;; Code block inside markdown
  (clerk/md
    "```clojure
(defn greet [name]
  (str \"Hello, \" name \"!\"))
```")

  ;; Multi-paragraph prose
  (clerk/md
    "### Why Clerk?

Clerk turns **ordinary Clojure files** into rich, interactive documents.
You write code in your editor, evaluate with your REPL, and Clerk
renders everything in the browser.

> This is a blockquote — useful for call-outs.")

  ,)


;; ---
;; ## 2. Code Display

(clerk/code
  "(defn factorial [n]
  (reduce * 1 (range 1 (inc n))))")

(comment
  ;; Show syntax-highlighted code without evaluating it
  (clerk/code "(+ 1 2 3)")

  (clerk/code
    "(defn fibonacci [n]
  (loop [a 0 b 1 i 0]
    (if (= i n) a
      (recur b (+ a b) (inc i)))))")

  (clerk/code
    ";; A threading macro example
(-> {:name \"Alice\" :age 30}
    (assoc :role :developer)
    (update :age inc))")

  ,)


;; ---
;; ## 3. Tables

(clerk/table
  [{:language "Clojure" :creator "Rich Hickey" :year 2007}
   {:language "Elixir" :creator "José Valim" :year 2011}
   {:language "Rust" :creator "Graydon Hoare" :year 2010}])

(comment
  ;; Seq of maps (most common)
  (clerk/table
    [{:name "Alice" :age 30 :city "Dublin"}
     {:name "Bob" :age 25 :city "Cork"}
     {:name "Carol" :age 35 :city "Galway"}])

  ;; Map of seqs (column-oriented)
  (clerk/table
    {:name ["Alice" "Bob" "Carol"]
     :score [95 87 92]
     :grade ["A" "B+" "A-"]})

  ;; Seq of seqs with explicit headers
  (clerk/table
    (clerk/use-headers
      [["Planet" "Diameter (km)" "Moons"]
       ["Earth" 12742 1]
       ["Mars" 6779 2]
       ["Jupiter" 139820 95]]))

  ;; Larger generated table
  (clerk/table
    (for [i (range 1 11)]
      {:n i :squared (* i i) :cubed (* i i i)}))

  ,)


;; ---
;; ## 4. HTML & Hiccup

(clerk/html
  [:div {:style {:padding "1rem"
                 :background "#f0f4ff"
                 :border-radius "8px"
                 :border "1px solid #c0d0ff"}}
   [:h3 "Hello from Hiccup!"]
   [:p "Clerk can render arbitrary " [:strong "HTML"] " via Hiccup vectors."]])

(comment
  ;; Styled text
  (clerk/html
    [:p {:style {:color "darkgreen" :font-size "1.2em"}}
     "This text is styled with inline CSS."])

  ;; An HTML list
  (clerk/html
    [:ul
     [:li "First item"]
     [:li "Second item with " [:em "emphasis"]]
     [:li [:strong "Bold"] " third item"]])

  ;; A simple inline SVG — a colored circle
  (clerk/html
    [:svg {:width 100 :height 100}
     [:circle {:cx 50 :cy 50 :r 40
               :fill "coral" :stroke "darkred" :stroke-width 2}]])

  ;; Multiple SVG shapes
  (clerk/html
    [:svg {:width 200 :height 100}
     [:rect {:x 10 :y 10 :width 80 :height 80 :fill "steelblue" :rx 8}]
     [:circle {:cx 150 :cy 50 :r 40 :fill "gold"}]])

  ;; A mini progress bar
  (let [pct 72]
    (clerk/html
      [:div {:style {:background "#eee" :border-radius "4px"
                     :height "24px" :width "300px"}}
       [:div {:style {:background "mediumseagreen" :border-radius "4px"
                      :height "100%" :width (str pct "%")
                      :display "flex" :align-items "center"
                      :justify-content "center" :color "white"
                      :font-size "0.8em" :font-weight "bold"}}
        (str pct "%")]]))

  ,)


;; ---
;; ## 5. Plotly Charts

(clerk/plotly
  {:data [{:x ["Clojure" "Python" "Rust" "Go"]
           :y [95 88 92 85]
           :type "bar"
           :marker {:color ["#4a90d9" "#e6a817" "#dea584" "#69d2e7"]}}]
   :layout {:title "Language Satisfaction"
            :yaxis {:title "Score"}}})

(comment
  ;; Line chart
  (let [xs (range 0 10 0.5)
        ys (map #(Math/sin %) xs)]
    (clerk/plotly
      {:data [{:x xs :y ys
               :type "scatter" :mode "lines"
               :name "sin(x)"
               :line {:color "coral" :width 3}}]
       :layout {:title "Sine Wave"
                :xaxis {:title "x"}
                :yaxis {:title "sin(x)"}}}))

  ;; Scatter plot
  (let [n 50
        xs (repeatedly n #(rand 100))
        ys (map #(+ (* 0.8 %) (- (rand 20) 10)) xs)]
    (clerk/plotly
      {:data [{:x xs :y ys
               :type "scatter" :mode "markers"
               :marker {:size 8 :color "steelblue" :opacity 0.7}}]
       :layout {:title "Scatter Plot"
                :xaxis {:title "X"} :yaxis {:title "Y"}}}))

  ;; Multi-trace line chart
  (let [xs (range 1 11)]
    (clerk/plotly
      {:data [{:x xs :y (map #(* % %) xs)
               :type "scatter" :mode "lines+markers"
               :name "x²"}
              {:x xs :y (map #(* 2 %) xs)
               :type "scatter" :mode "lines+markers"
               :name "2x"}]
       :layout {:title "Comparing x² and 2x"}}))

  ;; Horizontal bar chart
  (clerk/plotly
    {:data [{:y ["Tests" "Linting" "Formatting" "Build"]
             :x [12 3 1 45]
             :type "bar"
             :orientation "h"
             :marker {:color "mediumpurple"}}]
     :layout {:title "CI Step Duration (seconds)"
              :xaxis {:title "Seconds"}}})

  ,)


;; ---
;; ## 6. Vega-Lite

(clerk/vl
  {:$schema "https://vega.github.io/schema/vega-lite/v5.json"
   :data {:values [{:category "A" :amount 28}
                   {:category "B" :amount 55}
                   {:category "C" :amount 43}
                   {:category "D" :amount 91}]}
   :mark "bar"
   :encoding {:x {:field "category" :type "nominal" :title "Category"}
              :y {:field "amount" :type "quantitative" :title "Amount"}
              :color {:field "category" :type "nominal"}}})

(comment
  ;; Scatter plot with Vega-Lite
  (clerk/vl
    {:$schema "https://vega.github.io/schema/vega-lite/v5.json"
     :data {:values (for [_ (range 60)]
                      {:x (rand 100) :y (rand 100)
                       :size (+ 10 (rand-int 40))})}
     :mark {:type "circle" :opacity 0.7}
     :encoding {:x {:field "x" :type "quantitative"}
                :y {:field "y" :type "quantitative"}
                :size {:field "size" :type "quantitative"}}})

  ;; Line chart
  (clerk/vl
    {:$schema "https://vega.github.io/schema/vega-lite/v5.json"
     :data {:values (for [x (range 0 20)]
                      {:x x :y (+ (* 2 x) (- (rand-int 10) 5))})}
     :mark {:type "line" :point true}
     :encoding {:x {:field "x" :type "quantitative"}
                :y {:field "y" :type "quantitative"}}})

  ;; Colored bar chart
  (clerk/vl
    {:$schema "https://vega.github.io/schema/vega-lite/v5.json"
     :data {:values [{:month "Jan" :sales 120}
                     {:month "Feb" :sales 180}
                     {:month "Mar" :sales 150}
                     {:month "Apr" :sales 210}
                     {:month "May" :sales 190}]}
     :mark "bar"
     :encoding {:x {:field "month" :type "ordinal"}
                :y {:field "sales" :type "quantitative"}
                :color {:field "month" :type "nominal"
                        :legend nil}}})

  ,)


;; ---
;; ## 7. LaTeX

(clerk/tex "E = mc^2")

(comment
  ;; Quadratic formula
  (clerk/tex "x = \\frac{-b \\pm \\sqrt{b^2 - 4ac}}{2a}")

  ;; Summation
  (clerk/tex "\\sum_{i=1}^{n} i = \\frac{n(n+1)}{2}")

  ;; Integral
  (clerk/tex "\\int_{0}^{\\infty} e^{-x^2} dx = \\frac{\\sqrt{\\pi}}{2}")

  ;; Matrix
  (clerk/tex
    "\\begin{bmatrix} 1 & 2 & 3 \\\\ 4 & 5 & 6 \\\\ 7 & 8 & 9 \\end{bmatrix}")

  ;; Euler's identity
  (clerk/tex "e^{i\\pi} + 1 = 0")

  ,)


;; ---
;; ## 8. Images

(clerk/html [:img {:src "https://clojure.org/images/clojure-logo-120b.png"
                   :alt "Clojure logo"
                   :width 120}])

(comment
  ;; Another image from URL
  (clerk/image "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1a/24701-nature-702-702.jpg/320px-24701-nature-702-702.jpg")

  ,)


;; ---
;; ## 9. Layouts

(clerk/row
  (clerk/md "**Left panel**")
  (clerk/md "**Right panel**"))

(comment
  ;; Side-by-side charts
  (clerk/row
    (clerk/plotly
      {:data [{:x [1 2 3] :y [10 20 30] :type "bar"}]
       :layout {:title "Chart A" :height 250 :width 300}})
    (clerk/plotly
      {:data [{:x [1 2 3] :y [30 10 20] :type "bar"
               :marker {:color "coral"}}]
       :layout {:title "Chart B" :height 250 :width 300}}))

  ;; Stacked column layout
  (clerk/col
    (clerk/md "### Top section")
    (clerk/html [:hr])
    (clerk/md "### Bottom section"))

  ;; Three items in a row
  (clerk/row
    (clerk/html
      [:div {:style {:background "#ffeeba" :padding "1rem"
                     :border-radius "8px" :text-align "center"}}
       "One"])
    (clerk/html
      [:div {:style {:background "#c3e6cb" :padding "1rem"
                     :border-radius "8px" :text-align "center"}}
       "Two"])
    (clerk/html
      [:div {:style {:background "#bee5eb" :padding "1rem"
                     :border-radius "8px" :text-align "center"}}
       "Three"]))

  ;; Caption on a chart
  (clerk/caption
    "Figure 1: A simple bar chart"
    (clerk/plotly
      {:data [{:x ["A" "B" "C"] :y [4 7 2] :type "bar"}]
       :layout {:height 250}}))

  ,)


;; ---
;; ## 10. Visibility

;; Use metadata to control what Clerk shows.

;; Show result only (hide the source code):
^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/md "_This markdown appears without its source code visible above it._")

(comment
  ;; Hide result, show code:
  ;; ^{:nextjournal.clerk/visibility {:result :hide}}
  ;; (def my-secret 42)

  ;; Hide both code and result (useful for setup forms):
  ;; ^{:nextjournal.clerk/visibility {:code :hide :result :hide}}
  ;; (def internal-state (atom {}))

  ,)


;; ---
;; ## 11. clerk/example

;; The `clerk/example` macro shows each form alongside its result,
;; which is great for documentation and teaching.

(clerk/example
  (+ 1 2 3)
  (map inc [1 2 3])
  (str "Hello, " "Clerk!"))

(comment
  (clerk/example
    (range 10)
    (filter odd? (range 20))
    (reduce + (range 1 101)))

  (clerk/example
    (zipmap [:a :b :c] [1 2 3])
    (frequencies "abracadabra")
    (group-by even? (range 10)))

  ,)


;; ---
;; ## 12. Putting It All Together

(comment
  ;; Compose a mini-dashboard with layouts and multiple viewers
  (clerk/col
    (clerk/md "## Mini Dashboard")
    (clerk/row
      (clerk/plotly
        {:data [{:x ["Q1" "Q2" "Q3" "Q4"]
                 :y [100 150 130 180]
                 :type "bar"
                 :marker {:color "steelblue"}}]
         :layout {:title "Revenue" :height 250 :width 350}})
      (clerk/vl
        {:$schema "https://vega.github.io/schema/vega-lite/v5.json"
         :width 300 :height 200
         :data {:values [{:month "Jan" :users 400}
                         {:month "Feb" :users 600}
                         {:month "Mar" :users 550}
                         {:month "Apr" :users 700}]}
         :mark {:type "line" :point true}
         :encoding {:x {:field "month" :type "ordinal"}
                    :y {:field "users" :type "quantitative"}}}))
    (clerk/table
      [{:metric "Revenue" :q1 100 :q2 150 :q3 130 :q4 180}
       {:metric "Users" :q1 400 :q2 600 :q3 550 :q4 700}
       {:metric "NPS" :q1 45 :q2 52 :q3 48 :q4 61}]))

  ,)
