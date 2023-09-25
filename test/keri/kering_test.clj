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
      (is (= "000000" size))))
  (testing "versify KERI default version JSON size 0"
    (let [vs (kr/versify {:proto (:keri kr/protos) :version kr/vrsn :kind (:json kr/serials) :size 0})
          dvs (kr/deversify vs)]
      (is (= "KERI10JSON000000_" vs))
      (is (= (count vs) kr/verfullsize))
      (is (= (:keri kr/protos) (:proto dvs)))
      (is (= (:json kr/serials) (:kind dvs)))
      (is (= kr/vrsn (:version dvs)))
      (is (= 0 (:size dvs)))))
  (testing "versify KERI default version JSON size 65"
    (let [vs (kr/versify {:proto (:keri kr/protos) :version kr/vrsn :kind (:json kr/serials) :size 65})
          dvs (kr/deversify vs)]
      (is (= "KERI10JSON000041_" vs))
      (is (= (count vs) kr/verfullsize))
      (is (= (:keri kr/protos) (:proto dvs)))
      (is (= (:json kr/serials) (:kind dvs)))
      (is (= kr/vrsn (:version dvs)))
      (is (= 65 (:size dvs)))))
  (testing "versify ACDC default version JSON size 86"
    (let [vs (kr/versify {:proto (:acdc kr/protos) :version kr/vrsn :kind (:json kr/serials) :size 86})
          dvs (kr/deversify vs)]
      (is (= "ACDC10JSON000056_" vs))
      (is (= (count vs) kr/verfullsize))
      (is (= (:acdc kr/protos) (:proto dvs)))
      (is (= (:json kr/serials) (:kind dvs)))
      (is (= kr/vrsn (:version dvs)))
      (is (= 86 (:size dvs)))))
  (testing "versify ACDC default version JSON size 0"
    (let [vs (kr/versify {:proto (:acdc kr/protos) :version kr/vrsn :kind (:json kr/serials) :size 0})
          dvs (kr/deversify vs)]
      (is (= "ACDC10JSON000000_" vs))
      (is (= (count vs) kr/verfullsize))
      (is (= (:acdc kr/protos) (:proto dvs)))
      (is (= (:json kr/serials) (:kind dvs)))
      (is (= kr/vrsn (:version dvs)))
      (is (= 0 (:size dvs)))))
  (testing "versify KERI default version MGPK size 0"
    (let [vs (kr/versify {:proto (:keri kr/protos) :version kr/vrsn :kind (:mgpk kr/serials) :size 0})
          dvs (kr/deversify vs)]
      (is (= "KERI10MGPK000000_" vs))
      (is (= (count vs) kr/verfullsize))
      (is (= (:keri kr/protos) (:proto dvs)))
      (is (= (:mgpk kr/serials) (:kind dvs)))
      (is (= kr/vrsn (:version dvs)))
      (is (= 0 (:size dvs)))))
  (testing "versify KERI default version mgpk size 65"
    (let [vs (kr/versify {:proto (:keri kr/protos) :version kr/vrsn :kind (:mgpk kr/serials) :size 65})
          dvs (kr/deversify vs)]
      (is (= "KERI10MGPK000041_" vs))
      (is (= (count vs) kr/verfullsize))
      (is (= (:keri kr/protos) (:proto dvs)))
      (is (= (:mgpk kr/serials) (:kind dvs)))
      (is (= kr/vrsn (:version dvs)))
      (is (= 65 (:size dvs)))))
  (testing "versify ACDC default version CBOR size 0"
    (let [vs (kr/versify {:proto (:acdc kr/protos) :version kr/vrsn :kind (:cbor kr/serials) :size 0})
          dvs (kr/deversify vs)]
      (is (= "ACDC10CBOR000000_" vs))
      (is (= (count vs) kr/verfullsize))
      (is (= (:acdc kr/protos) (:proto dvs)))
      (is (= (:cbor kr/serials) (:kind dvs)))
      (is (= kr/vrsn (:version dvs)))
      (is (= 0 (:size dvs)))))
  (testing "versify ACDC default version CBOR size 0"
    (let [vs (kr/versify {:proto (:acdc kr/protos) :version kr/vrsn :kind (:cbor kr/serials) :size 65})
          dvs (kr/deversify vs)]
      (is (= "ACDC10CBOR000041_" vs))
      (is (= (count vs) kr/verfullsize))
      (is (= (:acdc kr/protos) (:proto dvs)))
      (is (= (:cbor kr/serials) (:kind dvs)))
      (is (= kr/vrsn (:version dvs)))
      (is (= 65 (:size dvs)))))
  )