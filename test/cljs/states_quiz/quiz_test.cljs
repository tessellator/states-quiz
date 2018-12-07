(ns states-quiz.quiz-test
  (:require [states-quiz.quiz :as quiz]
            [cljs.test :refer-macros [deftest is]]))

(deftest create-capital-question-test
  (let [question (quiz/create-capital-question ["Alabama" "Montgomery"])]
    (is (= "What is the capital of Alabama?" (:question question)))))
