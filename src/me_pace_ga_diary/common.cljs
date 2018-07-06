(ns me-pace-ga-diary.common
    (:require [reagent.core :as r]
              [re-frame.core :refer [dispatch 
                                     dispatch-sync
                                     subscribe]]
              [me-pace-ga-diary.events]
              [me-pace-ga-diary.subs]
              [me-pace-ga-diary.ui :as ui]))

(println "Loading common")
  
(def ReactNative (js/require "react-native"))

(def logo-img (js/require "./images/cljs.png"))

(defn do-init [app-root]
  (let [app-registry (.-AppRegistry ReactNative)]
  (dispatch-sync [:initialize-db])
  (dispatch [:load-db])
  (.registerComponent app-registry "MePaceGaDiary" #(r/reactify-component app-root))))

(defn unwrap-value [handler]
  (fn [value] (handler (-> value
                          .-nativeEvent
                          .-text))))

(defn set-value 
  ([key]          (unwrap-value #(dispatch [:store key %1])))
  ([key & further](unwrap-value (fn [value] 
                                  (dispatch [:store key value]) 
                                  (dispatch (vec further))))))

(let [db (subscribe [:get-db])]
  (defn app-root
    []
    [ui/view (ui/styled :view-main)
        [ui/h1 "Hai!"]
        [ui/image (ui/styled :logo 
                             :source logo-img)]
        [ui/input {:style           {:width "100%"}
                   :placeholder     "Key"
                   :on-end-editing  (set-value :k)} (:k @db)]
        [ui/input {:ref "value"
                   :style           {:width "100%"}
                   :placeholder     "Value"
                   :on-end-editing  (set-value :v :save-db)} (:v @db)]
        [ui/h2 ""]
        
        [ui/h3 "DB content:"]
        [ui/p (js/JSON.stringify (clj->js @db))]
    ]))

  