(ns me-pace-ga-diary.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :get-db
  (fn [db _] db))

(reg-sub
  :get-greeting
  (fn [db _]
    (:greeting db)))