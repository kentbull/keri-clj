(ns keri.coring-test
  (:require [clojure.test :refer :all]
            [keri.coring :as kc]
            [keri.helping :as hlp])
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
  (testing "-BAC (0 pad bits) converts to bytes and back"
    (let [test-str "-BAC"
          bytes (kc/code-b64-to-b2 test-str)
          chars (kc/code-b2-to-b64 bytes 4)
          ; Convert bytes to BigInteger for octal comparison
          int-from-bytes (BigInteger. 1 ^"[B" (byte-array bytes))
          ; Shift
          shifted-int (.shiftRight int-from-bytes (* 2 (mod (count test-str) 4)))
          nabbed-bytes (kc/nab-sextets bytes 4)             ; first four characters
          ]
      (is (= 3 (count bytes)))
      (is (= test-str chars))
      (is (= 076010002 int-from-bytes))
      (is (= 076010002 shifted-int))
      (is (= bytes nabbed-bytes))))
  (testing "-BA (6 pad bits) converts to bytes and back and padding bits, with a remainder of 1, are handled correctly"
    (let [test-str "-BA"
          bytes (kc/code-b64-to-b2 test-str)
          chars (kc/code-b2-to-b64 bytes 3)
          int-from-bytes (BigInteger. 1 ^"[B" (byte-array bytes))
          shifted-int (.shiftRight int-from-bytes (* 2 (mod (count test-str) 4)))
          nabbed-bytes (kc/nab-sextets bytes 3)]
      (is (= 3 (count bytes)))
      (is (= test-str chars))
      (is (= 076010000 int-from-bytes))
      (is (= 0760100 shifted-int))
      (is (= bytes nabbed-bytes))))
  (testing "-B (4 pad bits) converts to bytes and back and padding bits, with a remainder of 1, are handled correctly"
    (let [test-str "-B"
          bytes (kc/code-b64-to-b2 test-str)
          chars (kc/code-b2-to-b64 bytes 2)
          int-from-bytes (BigInteger. 1 ^"[B" (byte-array bytes))
          shifted-int (.shiftRight int-from-bytes (* 2 (mod (count test-str) 4)))
          nabbed-bytes (kc/nab-sextets bytes 2)]
      (is (= 2 (count bytes)))
      (is (= test-str chars))
      (is (= 0174020 int-from-bytes))
      (is (= 07601 shifted-int))
      (is (= bytes nabbed-bytes))))
  (testing "-BACD (3 pad byte) converts to bytes and back and padding bits, with a remainder of 1, are handled correctly"
    (let [test-str "-BACD"
          bytes (kc/code-b64-to-b2 test-str)
          chars (kc/code-b2-to-b64 bytes 5)
          int-from-bytes (BigInteger. 1 ^"[B" (byte-array bytes))
          shifted-int (.shiftRight int-from-bytes (* 2 (mod (count test-str) 4)))
          nabbed-bytes (kc/nab-sextets bytes 5)]
      (is (= 4 (count bytes)))
      (is (= test-str chars))
      (is (= 037004001014 int-from-bytes))
      (is (= 07601000203 shifted-int))
      (is (= bytes nabbed-bytes))))
  (testing "- (2 pad bits) converts to bytes and back and padding bits, with a remainder of 1, are handled correctly"
    (let [test-str "-"
          bytes (kc/code-b64-to-b2 test-str)
          chars (kc/code-b2-to-b64 bytes 1)
          int-from-bytes (BigInteger. 1 ^"[B" (byte-array bytes))
          shifted-int (.shiftRight int-from-bytes (* 2 (mod (count test-str) 4)))
          nabbed-bytes (kc/nab-sextets bytes 1)]
      (is (= 1 (count bytes)))
      (is (= test-str chars))
      (is (= 0370 int-from-bytes))
      (is (= 076 shifted-int))
      (is (= bytes nabbed-bytes))))
  ; Test with a longer string
  (testing "CIOdpoavidvODJFPDKasdkfpSDPLKFJ converts to bytes and back"
    (let [test-str "CIOdpoavidvODJFPDKasdkfpSDPLKFJ"
          bytes (kc/code-b64-to-b2 test-str)
          chars (kc/code-b2-to-b64 bytes 31)]
      (is (= 24 (count bytes)))
      (is (= test-str chars))))
  )

(deftest matter-tests
  "Test that the setup code and usages of the IMatter protocol are correct"
  (testing "Bards set membership check works"
    (let [s "BAC"
          bytes (kc/code-b64-to-b2 s)
          nabbed (kc/nab-sextets bytes 1)]
      (is (contains? kc/bards nabbed))))

  (testing "Matter.Hards and Matter.Bards consistency"
    ; kc/bards maps bytes of sextet of decoded first character of code with hard size of code
    ; verify equivalents of items for kc/sizes and kc/bards
    (doseq [entry kc/hards]
      (let [skey (key (first entry))
            sval (val (first entry))
            ckey (kc/code-b64-to-b2 skey)]
        (is (= (get kc/bards ckey) sval))))))

