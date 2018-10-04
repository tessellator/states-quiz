(ns capitals-test.tests)

(def state-capitals
  {"Alabama" "Montgomery"
   "Alaska" "Juneau"
   "Arizona" "Phoenix"
   "Arkansas" "Little Rock"
   "California" "Sacramento"
   "Colorado" "Denver"
   "Connecticut" "Hartford"
   "Delaware" "Dover"
   "Florida" "Tallahassee"
   "Georgia" "Atlanta"
   "Hawaii" "Honolulu"
   "Idaho" "Boise"
   "Illinois" "Springfield"
   "Indiana" "Indianapolis"
   "Iowa" "Des Moines"
   "Kansas" "Topeka"
   "Kentucky" "Frankfort"
   "Louisiana" "Baton Rouge"
   "Maine" "Augusta"
   "Maryland" "Annapolis"
   "Massachusetts" "Boston"
   "Michigan" "Lansing"
   "Minnesota" "Saint Paul"
   "Mississippi" "Jackson"
   "Missouri" "Jefferson City"
   "Montana" "Helena"
   "Nebraska" "Lincoln"
   "Nevada" "Carson City"
   "New Hampshire" "Concord"
   "New Jersey" "Trenton"
   "New Mexico" "Santa Fe"
   "New York" "Albany"
   "North Carolina" "Raleigh"
   "North Dakota" "Bismarck"
   "Ohio" "Columbus"
   "Oklahoma" "Oklahoma City"
   "Oregon" "Salem"
   "Pennsylvania" "Harrisburg"
   "Rhode Island" "Providence"
   "South Carolina" "Columbia"
   "South Dakota" "Pierre"
   "Tennessee" "Nashville"
   "Texas" "Austin"
   "Utah" "Salt Lake City"
   "Vermont" "Montpelier"
   "Virginia" "Richmond"
   "Washington" "Olympia"
   "West Virginia" "Charleston"
   "Wisconsin" "Madison"
   "Wyoming" "Cheyenne"})

(def capital-states
  (into {} (map (comp vec reverse) state-capitals)))

(def states
  (into #{} (keys state-capitals)))

(def capitals
  (into #{} (vals state-capitals)))

;; -----------------------------------------------------------------------------
;; Create questions

(defn create-capital-question [[state capital]]
  {:question (str "What is the capital of " state "?")
   :id state
   :type :capital
   :options (shuffle (concat [capital] (take 4 (shuffle (disj capitals capital)))))})

(defn create-state-question [[state capital]]
  {:question (str "Which state has " capital " as its capital?")
   :id capital
   :type :state
   :options (shuffle (concat [state] (take 4 (shuffle (disj states state)))))})

(defn create-random-question
  ([[state capital]] (create-random-question [state capital] (rand)))
  ([[state capital] r]
   (if (>= r 0.5)
     (create-capital-question [state capital])
     (create-state-question [state capital]))))

;; -----------------------------------------------------------------------------
;; Create tests

(defn create-capitals-test []
  (mapv create-capital-question (shuffle state-capitals)))

(defn create-states-test []
  (mapv create-state-question (shuffle state-capitals)))

(defn create-mixed-test []
  (mapv create-random-question (shuffle state-capitals)))

;; -----------------------------------------------------------------------------
;; Grading

(defmulti check-answer (fn [question responses] (:type question)))

(defmethod check-answer :capital [question responses]
  (let [answer (get state-capitals (:id question))
        provided (get responses (:id question))]
    {:id (:id question)
     :question (:question question)
     :answer answer
     :provided provided
     :correct? (= answer provided)}))

(defmethod check-answer :state [question responses]
  (let [answer (get capital-states (:id question))
        provided (get responses (:id question))]
    {:id (:id question)
     :question (:question question)
     :answer answer
     :provided provided
     :correct? (= answer provided)}))

(defn grade-test [test answers]
  (let [checked-answers (map #(check-answer % answers) test)
        corrections (filter (complement :correct?) checked-answers)]
    {:number-correct (- (count test) (count corrections))
     :number-incorrect (count corrections)
     :corrections corrections
     :checked-answers checked-answers}))
