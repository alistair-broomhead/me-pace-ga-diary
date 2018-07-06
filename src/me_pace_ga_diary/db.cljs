(ns me-pace-ga-diary.db
  (:require [clojure.spec.alpha :as s]
   [re-frame.core :refer [dispatch]]))

(println "Loading db")

;; spec of app-db
(s/def ::app-db
  (s/keys :req-un []))

;; initial state of app-db
(def app-db {})

(defn map->json 
  [mapping]
  (-> mapping
      clj->js
      js/JSON.stringify))
  

(defn json->map 
  [json]
  (-> json
      js/JSON.parse
      (js->clj :keywordize-keys true)))

(let [db-key        "@MEPaceGADiary:db"
      AsyncStorage  (.-AsyncStorage (js/require "react-native"))]
      
  (defn load [old-state]
    (->
      AsyncStorage
      (.getItem db-key)
      (.then json->map)
      (.then  (fn [new-state]
                (dispatch [:store-all new-state])))))
              
  (defn save! [new-state]
    (println "saving new-state:" new-state)
    (->
    AsyncStorage
      (.setItem "@MEPaceGADiary:db" (map->json new-state)))))
