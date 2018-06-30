(ns me-pace-ga-diary.db
  (:require [clojure.spec.alpha :as s]))

;; spec of app-db
(s/def ::greeting string?)
(s/def ::ending string?)
(s/def ::app-db
  (s/keys :req-un [::greeting ::ending]))

;; initial state of app-db
(def app-db {
  :greeting "Hello from clojurescript!"
  :ending   "See you again soon!"})
