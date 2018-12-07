(ns states-quiz.subs
  (:require
   [re-frame.core :as rf]
   [states-quiz.quiz :as tests]))

(rf/reg-sub ::panel-name
  (fn [db]
    (:panel-name db)))

(rf/reg-sub ::test
  (fn [db]
    (:test db)))

(rf/reg-sub ::answers
  (fn [db]
    (:answers db)))

(rf/reg-sub ::graded-test
  :<- [::test]
  :<- [::answers]
  (fn [[test answers]]
    (tests/grade-test test answers)))

(rf/reg-sub ::number-correct
  :<- [::graded-test]
  (fn [graded-test]
    (:number-correct graded-test)))

(rf/reg-sub ::number-incorrect
  :<- [::graded-test]
  (fn [graded-test]
    (:number-incorrect graded-test)))

(rf/reg-sub ::score
  :<- [::number-correct]
  :<- [::number-incorrect]
  (fn [[number-correct number-incorrect]]
    (int (* 100 (/ number-correct (+ number-correct number-incorrect))))))

(rf/reg-sub ::corrections
  :<- [::graded-test]
  (fn [graded-test]
    (:corrections graded-test)))
