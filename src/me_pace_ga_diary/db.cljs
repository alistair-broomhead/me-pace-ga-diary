(ns me-pace-ga-diary.db
  (:require [clojure.spec.alpha :as s]
   [re-frame.core :refer [dispatch]]))

(println "Loading db")

;; spec of app-db
(s/def ::cache
  (s/keys :req-un []))
(s/def ::persist
  (s/keys :req-un []))
(s/def ::app-db
  (s/keys :req-un [::cache ::persist]))

;; initial state of app-db
(def app-db {:persist {}
             :cache   {}})

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
      (.then #(if (= %1 nil) {} %1))
      (.then #(dispatch [:store-all %1]))))

  (defn save! [new-state]
    (println "saving new-state:" new-state)
    (->
    AsyncStorage
      (.setItem "@MEPaceGADiary:db" (map->json (:persist new-state))))))
