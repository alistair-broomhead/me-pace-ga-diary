(ns me-pace-ga-diary.events
  (:require
   [re-frame.core :refer [reg-event-db reg-event-fx inject-cofx after dispatch]]
   [clojure.spec.alpha :as s]
   [cljs-time.local :refer [local-now]]
   [me-pace-ga-diary.db :as db :refer [app-db]]
   [me-pace-ga-diary.ui :as ui]
   [goog.date]
   ))

(println "Loading events")
;; -- Interceptors ------------------------------------------------------------
;;
;; See https://github.com/Day8/re-frame/blob/master/docs/Interceptors.md
;;
(defn check-and-throw
  "Throw an exception if db doesn't have a valid spec."
  [spec db [event]]
  (when-not (s/valid? spec db)
    (let [explain-data (s/explain-data spec db)]
      (throw (ex-info (str "Spec check after " event " failed: " explain-data) explain-data)))))

(def validate-spec
  (if goog.DEBUG
    (after (partial check-and-throw ::db/app-db))
    []))

;; -- Handlers --------------------------------------------------------------

(defn reg-db
  [name handler]
  (reg-event-db name validate-spec (fn
    [db [event-name & rest]]
    (apply handler db rest))))

(reg-db :initialize-db
 (fn [db]
  db/app-db))

(reg-db :load-db
 (fn [state]
    (db/load state)
    state))

(reg-db :save-db
 (fn [state]
    (db/save! state)
    state))

(defn to-key
  [input']
    (if (string? input')
      (keyword input')
      input'))

(reg-db :tick
  (fn [old-state]
    (assoc-in old-state [:cache :now] (local-now))))

(reg-db :store
  (fn [old-state key' value]
    (let [key-chain (map to-key key')]
        (println "storing" key-chain value "in" old-state)
        (assoc-in old-state key-chain value))))

(reg-db :enter-info
  (fn [old-state entry]
    (let [now   (-> old-state :cache :now)

          ]

      )))

(reg-db :store-all
  (fn [old-state new-state]
    (dispatch [:save-db])
    (merge-with merge old-state {:persist new-state})))

