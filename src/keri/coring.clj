(ns keri.coring
  "Core constants, records, interfaces, and functions used throughout"
  (:require
    [clojure.set :refer [union]]
    [clojure.string :as str]
    [keri.kering :as kr]
    [keri.helping :as hlp]))


; TODO Consider replacing this set of labels with Record types, maybe even clojure.spec definitions, to validate
;      object structure. Is the set of labels being used only to enforce object structure? Maybe records are best.
(def labels
  "Valid property names for each type of message (18 event types, 13 in use)"
  {:icp kr/icp-labels :rot kr/rot-labels :ixn kr/ixn-labels :dip kr/dip-labels :drt kr/drt-labels :rct []
   :ksn kr/ksn-labels :qry [] :rpy kr/rpy-labels :exn [] :pro [] :bar [] :vcp kr/vcp-labels :vrt kr/vrt-labels
   :iss kr/iss-labels :rev kr/rev-labels :bis kr/bis-labels :brv kr/brv-labels})

(def ecdsa-256r1-seedbytes 32)
(def ecdsa-256k1-seedbytes 32)

(def vstrings
  "Version strings for JSON, CBOR, MGPK with KERI and ACDC"
  {:json (kr/versify {:proto (:keri kr/protos) :version kr/vrsn :kind (:json kr/serials) :size 0})
   :cbor (kr/versify {:proto (:keri kr/protos) :version kr/vrsn :kind (:cbor kr/serials) :size 0})
   :mgpk (kr/versify {:proto (:keri kr/protos) :version kr/vrsn :kind (:mgpk kr/serials) :size 0})})



(def tierage
  "Secret derivation security tiers for keypair generation"
  {:low  "low"
   :med  "med"
   :high "high"})

(def matter-codex
  "MatterCodex is codex code (stable) part of all matter derivation codes used in CESR.
   Only provide defined codes.
   Undefined are left out so that inclusion(exclusion) via 'in' operator works.
  "
  {
   :Ed25519_Seed       "A"                                  ; Ed25519 256 bit random seed for private key
   :Ed25519N           "B"                                  ; Ed25519 verification key non-transferable, basic derivation.
   :X25519             "C"                                  ; X25519 public encryption key, converted from Ed25519 or Ed25519N.
   :Ed25519            "D"                                  ; Ed25519 verification key basic derivation
   :Blake3_256         "E"                                  ; Blake3 256 bit digest self-addressing derivation.
   :Blake2b_256        "F"                                  ; Blake2b 256 bit digest self-addressing derivation.
   :Blake2s_256        "G"                                  ; Blake2s 256 bit digest self-addressing derivation.
   :SHA3_256           "H"                                  ; SHA3 256 bit digest self-addressing derivation.
   :SHA2_256           "I"                                  ; SHA2 256 bit digest self-addressing derivation.
   :ECDSA_256k1_Seed   "J"                                  ; ECDSA secp256k1 256 bit random Seed for private key
   :Ed448_Seed         "K"                                  ; Ed448 448 bit random Seed for private key
   :X448               "L"                                  ; X448 public encryption key, converted from Ed448
   :Short              "M"                                  ; Short 2 byte b2 number
   :Big                "N"                                  ; Big 8 byte b2 number
   :X25519_Private     "O"                                  ; X25519 private decryption key converted from Ed25519
   :X25519_Cipher_Seed "P"                                  ; X25519 124 char b64 Cipher of 44 char qb64 Seed
   :ECDSA_256r1_Seed   "Q"                                  ; ECDSA secp256r1 256 bit random Seed for private key
   :Salt_128           "0A"                                 ; 128 bit random salt or 128 bit number (see Huge)
   :Ed25519_Sig        "0B"                                 ; Ed25519 signature.
   :ECDSA_256k1_Sig    "0C"                                 ; ECDSA secp256k1 signature.
   :Blake3_512         "0D"                                 ; Blake3 512 bit digest self-addressing derivation.
   :Blake2b_512        "0E"                                 ; Blake2b 512 bit digest self-addressing derivation.
   :SHA3_512           "0F"                                 ; SHA3 512 bit digest self-addressing derivation.
   :SHA2_512           "0G"                                 ; SHA2 512 bit digest self-addressing derivation.
   :Long               "0H"                                 ; Long 4 byte b2 number
   :ECDSA_256r1_Sig    "0I"                                 ; ECDSA secp256r1 signature.
   :ECDSA_256k1N       "1AAA"                               ; ECDSA secp256k1 verification key non-transferable, basic derivation.
   :ECDSA_256k1        "1AAB"                               ; ECDSA public verification or encryption key, basic derivation
   :Ed448N             "1AAC"                               ; Ed448 non-transferable prefix public signing verification key. Basic derivation.
   :Ed448              "1AAD"                               ; Ed448 public signing verification key. Basic derivation.
   :Ed448_Sig          "1AAE"                               ; Ed448 signature. Self-signing derivation.
   :Tern               "1AAF"                               ; 3 byte b2 number or 4 char B64 str.
   :DateTime           "1AAG"                               ; Base64 custom encoded 32 char ISO-8601 DateTime
   :X25519_Cipher_Salt "1AAH"                               ; X25519 100 char b64 Cipher of 24 char qb64 Salt
   :ECDSA_256r1N       "1AAI"                               ; ECDSA secp256r1 verification key non-transferable, basic derivation.
   :ECDSA_256r1        "1AAJ"                               ; ECDSA secp256r1 verification or encryption key, basic derivation
   :TBD1               "2AAA"                               ; Testing purposes only fixed with lead size 1
   :TBD2               "3AAA"                               ; Testing purposes only of fixed with lead size 2
   :StrB64_L0          "4A"                                 ; String Base64 Only Lead Size 0
   :StrB64_L1          "5A"                                 ; String Base64 Only Lead Size 1
   :StrB64_L2          "6A"                                 ; String Base64 Only Lead Size 2
   :StrB64_Big_L0      "7AAA"                               ; String Base64 Only Big Lead Size 0
   :StrB64_Big_L1      "8AAA"                               ; String Base64 Only Big Lead Size 1
   :StrB64_Big_L2      "9AAA"                               ; String Base64 Only Big Lead Size 2
   :Bytes_L0           "4B"                                 ; Byte String Leader Size 0
   :Bytes_L1           "5B"                                 ; Byte String Leader Size 1
   :Bytes_L2           "6B"                                 ; ByteString Leader Size 2
   :Bytes_Big_L0       "7AAB"                               ; Byte String Big Leader Size 0
   :Bytes_Big_L1       "8AAB"                               ; Byte String Big Leader Size 1
   :Bytes_Big_L2       "9AAB"                               ; Byte String Big Leader Size 2
   })

(def number-mappings
  "Number encoding sizes for CESR"
  #{{"0" 2} {"1" 4} {"2" 4} {"3" 4} {"4" 2}
    {"5" 2} {"6" 2} {"7" 4} {"8" 4} {"9" 4}})

(defn char-to-str [num]
  (str (char num)))
(def hards
  "Mappings of alphabet and numeric digits to encoding code sizes for the size of the hard part of a code in a
  CESR-encoded data type.

  Hards table maps from bytes Base64 first code char to int of hard size, hs, (stable) of code.
  The soft size, ss, (unstable) is always 0 for Matter unless fs is nil which allows for variable size multiple of 4,
  i.e. not (hs + ss) % 4."
  (let [uppercase-mappings (into #{} (map (fn [c] {c 1}) (map char-to-str (range 65 (+ 65 26)))))
        lowercase-mappings (into #{} (map (fn [c] {c 1}) (map char-to-str (range 97 (+ 97 26)))))]
    (union uppercase-mappings lowercase-mappings number-mappings)))

(def sizes
  "The sizes table maps from value of Hards hard size (hs) chars of code to an entry of {:hs num :ss num :fs num :ls num} where
  hs is hard size
  ss is soft size
  fs is full size
  ls is lead size
  Soft size, ss, should always be 0 for Matter unless fs is nil which allows for variable size multiple of 4,
  i.e. not (hs + ss) % 4.
  "
  {"A"    {:hs 1, :ss 0, :fs 44, :ls 0},
   "B"    {:hs 1, :ss 0, :fs 44, :ls 0},
   "C"    {:hs 1, :ss 0, :fs 44, :ls 0},
   "D"    {:hs 1, :ss 0, :fs 44, :ls 0},
   "E"    {:hs 1, :ss 0, :fs 44, :ls 0},
   "F"    {:hs 1, :ss 0, :fs 44, :ls 0},
   "G"    {:hs 1, :ss 0, :fs 44, :ls 0},
   "H"    {:hs 1, :ss 0, :fs 44, :ls 0},
   "I"    {:hs 1, :ss 0, :fs 44, :ls 0},
   "J"    {:hs 1, :ss 0, :fs 44, :ls 0},
   "K"    {:hs 1, :ss 0, :fs 76, :ls 0},
   "L"    {:hs 1, :ss 0, :fs 76, :ls 0},
   "M"    {:hs 1, :ss 0, :fs 4, :ls 0},
   "N"    {:hs 1, :ss 0, :fs 12, :ls 0},
   "O"    {:hs 1, :ss 0, :fs 44, :ls 0},
   "P"    {:hs 1, :ss 0, :fs 124, :ls 0},
   "Q"    {:hs 1, :ss 0, :fs 44, :ls 0},
   "0A"   {:hs 2, :ss 0, :fs 24, :ls 0},
   "0B"   {:hs 2, :ss 0, :fs 88, :ls 0},
   "0C"   {:hs 2, :ss 0, :fs 88, :ls 0},
   "0D"   {:hs 2, :ss 0, :fs 88, :ls 0},
   "0E"   {:hs 2, :ss 0, :fs 88, :ls 0},
   "0F"   {:hs 2, :ss 0, :fs 88, :ls 0},
   "0G"   {:hs 2, :ss 0, :fs 88, :ls 0},
   "0H"   {:hs 2, :ss 0, :fs 8, :ls 0},
   "0I"   {:hs 2, :ss 0, :fs 88, :ls 0},
   "1AAA" {:hs 4, :ss 0, :fs 48, :ls 0},
   "1AAB" {:hs 4, :ss 0, :fs 48, :ls 0},
   "1AAC" {:hs 4, :ss 0, :fs 80, :ls 0},
   "1AAD" {:hs 4, :ss 0, :fs 80, :ls 0},
   "1AAE" {:hs 4, :ss 0, :fs 56, :ls 0},
   "1AAF" {:hs 4, :ss 0, :fs 8, :ls 0},
   "1AAG" {:hs 4, :ss 0, :fs 36, :ls 0},
   "1AAH" {:hs 4, :ss 0, :fs 100, :ls 0},
   "1AAI" {:hs 4, :ss 0, :fs 48, :ls 0},
   "1AAJ" {:hs 4, :ss 0, :fs 48, :ls 0},
   "2AAA" {:hs 4, :ss 0, :fs 8, :ls 1},
   "3AAA" {:hs 4, :ss 0, :fs 8, :ls 2},
   "4A"   {:hs 2, :ss 2, :fs nil, :ls 0},
   "5A"   {:hs 2, :ss 2, :fs nil, :ls 1},
   "6A"   {:hs 2, :ss 2, :fs nil, :ls 2},
   "7AAA" {:hs 4, :ss 4, :fs nil, :ls 0},
   "8AAA" {:hs 4, :ss 4, :fs nil, :ls 1},
   "9AAA" {:hs 4, :ss 4, :fs nil, :ls 2},
   "4B"   {:hs 2, :ss 2, :fs nil, :ls 0},
   "5B"   {:hs 2, :ss 2, :fs nil, :ls 1},
   "6B"   {:hs 2, :ss 2, :fs nil, :ls 2},
   "7AAB" {:hs 4, :ss 4, :fs nil, :ls 0},
   "8AAB" {:hs 4, :ss 4, :fs nil, :ls 1},
   "9AAB" {:hs 4, :ss 4, :fs nil, :ls 2}
   })

;
; Base64 Utilities for Base64URLSafe characters
; RFC: https://datatracker.ietf.org/doc/html/rfc4648#section-5
;

; Mappings between Base64 encode index and decode characters
(def b64char-by-idx-uppercase
  "A set of the ASCII upper case characters indexed by their Base64URLSafe encoding code"
  (into {} (for [index-char (map-indexed vector (map str (map char (range 65 91))))]
             (let [index (first index-char)
                   char (second index-char)]
               {index char}))))

(def b64char-by-idx-lowercase
  "A set of the ASCII lower case characters indexed by their Base64URLSafe encoding code"
  (into {} (for [index-char (map-indexed vector (map str (map char (range 97 123))))]
             (let [index (first index-char)
                   char (second index-char)]
               {(+ 26 index) char}))))

(def b64char-by-idx-numbers
  "A set of the ASCII numbers indexed by their Base64URLSafe encoding code"
  (into {} (for [index-char (map-indexed vector (map str (map char (range 48 58))))]
             (let [index (first index-char)
                   char (second index-char)]
               {(+ 52 index) char}))))

(def b64-end-chars
  "The last two Base64URLSafe characters"
  {62 "-", 63 "_"})

(def b64char-by-idx
  "Map of {index char} Base64URLSafe characters indexed by character index to character"
  (merge b64-end-chars
    b64char-by-idx-uppercase
    b64char-by-idx-lowercase
    b64char-by-idx-numbers))

(defn reverse-map [m]
  "Swap keys and values"
  (map (fn [[k v]] [v k]) m))

(def b64char-by-char
  "Map of {char index} Base64URLSafe characters indexed by character mapped to character index"
  (into {} (reverse-map b64char-by-idx)))

(def b64-chars
  "All Base64URLSafe characters"
  (vals b64char-by-idx))

(def base64urlsafe-pattern
  "Regex pattern matching any Base64URLSafe character"
  #"^[A-Za-z0-9_-]+$")

(defn reb64 [s]
  "Returns a Base64URLSafe regular expression matcher"
  (re-matcher base64urlsafe-pattern s))

(defn int-to-b64
  "Convert an integer to the equivalent Base64URLSafe characters represented as a string"
  ([i]
   (int-to-b64 i 1))
  ([i l]
   (if (and (nil? i) (zero? l))
     ""
     (let [chars (if (not (zero? l))
                   (loop [d '()
                          num i]
                     ; Wraparound division with quotient
                     (let [character (b64char-by-idx (mod num 64))
                           updated (cons character d)]
                       (if (= 0 (quot num 64))
                         updated
                         (recur
                           updated
                           (quot num 64))
                         )))
                   ())
           padding-count (- l (count chars))
           padding (if (> padding-count 0)
                     (str/join (repeat padding-count "A"))
                     "")]
       (apply str padding chars)))))

(defn int-to-b64b
  "Convert an integer to the equivalent Base64URLSafe characters represented as bytes"
  ([i]
   (int-to-b64b i 1))
  ([i l]
   (.getBytes (int-to-b64 i l) "UTF-8")))

(defn b64-to-int [s]
  "Converts a Base64URLSafe set of characters into it's integer equivalent, represented as a BigInt since primitive JVM
  integer types don't have enough space to represent large b64 values.
  Accepts a byte array or a string."
  (let [s (if (instance? (Class/forName "[B") s)            ; "[B" is the byte array class name in the JVM
            (String. ^"[B" s "UTF-8")                       ; "[B" byte array hint to disambiguate function signature
            s)]
    (if (empty? s)
      (throw (IllegalArgumentException. "Empty string, conversion undefined."))
      (reduce (fn [i [e c]]
                (let [char (str c)
                      b64 (b64char-by-char char)
                      shifted (.shiftLeft (BigInteger/valueOf b64) (* e 6))
                      ]
                  (.or i shifted)))
        (BigInteger/valueOf 0)
        (map vector (range (count s)) (reverse s))))
    ))

(defn strip-first-pad-byte [bytearray]
  (if (and (> (count bytearray) 1) (zero? (first bytearray)))
    (rest bytearray)
    bytearray))

(defn code-b64-to-b2 [s]
  "Decode to binary: returns a lazy sequence of the conversion (decoding) of Base64URLSafe chars to Base2 bytes.

  The number of total bytes returned equals the minimum number of octets sufficient
  to hold the total converted concatenated sextets from s, with one sextet per each
  Base64 decoded char of s.
  Assumes no pad chars in s.
  Sextets are left aligned with pad bits in last (rightmost) byte.
  This is useful for decoding as bytes, code characters from the front of a Base64
  encoded string of characters."
  (let [n (hlp/sceil (/ (* (count s) 3) 4))                 ; Minimum number of octets to hold all sextets
        bytes (-> s
                (b64-to-int)                                ; Returns a BigInteger
                (.shiftLeft (* 2 (mod (count s) 4)))        ; Adds 2 bits of right zero padding for each sextet
                (.toByteArray)                              ; Defaults to big-endian
                (strip-first-pad-byte)                      ; Needed since toByteArray prepends zero byte ensuring byte
                                                            ; array's most significant byte is zero if the highest bit
                                                            ; in the next byte is set
                )]
    (concat (repeat (- n (count bytes)) (byte 0)) bytes)    ; Needed to ensure correct length like int.toBytes(n, 'big') in Python
    ))

(defn bytes-to-big-int [b]
  "Converts a byte array to a number represented as a BigInteger."
  (BigInteger. 1 ^"[B" b))

(defn code-b2-to-b64 [b l]
  "Encode to Base64URLSafe: returns conversion (encoding) of l Base2 sextets from front of b (a byte array) to
   Base64URLSafe characters. One char for each of l sextets from front (left) of b.
   This is useful for encoding as code characters, sextets from the front of a Base2 byte array (byte string).
   Must provide l because of ambiguity between l=3 and l=4. Both require 3 bytes in b.
  "
  (let [b-bytes (if (instance? String b)
            (.getBytes b "UTF-8")
            b)
        n (hlp/sceil (/ (* l 3) 4))]
    (if (> n (count b-bytes))
      (throw (IllegalArgumentException. (str "Not enough bytes in " b " to convert " l " sextets."))))
    (let [i (bytes-to-big-int (byte-array (take n b-bytes)))
          tbs (* 2 (mod l 4))                               ; Trailing bit size in bits - check if prepad bits are zero
          shifted-i (.shiftRight i tbs)                 ; right shift out trailing bits to make right aligned
          ]
      (int-to-b64 shifted-i l)
      )))

(defn nab-sextets [b l]
  "Return first l sextets from front (left) of b as bytes (byte string).
  Length of bytes returned is minimum sufficient to hold all l sextets.
  Last byte returned is right bit padded with zeros.
  b is bytes or str"
  (let [b-bytes (if (instance? String b)
                  (.getBytes b "UTF-8")
                  b)
        n (hlp/sceil (/ (* l 3) 4))]
    (if (> n (count b-bytes))
      (throw (IllegalArgumentException. (str "Not enough bytes in " b " to nab " l " sextets."))))
    (let [p (* 2 (mod l 4))                                 ; number of pad bits?
          i (bytes-to-big-int (byte-array (take n b-bytes)))
          sextet-bytes (-> i
                         (.shiftRight p)                    ; strip off last bits
                         (.shiftLeft p)                     ; pad with empty bits
                         (.toByteArray)                     ; Defaults to big-endian
                         (strip-first-pad-byte)             ; Needed since toByteArray prepends zero byte ensuring byte
                                                            ; array's most significant byte is zero if the highest bit
                                                            ; in the next byte is set
                         )]
      (concat (repeat (- n (count sextet-bytes)) (byte 0)) sextet-bytes) ; Needed to ensure correct length like int.toBytes(n, 'big') in Python
      )))

(defn bytes-to-vector [b]
  (vec (map byte b)))


(def bards
  "Binary sextets of hard size (hs) for CESR encoding sizes.
  Used for `bexfil`"
  (into {}
    (map (fn [entry]
           (let [c (key (first entry))
                 hs (val (first entry))]
             [(bytes-to-vector (code-b64-to-b2 c)) hs]))
      hards)))



(defprotocol IMatter
  "Cryptographic primitive material base class. Uses a fully qualified encoding for non-indexed primitives.

  Implementations")