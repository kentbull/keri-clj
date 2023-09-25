(ns keri.kering)

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
(defrecord versionage [major minor])
(def vrsn (->versionage 1 0))                           ; KERI Protocol Version

(def vrsn_1_0 (->versionage 1 0))                          ; KERI Protocol Version Specific
(def vrsn_1_1 (->versionage 1 1))                          ; KERI Protocol Version Specific

;; Constants
(def verrawsize 6)                                          ; hex characters in raw serialization size in version string
; "{:0{}x}".format(300, 6)  ; make num char in hex a variable
; '00012c'
(def verfmt "%s%x%x%s%06x_")                         ; version format string
(def verfullsize 17)                                        ;  number of characters in full version string

(def verex #"(?<proto>[A-Z]{4})(?<major>[0-9a-f])(?<minor>[0-9a-f])(?<kind>[A-Z]{4})(?<size>[0-9a-f]{6})_")

(defn rever [s]
  (re-matcher verex s))

(defn versify
  "Creates a protocol-specific version string

  Only supports ASCII characters for the size hexadecimal characters
  See the following StackOverflow post for full UTF-8 support: https://stackoverflow.com/a/10087740/2084253
  "
  ([]
   (versify {:proto   (:keri protos)
             :version vrsn
             :kind    (:json serials)
             :size    0}))
  ([{:keys [proto version kind size]}]
   (let [in-proto (if (nil? proto) (:keri protos) proto)
         in-version (if (nil? version) vrsn version)]
     (when (not (some #{proto} (vals protos)))
       (throw (ex-info (str "Invalid message identifier = " proto) {})))
     (when (not (some #{kind} (vals serials)))
       (throw (ex-info (str "Invalid serialization kind = " kind) {})))
     (format verfmt in-proto (:major in-version) (:minor in-version) kind size))))

(defn deversify
  "Returns parts of a version string (vs)"
  ([vs]
   (deversify vs nil))
  ([vs version]
   (let [matcher (rever vs)]
     (if (.find matcher)
       (let [[_ proto major minor kind size] (re-groups matcher)
             maj (Integer/parseInt major 16)
             min (Integer/parseInt minor 16)
             vers (->versionage maj min)
             size-decimal (Integer/parseInt size 16)]
            (when (not (some #{proto} (vals protos)))
              (throw (ex-info (str "Invalid message identifier = " proto) {})))
            (when (not (some #{kind} (vals serials)))
              (throw (ex-info (str "Invalid serialization kind = " kind) {})))
            (when (and (not (nil? version)) (not (= version vers)))
              (throw (ex-info (str "Expected version = " version ", got " vers) {})))
            {:proto proto :version vers :kind kind :size size-decimal}
         )))))

(deversify "KERI10JSON000000_")

(def Ilks
  "KERI protocol packet (message) types"
  {:icp "icp"                                               ; Inception                                                   - incept
   :rot "rot"                                               ; Rotation                                                    - rotate
   :ixn "ixn"                                               ; Interaction                                                 - interact
   :dip "dip"                                               ; Delegated Inception                                         - delcept
   :drt "drt"                                               ; Delegated Rotation                                          - deltate
   :rct "rct"                                               ; Receipt
   :ksn "ksn"                                               ; Key State Notice                                            - state
   :qry "qry"                                               ; Query
   :rpy "rpy"                                               ; Reply
   :exn "exn"                                               ; Exchange
   :pro "pro"                                               ; unimplemented
   :bar "bar"                                               ; unimplemented
   :exp "exp"                                               ; sealed data exposition, expose - not used
   :vcp "vcp"                                               ; Verifiable Data Registry inception                          - vdr incept
   :vrt "vrt"                                               ; Verifiable Data Registry rotation                           - vdr rotate
   :iss "iss"                                               ; Verifiable Credential Issue                                 - vc issue
   :rev "rev"                                               ; Verifiable Credential Revocation                            - vc revoke
   :bis "bis"                                               ; Registry-backed transaction event log credential issuance   - backed vc issue
   :brv "brv"                                               ; Registry-backed transaction event log credential revocation - backed vc revoke
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