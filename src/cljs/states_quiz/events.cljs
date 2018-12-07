(ns states-quiz.events
  (:require
   [states-quiz.db :as db]
   [states-quiz.quiz :as tests]
   [re-frame.core :as rf]))

(rf/reg-fx ::scroll-to-top
  (fn [_ _]
    (set! js/document.body.scrollTop 0)
    (set! js/document.documentElement.scrollTop 0)))

(defn set-panel [{db :db} [_ panel-name]]
  {:db (assoc db :panel-name panel-name)
   ::scroll-to-top nil})

(rf/reg-event-fx ::set-panel set-panel)

;; -----------------------------------------------------------------------------
;; Test events

(defn start-capitals-test [{db :db} _]
  {:db (assoc db :test (tests/create-capitals-test))
   :dispatch [::set-panel :test-screen]})

(defn start-states-test [{db :db} _]
  {:db (assoc db :test (tests/create-states-test))
   :dispatch [::set-panel :test-screen]})

(defn start-mixed-test [{db :db} _]
    {:db (assoc db :test (tests/create-mixed-test))
   :dispatch [::set-panel :test-screen]})

(defn provide-answer [{db :db} [_ question-id answer]]
  {:db (assoc-in db [:answers question-id] answer)})

(defn check-answers [{db :db} _]
  {:dispatch [::set-panel :results-screen]})

(rf/reg-event-fx ::start-capitals-test start-capitals-test)
(rf/reg-event-fx ::start-states-test start-states-test)
(rf/reg-event-fx ::start-mixed-test start-mixed-test)
(rf/reg-event-fx ::provide-answer provide-answer)
(rf/reg-event-fx ::check-answers check-answers)

;; -----------------------------------------------------------------------------
;; Initialization

(defn start-over [{db :db} _]
  {:db (dissoc db :test :answers)
   :dispatch [::set-panel :start-screen]})

(rf/reg-event-fx ::start-over start-over)

(rf/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))
