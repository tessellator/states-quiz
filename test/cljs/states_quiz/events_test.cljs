(ns states-quiz.events-test
  (:require [states-quiz.events :as events]
            [cljs.test :refer-macros [deftest testing is]]))

(deftest start-capitals-test-test
  (let [{:keys [db dispatch]} (events/start-capitals-test {} nil)]
    (is (some? (:test db)) "the new db should contain a test")
    (is (= [::events/set-panel :test-screen] dispatch) "it should trigger an event to go to the test screen")))
