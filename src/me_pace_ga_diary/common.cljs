(ns me-pace-ga-diary.common
   (:require
     [cljs-time.format :refer [formatters unparse]]
     [cljs-time.core   :refer [date?]]
     [reagent.core :as r]
     [re-frame.core :refer [dispatch
                            dispatch-sync
                            subscribe]]
     [me-pace-ga-diary.events]
     [me-pace-ga-diary.subs]
     [me-pace-ga-diary.ui :as ui]))

(println "Loading common")

(def ReactNative (js/require "react-native"))

(def logo-img (js/require "./images/cljs.png"))

(let [app-registry (.-AppRegistry ReactNative)]
  (defn do-init [app-root]
    (dispatch-sync [:initialize-db])
    (js/setInterval #(dispatch [:tick]) 1000)
    (dispatch [:load-db])
    (.registerComponent
      app-registry
      "MePaceGaDiary"
      #(r/reactify-component app-root))))

(let [db            (subscribe [:get-state])
      cache-key     (subscribe [:get-from-cache :k])
      cache-val     (subscribe [:get-from-cache :v])
      now           (subscribe [:get-from-cache :now])
      get-ref       (fn [parent key] ((js->clj  (.-refs parent)) key))
      unwrap        #(-> %1 .-nativeEvent .-text)
      store         #(dispatch [:store [%1 %2] (unwrap %3)])
      set-cache     (partial store :cache)
      set-db        (fn [k v]
                      (store :persist k v)
                      (dispatch [:save-db]))
      to-str        #(cond
                      (= %1 nil)   ""
                      (date? %1)   (unparse (%2 formatters) %1)
                      (string? %1) %1
                      :else        (str %1))
      ]
  (defn app-root
    []
    (let
      [page      (r/current-component)
       focus-el  #(.focus ((js->clj (.-refs page)) %1))]
      [ui/view (ui/styled :view-main)
        [ui/h1 (to-str @now :date)]
        [ui/h2 (to-str @now :hour-minute-second)]
        [ui/input {:style             {:width "100%"}
                   :ref               "key"
                   :placeholder       "Key"
                   :on-change         (partial set-cache :k)
                   :on-submit-editing (fn [value]
                                        (focus-el "value")
                                        (set-cache :k value))}
                  [to-str @cache-key]]
        [ui/input {:style             {:width "100%"}
                   :ref               "value"
                   :placeholder       "Value"
                   :on-change         (partial set-cache :v)
                   :on-submit-editing (fn [value]
                                        (focus-el "key")
                                        (set-cache :v value)
                                        (set-db @cache-key value))}
                  [to-str @cache-val]]
        [ui/h2 " "]
        [ui/h3 "App State:"]
        [ui/p (str @db)]
    ])))
