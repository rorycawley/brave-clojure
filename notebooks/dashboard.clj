(ns dashboard
  (:require
    [babashka.fs :as fs]
    [clojure.string :as str]
    [nextjournal.clerk :as clerk]))


;; ---------------------------------------------------------------------------
;; Helpers
;; ---------------------------------------------------------------------------

(defn- chapter-files
  []
  (->> (fs/glob "notebooks" "*.clj")
       (map str)
       (remove #(str/ends-with? % "dashboard.clj"))
       sort))


(defn- chapter-card
  "Render a single chapter file as a styled card linking to the notebook."
  [path]
  (let [fname (fs/file-name path)
        href (str/replace path #"^notebooks/" "")]
    (clerk/html
      [:a {:href href
           :style {:text-decoration "none" :color "inherit"}}
       [:div {:style {:background "#f8f9fa"
                      :border "1px solid #dee2e6"
                      :border-radius "10px"
                      :padding "1rem 1.25rem"
                      :transition "box-shadow 0.2s"
                      :cursor "pointer"}}
        [:strong (str/replace (str/replace fname #"\.clj$" "")
                              #"_" " ")]]])))


;; ---------------------------------------------------------------------------
;; 1. Hero — clerk/html + visibility (hide source for clean look)
;; ---------------------------------------------------------------------------

^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/html
  [:div {:style {:background "linear-gradient(135deg, #4a90d9 0%, #7b68ee 100%)"
                 :color "white"
                 :padding "2.5rem 2rem"
                 :border-radius "12px"
                 :text-align "center"
                 :margin-bottom "1rem"}}
   [:h1 {:style {:margin 0 :font-size "2.2rem"}}
    "Brave Clojure — Clerk Showcase"]
   [:p {:style {:margin-top "0.5rem" :font-size "1.1rem" :opacity 0.9}}
    "A living notebook playground for learning Clojure with Clerk"]])


;; ---------------------------------------------------------------------------
;; 2. Markdown prose — clerk/md
;; ---------------------------------------------------------------------------

(clerk/md
  "Welcome! This dashboard **is itself a showcase** of
[Clerk](https://book.clerk.vision) features.
Every section below uses a different viewer — read the source to see how
each one works. Use the chapter links at the bottom to dive deeper.")


;; ---------------------------------------------------------------------------
;; 3. Feature grid — clerk/row + clerk/html cards
;; ---------------------------------------------------------------------------

(clerk/md "## What Can Clerk Render?")


^{:nextjournal.clerk/visibility {:code :hide}}
(clerk/row
  (clerk/html
    [:div {:style {:background "#e8f5e9" :padding "1rem" :border-radius "10px"
                   :text-align "center" :min-width "120px"}}
     [:div {:style {:font-size "1.6rem"}} "M↓"]
     [:strong "Markdown"]
     [:div {:style {:font-size "0.85rem" :color "#555"}} "Prose & docs"]])
  (clerk/html
    [:div {:style {:background "#e3f2fd" :padding "1rem" :border-radius "10px"
                   :text-align "center" :min-width "120px"}}
     [:div {:style {:font-size "1.6rem"}} "||"]
     [:strong "Tables"]
     [:div {:style {:font-size "0.85rem" :color "#555"}} "Structured data"]])
  (clerk/html
    [:div {:style {:background "#fff3e0" :padding "1rem" :border-radius "10px"
                   :text-align "center" :min-width "120px"}}
     [:div {:style {:font-size "1.6rem"}} "/\\"]
     [:strong "Charts"]
     [:div {:style {:font-size "0.85rem" :color "#555"}} "Plotly & Vega-Lite"]])
  (clerk/html
    [:div {:style {:background "#fce4ec" :padding "1rem" :border-radius "10px"
                   :text-align "center" :min-width "120px"}}
     [:div {:style {:font-size "1.6rem"}} "∑"]
     [:strong "LaTeX"]
     [:div {:style {:font-size "0.85rem" :color "#555"}} "Math formulas"]])
  (clerk/html
    [:div {:style {:background "#f3e5f5" :padding "1rem" :border-radius "10px"
                   :text-align "center" :min-width "120px"}}
     [:div {:style {:font-size "1.6rem"}} "</>"]
     [:strong "HTML"]
     [:div {:style {:font-size "0.85rem" :color "#555"}} "Hiccup & SVG"]])
  (clerk/html
    [:div {:style {:background "#e0f7fa" :padding "1rem" :border-radius "10px"
                   :text-align "center" :min-width "120px"}}
     [:div {:style {:font-size "1.6rem"}} "[ ]"]
     [:strong "Layouts"]
     [:div {:style {:font-size "0.85rem" :color "#555"}} "row / col / grid"]]))


;; ---------------------------------------------------------------------------
;; 4. Live mini-charts — clerk/plotly + clerk/vl side by side
;; ---------------------------------------------------------------------------

(clerk/md "## Live Charts")


(clerk/row
  (clerk/plotly
    {:data [{:x ["Clojure" "Python" "Rust" "Go"]
             :y [95 88 92 85]
             :type "bar"
             :marker {:color ["#4a90d9" "#e6a817" "#dea584" "#69d2e7"]}}]
     :layout {:title "Language Satisfaction (Plotly)"
              :yaxis {:title "Score"}
              :height 280
              :margin {:t 40 :b 40 :l 50 :r 20}}})
  (clerk/vl
    {:$schema "https://vega.github.io/schema/vega-lite/v5.json"
     :width 280
     :height 220
     :title "Category Values (Vega-Lite)"
     :data {:values [{:category "A" :value 28}
                     {:category "B" :value 55}
                     {:category "C" :value 43}
                     {:category "D" :value 91}]}
     :mark {:type "bar" :cornerRadiusTopLeft 4 :cornerRadiusTopRight 4}
     :encoding {:x {:field "category" :type "nominal" :title "Category"}
                :y {:field "value" :type "quantitative" :title "Value"}
                :color {:field "category" :type "nominal" :legend nil}}}))


;; ---------------------------------------------------------------------------
;; 5. LaTeX sample — clerk/tex
;; ---------------------------------------------------------------------------

(clerk/md "## LaTeX Rendering")

(clerk/tex "\\int_{0}^{\\infty} e^{-x^2}\\, dx = \\frac{\\sqrt{\\pi}}{2}")


;; ---------------------------------------------------------------------------
;; 6. Code example — clerk/example
;; ---------------------------------------------------------------------------

(clerk/md "## Code Examples")


(clerk/example
  (+ 1 2 3)
  (map inc [10 20 30])
  (frequencies "abracadabra"))


;; ---------------------------------------------------------------------------
;; 7. Table — clerk/table with chapter metadata
;; ---------------------------------------------------------------------------

(clerk/md "## Viewer Reference")


(clerk/table
  [{:viewer "`clerk/md`" :purpose "Markdown prose" :example "`(clerk/md \"# Title\")`"}
   {:viewer "`clerk/html`" :purpose "Raw HTML via Hiccup" :example "`(clerk/html [:div ...])`"}
   {:viewer "`clerk/table`" :purpose "Data tables" :example "`(clerk/table [{:a 1}])`"}
   {:viewer "`clerk/plotly`" :purpose "Plotly charts" :example "`(clerk/plotly {:data [...]})`"}
   {:viewer "`clerk/vl`" :purpose "Vega-Lite charts" :example "`(clerk/vl {:mark \"bar\" ...})`"}
   {:viewer "`clerk/tex`" :purpose "LaTeX formulas" :example "`(clerk/tex \"E=mc^2\")`"}
   {:viewer "`clerk/code`" :purpose "Syntax-highlighted code" :example "`(clerk/code \"(+ 1 2)\")`"}
   {:viewer "`clerk/example`" :purpose "Form + result pairs" :example "`(clerk/example (+ 1 2))`"}
   {:viewer "`clerk/row`" :purpose "Horizontal layout" :example "`(clerk/row a b c)`"}
   {:viewer "`clerk/col`" :purpose "Vertical layout" :example "`(clerk/col a b c)`"}])


;; ---------------------------------------------------------------------------
;; 8. Chapter links — dynamic discovery, styled cards
;; ---------------------------------------------------------------------------

(clerk/md "## Chapters")


^{:nextjournal.clerk/visibility {:code :hide}}
(let [files (chapter-files)]
  (if (seq files)
    (apply clerk/row (map chapter-card files))
    (clerk/md "_No chapter notebooks found yet. Copy `ch00_template.clj` to get started!_")))
