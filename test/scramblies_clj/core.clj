(ns test.scramblies-clj.core
  (:require [scramblies-clj.core :refer :all]
            [clojure.test :refer [deftest testing is]]))

(deftest
  scramble?-test
  (is (= (scramble? "rekqodlw" "world") true))
  (is (= (scramble? "cedewaraaossoqqyt" "codewars") true))
  (is (= (scramble? "katas" "steak") false))
  (is (= (scramble? "abc" "aabc") false))
  (is (= (scramble? "dovalle" "dalloca") false)))

