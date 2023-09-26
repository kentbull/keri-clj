(ns keri.coring-test
  (:require [clojure.test :refer :all]
            [keri.coring :as kc]))

(deftest int-to-b64-test
  (testing "zero returns A"
    (let [cs (kc/int-to-b64 0)]
      (is (= "A" cs))))
  (testing "zeroth char and zero padding returns blank"
    (let [cs (kc/int-to-b64 0 0)]
      (is (= "" cs))))
  (testing "Nil char and padding zero returns blank"
    (let [cs (kc/int-to-b64 nil 0)]
      (is (= "" cs))))
  (testing "lowercase 'b' returned for 27"
    (let [cs (kc/int-to-b64 27)]
      (is (= "b" cs))))
  )