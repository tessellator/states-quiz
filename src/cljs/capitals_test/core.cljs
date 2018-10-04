(ns capitals-test.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [capitals-test.events :as events]
   [capitals-test.views :as views]
   [capitals-test.config :as config]
   ))

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
