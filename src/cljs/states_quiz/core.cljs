(ns states-quiz.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [states-quiz.events :as events]
   [states-quiz.views :as views]
   [states-quiz.config :as config]))

;; -----------------------------------------------------------------------------
;; Bootstrap

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
