(ns keri.coring
  "Core constants, records, interfaces, and functions used throughout"
  (:require [clojure.string :as str]))

(def falsey [false 0 nil "?0" "no" "false" "False" "off"])
(def truthy [true 1 "?1" "yes" "true" "True" "on"])

;; Serialization Kinds
(def serials
  {:json "JSON"
   :mgpk "MGPK"
   :cbor "CBOR"})

;; Protocol Types
(def protos
  {:keri "KERI"
   :crel "CREL"
   :acdc "ACDC"})

;; Version
(def version {:major 1 :minor 0})                           ; KERI Protocol Version
(def vrsn_1_0 {:major 1 :minor 0})                          ; KERI Protocol Version Specific
(def vrsn_1_1 {:major 1 :minor 1})                          ; KERI Protocol Version Specific

;; Constants
(def verrawsize 6)                                          ; hex characters in raw serialization size in version string
; "{:0{}x}".format(300, 6)  ; make num char in hex a variable
; '00012c'
(def verfmt "{}{:x}{:x}{}{:0{}x}_")                         ; version format string
(def verfullsize 17)                                        ;  number of characters in full version string

(def verex #"(?<proto>[A-Z]{4})(?<major>[0-9a-f])(?<minor>[0-9a-f])(?<kind>[A-Z]{4})(?<size>[0-9a-f]{6})_")

(defn rever [s]
  (re-matcher verex s))



(def Ilks
  "KERI protocol packet (message) types"
  {:icp "icp"     ; Inception                                                   - incept
   :rot "rot"     ; Rotation                                                    - rotate
   :ixn "ixn"     ; Interaction                                                 - interact
   :dip "dip"     ; Delegated Inception                                         - delcept
   :drt "drt"     ; Delegated Rotation                                          - deltate
   :rct "rct"     ; Receipt
   :ksn "ksn"     ; Key State Notice                                            - state
   :qry "qry"     ; Query
   :rpy "rpy"     ; Reply
   :exn "exn"     ; Exchange
   :pro "pro"     ; unimplemented
   :bar "bar"     ; unimplemented
   :exp "exp"     ; sealed data exposition, expose - not used
   :vcp "vcp"     ; Verifiable Data Registry inception                          - vdr incept
   :vrt "vrt"     ; Verifiable Data Registry rotation                           - vdr rotate
   :iss "iss"     ; Verifiable Credential Issue                                 - vc issue
   :rev "rev"     ; Verifiable Credential Revocation                            - vc revoke
   :bis "bis"     ; Registry-backed transaction event log credential issuance   - backed vc issue
   :brv "brv"     ; Registry-backed transaction event log credential revocation - backed vc revoke
   })

(def icp-labels
  "Inception event labels - valid fields for an inception event"
  ["v" "t" "d" "i" "s" "kt" "k" "nt" "n" "bt" "b" "c" "a"])
(def dip-labels
  ["v" "d" "i" "s" "t" "kt" "k" "nt" "n" "bt" "b" "c" "a" "di"])
(def rot-labels
  ["v" "d" "i" "s" "t" "p" "kt" "k" "nt" "n" "bt" "br" "ba" "a"])
(def drt-labels
  ["v" "d" "i" "s" "t" "p" "kt" "k" "nt" "n" "bt" "br" "ba" "a"])
(def ixn-labels
  ["v" "d" "i" "s" "t" "p" "a"])
(def ksn-labels
  ["v" "d" "i" "s" "p" "d" "f" "dt" "et" "kt" "k" "nt" "n" "bt" "b" "c" "ee" "di"])
(def rpy-labels
  ["v" "d" "t" "d" "dt" "r" "a"])
(def vcp-labels
  ["v" "d" "i" "s" "t" "bt" "b" "c"])
(def vrt-labels
  ["v" "d" "i" "s" "t" "p" "bt" "b" "ba" "br"])
(def iss-labels
  ["v" "i" "s" "t" "ri" "dt"])
(def bis-labels
  ["v" "i" "s" "t" "ra" "dt"])
(def rev-labels
  ["v" "i" "s" "t" "p" "dt"])
(def brv-labels
  ["v" "i" "s" "t" "ra" "p" "dt"])
(def tsn-labels
  ["v" "i" "s" "d" "ii" "a" "et" "bt" "b" "c" "br" "ba"])
(def cred-tsn-labels
  ["v" "i" "s" "d" "ri" "a" "ra"])

(def tierage
  "Secret derivation security tier"
  {:low "low"
   :med "med"
   :high "high"})

(def matter-codex
  "MatterCodex is codex code (stable) part of all matter derivation codes.
   Only provide defined codes.
   Undefined are left out so that inclusion(exclusion) via 'in' operator works.
  "
  {
   :Ed25519_Seed          "A"     ; Ed25519 256 bit random seed for private key
   :Ed25519N              "B"     ; Ed25519 verification key non-transferable, basic derivation.
   :X25519                "C"     ; X25519 public encryption key, converted from Ed25519 or Ed25519N.
   :Ed25519               "D"     ; Ed25519 verification key basic derivation
   :Blake3_256            "E"     ; Blake3 256 bit digest self-addressing derivation.
   :Blake2b_256           "F"     ; Blake2b 256 bit digest self-addressing derivation.
   :Blake2s_256           "G"     ; Blake2s 256 bit digest self-addressing derivation.
   :SHA3_256              "H"     ; SHA3 256 bit digest self-addressing derivation.
   :SHA2_256              "I"     ; SHA2 256 bit digest self-addressing derivation.
   :ECDSA_256k1_Seed      "J"     ; ECDSA secp256k1 256 bit random Seed for private key
   :Ed448_Seed            "K"     ; Ed448 448 bit random Seed for private key
   :X448                  "L"     ; X448 public encryption key, converted from Ed448
   :Short                 "M"     ; Short 2 byte b2 number
   :Big                   "N"     ; Big 8 byte b2 number
   :X25519_Private        "O"     ; X25519 private decryption key converted from Ed25519
   :X25519_Cipher_Seed    "P"     ; X25519 124 char b64 Cipher of 44 char qb64 Seed
   :ECDSA_256r1_Seed      "Q"     ; ECDSA secp256r1 256 bit random Seed for private key
   :Salt_128              "0A"    ; 128 bit random salt or 128 bit number (see Huge)
   :Ed25519_Sig           "0B"    ; Ed25519 signature.
   :ECDSA_256k1_Sig       "0C"    ; ECDSA secp256k1 signature.
   :Blake3_512            "0D"    ; Blake3 512 bit digest self-addressing derivation.
   :Blake2b_512           "0E"    ; Blake2b 512 bit digest self-addressing derivation.
   :SHA3_512              "0F"    ; SHA3 512 bit digest self-addressing derivation.
   :SHA2_512              "0G"    ; SHA2 512 bit digest self-addressing derivation.
   :Long                  "0H"    ; Long 4 byte b2 number
   :ECDSA_256r1_Sig       "0I"    ; ECDSA secp256r1 signature.
   :ECDSA_256k1N          "1AAA"  ; ECDSA secp256k1 verification key non-transferable, basic derivation.
   :ECDSA_256k1           "1AAB"  ; ECDSA public verification or encryption key, basic derivation
   :Ed448N                "1AAC"  ; Ed448 non-transferable prefix public signing verification key. Basic derivation.
   :Ed448                 "1AAD"  ; Ed448 public signing verification key. Basic derivation.
   :Ed448_Sig             "1AAE"  ; Ed448 signature. Self-signing derivation.
   :Tern                  "1AAF"  ; 3 byte b2 number or 4 char B64 str.
   :DateTime              "1AAG"  ; Base64 custom encoded 32 char ISO-8601 DateTime
   :X25519_Cipher_Salt    "1AAH"  ; X25519 100 char b64 Cipher of 24 char qb64 Salt
   :ECDSA_256r1N          "1AAI"  ; ECDSA secp256r1 verification key non-transferable, basic derivation.
   :ECDSA_256r1           "1AAJ"  ; ECDSA secp256r1 verification or encryption key, basic derivation
   :TBD1                  "2AAA"  ; Testing purposes only fixed with lead size 1
   :TBD2                  "3AAA"  ; Testing purposes only of fixed with lead size 2
   :StrB64_L0             "4A"    ; String Base64 Only Lead Size 0
   :StrB64_L1             "5A"    ; String Base64 Only Lead Size 1
   :StrB64_L2             "6A"    ; String Base64 Only Lead Size 2
   :StrB64_Big_L0         "7AAA"  ; String Base64 Only Big Lead Size 0
   :StrB64_Big_L1         "8AAA"  ; String Base64 Only Big Lead Size 1
   :StrB64_Big_L2         "9AAA"  ; String Base64 Only Big Lead Size 2
   :Bytes_L0              "4B"    ; Byte String Leader Size 0
   :Bytes_L1              "5B"    ; Byte String Leader Size 1
   :Bytes_L2              "6B"    ; ByteString Leader Size 2
   :Bytes_Big_L0          "7AAB"  ; Byte String Big Leader Size 0
   :Bytes_Big_L1          "8AAB"  ; Byte String Big Leader Size 1
   :Bytes_Big_L2          "9AAB"  ; Byte String Big Leader Size 2
   })

(defprotocol IMatter
  "Cryptographic primitive material base class. Uses a fully qualified encoding for non-indexed primitives.

  Implementations")