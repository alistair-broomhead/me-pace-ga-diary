(ns me-pace-ga-diary.android.core
  (:require [me-pace-ga-diary.common :as common]))

(def app-root common/app-root)

(defn init []
      (common/do-init app-root))
