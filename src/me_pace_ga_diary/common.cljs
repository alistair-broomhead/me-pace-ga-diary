(ns me-pace-ga-diary.common
    (:require [reagent.core :as r :refer [atom]]
              [re-frame.core :refer [subscribe dispatch dispatch-sync]]
              [me-pace-ga-diary.events]
              [me-pace-ga-diary.subs]))
  
  (def ReactNative (js/require "react-native"))
  
  (def app-registry (.-AppRegistry ReactNative))
  (def text (r/adapt-react-class (.-Text ReactNative)))
  (def view (r/adapt-react-class (.-View ReactNative)))
  (def image (r/adapt-react-class (.-Image ReactNative)))
  (def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))
  
  (def styles {
    :default {:font-size 16 :font-weight "100" :margin 20 :text-align "center"}
    :heading {:font-size 30 :font-weight "100" :margin-top -20 :margin-bottom 20 :text-align "center"}
    :footer {:font-size 16 :font-weight "100" :padding-top "auto" :height "auto" :text-align "center"}

  })
  (def logo-img (js/require "./images/cljs.png"))
  

  (defn do-init [app-root]
    (dispatch-sync [:initialize-db])
    (.registerComponent app-registry "MePaceGaDiary" #(r/reactify-component app-root)))
  
  (defn get-root []
    (let [db (subscribe [:get-db])]
      (letfn [
          (get-db [label] (label @db))
          (create-text
            ([string-input] (create-text string-input :default))
            ([string-input style] [text {:style (style styles)} string-input])
          )
          (text-from-db 
            ([text-label] (create-text (get-db text-label)))
            ([text-label style-label] (create-text (get-db text-label) style-label)))
        ]
        (fn []
          [view {:style {:flex-direction "column" :margin 40 :align-items "center"}}
           (text-from-db :greeting :heading)
           [image {
              :source logo-img 
              :style {:width 80 :height 80 :margin 10}}]
           (text-from-db :ending :footer)
          ]))))
  
  