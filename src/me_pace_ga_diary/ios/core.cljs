(ns me-pace-ga-diary.android.core
  (:require [me-pace-ga-diary.common :as common]))

(defn app-root [] (common/get-root))

(defn init []
      (common/do-init app-root))