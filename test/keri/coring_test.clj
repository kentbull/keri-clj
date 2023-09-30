(ns keri.coring-test
  (:require [clojure.test :refer :all]
            [keri.coring :as kc])
  (:import (java.util Arrays)))

;(declare thrown? thrown-with-msg?)

(deftest b64-conversions
  "Base64URLSafe conversions to integers and back"
  ; blanks
  (testing "Empty string throws error when i is nil"
    (let [result (try
                   (kc/b64-to-int "")
                   (catch IllegalArgumentException e
                     e))]
      (is (instance? IllegalArgumentException result))))
  (testing "nil string throws error when i is nil"
    (let [result (try
                   (kc/b64-to-int nil)
                   (catch IllegalArgumentException e
                     e))]
      (is (instance? IllegalArgumentException result))))
  (testing "zeroth char and zero padding returns blank"
    (let [cs (kc/int-to-b64 0 0)]
      (is (= "" cs))))
  (testing "Nil char and padding zero returns blank"
    (let [cs (kc/int-to-b64 nil 0)]
      (is (= "" cs))))
  ; Zero, or character "A"
  (testing "zero returns A"
    (let [cs (kc/int-to-b64 0)
          i (kc/b64-to-int cs)]
      (is (= "A" cs))
      (is (= i 0))))
  (testing "bytes for 0 returned"
    (let [cs (kc/int-to-b64b 0)
          i (kc/b64-to-int cs)]
      (is (Arrays/equals ^"[B" (.getBytes "A" "UTF-8") ^"[B" cs))
      (is (= i 0))))
  ; 27, or "b"
  (testing "lowercase 'b' returned for 27"
    (let [cs (kc/int-to-b64 27)
          i (kc/b64-to-int cs)]
      (is (= "b" cs))
      (is (= i 27))))
  (testing "bytes lowercase 'b' returned for 27"
    (let [cs (kc/int-to-b64b 27)
          i (kc/b64-to-int cs)]
      (is (Arrays/equals ^"[B" (.getBytes "b" "UTF-8") ^"[B" cs))
      (is (= i 27))))
  ; 27 with one byte of padding, "Ab"
  (testing "lowercase 'b' with one byte of padding returned for 27 and length 2"
    (let [cs (kc/int-to-b64 27 2)
          i (kc/b64-to-int cs)]
      (is (= "Ab" cs))
      (is (= i 27))))
  (testing "bytes lowercase 'b' returned for 27"
    (let [cs (kc/int-to-b64b 27 2)
          i (kc/b64-to-int cs)]
      (is (Arrays/equals ^"[B" (.getBytes "Ab" "UTF-8") ^"[B" cs))
      (is (= i 27))))
  ; 80 = BQ
  (testing "BQ equals 80"
    (let [cs (kc/int-to-b64 80)
          i (kc/b64-to-int cs)]
      (is (= "BQ" cs))
      (is (= i 80))))
  (testing "bytes BQ equals 80"
    (let [cs (kc/int-to-b64b 80)
          i (kc/b64-to-int cs)]
      (is (Arrays/equals ^"[B" (.getBytes "BQ" "UTF-8") ^"[B" cs))
      (is (= i 80))))
  ; 4095 = __
  (testing "__ equals 4095"
    (let [cs (kc/int-to-b64 4095)
          i (kc/b64-to-int cs)]
      (is (= "__" cs))
      (is (= i 4095))))
  (testing "bytes __ equals 4095"
    (let [cs (kc/int-to-b64b 4095)
          i (kc/b64-to-int cs)]
      (is (Arrays/equals ^"[B" (.getBytes "__" "UTF-8") ^"[B" cs))
      (is (= i 4095))))
  ; 4096 = BAA
  (testing "BAA equals 4096"
    (let [cs (kc/int-to-b64 4096)
          i (kc/b64-to-int cs)]
      (is (= "BAA" cs))
      (is (= i 4096))))
  (testing "bytes BAA equals 4096"
    (let [cs (kc/int-to-b64b 4096)
          i (kc/b64-to-int cs)]
      (is (Arrays/equals ^"[B" (.getBytes "BAA" "UTF-8") ^"[B" cs))
      (is (= i 4096))))
  ; Bd7 = 6011
  (testing "Bd7 equals 6011"
    (let [cs (kc/int-to-b64 6011)
          i (kc/b64-to-int cs)]
      (is (= "Bd7" cs))
      (is (= i 6011))))
  (testing "bytes Bd7 equals 6011"
    (let [cs (kc/int-to-b64b 6011)
          i (kc/b64-to-int cs)]
      (is (Arrays/equals ^"[B" (.getBytes "Bd7" "UTF-8") ^"[B" cs))
      (is (= i 6011))))
  (testing "Bytearray literal works"
    (let [cs (byte-array (map byte "Bd7"))
          i (kc/b64-to-int cs)]
      (is (Arrays/equals ^"[B" (.getBytes "Bd7" "UTF-8") ^"[B" cs))
      (is (= i 6011))))
  ; Test with a longer string.
  (testing "Longer B64 string works to convert back and forth"
    (let [test-str "CIOdpoavidvODJFPDKasdkfpSDPLKFJ"
          num (kc/b64-to-int test-str)
          b64 (kc/int-to-b64 num)]
      (is (= num (BigInteger. "3261964383182375661823200533049427876121305900276031817")))
      (is (= test-str b64))))
  ;
  ; Converting Base64URLSafe to base 2 binary
  ;
  (testing "-BAC converts to bytes and back"
    (let [test-str "-BAC"
          bytes (kc/code-b64-to-b2 test-str)
          chars (kc/code-b2-to-b64 bytes 4)]
      (is (= 3 (count bytes)))
      (is (= test-str chars))))
  (testing "CIOdpoavidvODJFPDKasdkfpSDPLKFJ converts to bytes and back"
    (let [test-str "CIOdpoavidvODJFPDKasdkfpSDPLKFJ"
          bytes (kc/code-b64-to-b2 test-str)
          chars (kc/code-b2-to-b64 bytes 31)]
      (is (= 24 (count bytes)))
      (is (= test-str chars))))
  )

(deftest b64-to-b2-conversions
  "Base64URLSafe to Base 2 (binary) and back"
  (testing
    (is (= 0 1))))
