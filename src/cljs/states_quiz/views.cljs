(ns states-quiz.views
  (:require
   [re-frame.core :as rf]
   [states-quiz.events :as events]
   [states-quiz.subs :as subs]))

;; -----------------------------------------------------------------------------
;; Components

(defn- option-id [question option]
  (str (:id question) "_" option))

(defn question-view [question]
  [:fieldset
   [:legend [:b (:question question)]]
   (for [option (:options question)]
     ^{:key option}
     [:div
      [:input {:type :radio
               :name (:id question)
               :id (option-id question option)
               :value option
               :on-change #(rf/dispatch [::events/provide-answer
                                         (:id question)
                                         (.-target.value %)])}]
      [:label {:for (option-id question option)} option]])])

(defn correction-view [correction]
  (let [{:keys [question provided answer]} correction
        provided (if (empty? provided) "no answer" provided)]
    [:div
     [:p [:b question]
      [:br]
      [:text "You provided " provided ", but the correct answer is " answer "."]]]))

;; -----------------------------------------------------------------------------
;; Panels

(defmulti panel identity)

(defmethod panel :start-screen [_]
  [:div
   [:h1 "Select a test to start"]
   [:div.row
    [:div.columns.medium-6.large-4
     [:button.button
      {:type :button
       :on-click #(rf/dispatch [::events/start-capitals-test])}
      "Capitals Test"]]
    [:div.columns.medium-6.large-4
     [:button.button
      {:type :button
       :on-click #(rf/dispatch [::events/start-states-test])}
      "States Test"]]
    [:div.columns.medium-6.large-4
     [:button.button
      {:type :button
       :on-click #(rf/dispatch [::events/start-mixed-test])}
      "All Mixed Up!"]]]])

(defmethod panel :test-screen [_]
  (let [test (rf/subscribe [::subs/test])]
    [:div
     (for [question @test]
       ^{:key (:id question)}
       [question-view question])
     [:button.button {:on-click #(rf/dispatch [::events/check-answers])}
      "Check my answers!"]]))

(defmethod panel :results-screen [_]
  (let [number-correct (rf/subscribe [::subs/number-correct])
        score (rf/subscribe [::subs/score])
        corrections (rf/subscribe [::subs/corrections])]
   [:div
    [:h3 "You got " @number-correct " correct for a final score of " @score "!"]
    [:div
     [:p "Here is what you missed:"]
     (for [correction @corrections]
       ^{:key (:id correction)}
       [correction-view correction])]
    [:button.button {:on-click #(rf/dispatch [::events/start-over])} "Start Over"]]))

;; -----------------------------------------------------------------------------
;; Bootstrap

(defn main-panel []
  (let [panel-name (rf/subscribe [::subs/panel-name])]
    (fn []
     (panel @panel-name))))
