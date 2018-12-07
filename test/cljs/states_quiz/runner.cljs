(ns states-quiz.runner
  (:require [doo.runner :refer-macros [doo-all-tests]]
            [states-quiz.quiz-test]))

(enable-console-print!)

; make sure to require the names above for the tests to run
(doo-all-tests #"(states-quiz).*-test")
