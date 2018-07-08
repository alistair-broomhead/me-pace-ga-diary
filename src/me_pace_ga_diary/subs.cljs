(ns me-pace-ga-diary.subs
  (:require [re-frame.core :refer [reg-sub]]))

(println "Loading subs")
(letfn [(get-from
          [prefix]
          (fn
            [state [_ & key-chain]]
            (get-in state (into [prefix] key-chain))))]
  (reg-sub :get-from-db     (get-from :persist))
  (reg-sub :get-from-cache  (get-from :cache)))

(reg-sub
  :get-state
  (fn [state _] state))

