(ns me-pace-ga-diary.ui
    (:require [reagent.core :as r   :refer [adapt-react-class]]
              [re-frame.core        :refer [reg-sub]]
              [me-pace-ga-diary.db  :refer [map->json]]
              [clojure.string       :refer [join]]))
    
(println "Loading ui")

(def styles {
    :h1        {:font-size 30 
                :font-weight    "100"}
    :h2        {:font-size      24 
                :font-weight    "100"}
    :h3        {:font-size      18 
                :font-weight    "100"}
    :p         {:font-size      16}
    :view-main {:flex-direction "column" 
                :align-items    "center"
                :margin         20
                :margin-right   40
                :margin-left    40}
    :logo      {:width          80 
                :height         80 
                :margin         10}
})

(defn styled
    [style-label & other]
    (apply hash-map :style (style-label styles) other))
    
(def ReactNative (js/require "react-native"))

;; Slurp a load of RN functionality into our namespace

(def alert'      (adapt-react-class (.-Alert ReactNative)))
(defn alert [str]
    (println str)
    (alert'  str))
(defn alert-map [map] (-> map
                          map->json
                          alert))
(defn alert-vec [& vec] (->> vec
                             (map str)
                             (join " ")
                             alert))
(def view       (adapt-react-class (.-View ReactNative)))
(def image      (adapt-react-class (.-Image ReactNative)))
(let
    [text'      (adapt-react-class (.-Text ReactNative))]
    (defn text  ;; Utility to make text easier to work with
        ([style content & override]     [text' {:style (apply assoc (style styles) override)} content])
        ([style content]                [text' (styled style) content])
        ([content]                      [text' content])))

(def picker      (adapt-react-class (.-Picker      ReactNative)))
(def picker-item (adapt-react-class (.-Picker.Item ReactNative)))

(def list-view   (adapt-react-class (.-ListView    ReactNative)))
(def scroll-view (adapt-react-class (.-ScrollView  ReactNative)))
(def input       (adapt-react-class (.-TextInput   ReactNative)))
(def touchable-highlight
                 (adapt-react-class (.-TouchableHighlight ReactNative)))
(defn alert [title] (.alert (.-Alert ReactNative) title))


;; Convenience

(letfn 
    [(styled-text [style] (fn 
        ([content] [text style content])
        ([content & override] [text style content override])))]
    (def h1 (styled-text :h1))
    (def h2 (styled-text :h2))
    (def h3 (styled-text :h3))
    (def p (styled-text :p)))