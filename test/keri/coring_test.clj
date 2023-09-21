(ns keri.coring-test
  (:require [clojure.test :refer :all]
            [keri.coring :as kc]))

(deftest verex-matcher-test
  (testing "Verex matcher finds each part of the expression"
    (let [matcher (kc/rever "KERI10JSON000000_")
          match-found (.find matcher)
          [_ proto major minor kind size] (re-groups matcher)]
      (is (= true match-found))
      (is (= "KERI" proto))
      (is (= "1" major))
      (is (= "0" minor))
      (is (= "JSON" kind))
      (is (= "000000" size)))
    ))