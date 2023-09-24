(ns keri.kering-test
  (:require [clojure.test :refer :all]
            [keri.kering :as kr]))

(deftest verex-matcher-test
  (testing "Verex matcher finds each part of the expression"
    (let [matcher (kr/rever "KERI10JSON000000_")
          match-found (.find matcher)
          [_ proto major minor kind size] (re-groups matcher)]
      (is (= true match-found))
      (is (= "KERI" proto))
      (is (= "1" major))
      (is (= "0" minor))
      (is (= "JSON" kind))
      (is (= "000000" size)))
    )
  (testing "versify KERI default version JSON size 0"
    (let [vs (kr/versify {:kind (:json kr/serials) :size 0})
          dvs (kr/deversify vs)]
      (is (= "KERI10JSON000000_" vs))
      (is (= (count vs) kr/verfullsize))
      (is (and (= (:keri kr/protos) (:proto dvs))
            (= (:json kr/serials) (:kind dvs))
            (= kr/version (:version dvs))
            (= 0 (:size dvs))))))
  (testing "versify KERI default version JSON size 65"
    (let [vs (kr/versify {:kind (:json kr/serials) :size 65})
          dvs (kr/deversify vs)]
      (is (= "KERI10JSON000041_" vs))
      (is (= (count vs) kr/verfullsize))
      (is (and (= (:keri kr/protos) (:proto dvs))
            (= (:json kr/serials) (:kind dvs))
            (= kr/version (:version dvs))
            (= 65 (:size dvs))))))
  ;TODO more test cases
  ; acdc, json, different sizes
  ; keri mgpk 0
  ; keri mgpk 65
  ; acdc cbor 0
  ; acdc cbor 65
  )