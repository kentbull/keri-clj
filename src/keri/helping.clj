(ns keri.helping
  (:import org.apache.commons.codec.binary.Hex))

(defn isign [i]
  "Integer sign function.
  Returns 1 for positive, 0 for 0, and -1 for negative"
  (cond
    (> i 0) 1
    (< i 0) -1
    :else 0)
  )

(defn sceil [r]
  "Symmetric ceiling function
  Returns symmetric ceiling of r away from zero
  "
  (+ (int r) (isign (- r (int r)))))


;
; hexadecimal encoding and decoding utilities
;

(defn bytes->hex
  "Convert a byte array to hex encoded string."
  [^bytes data]
  (Hex/encodeHexString data))

(defn hexify "Convert byte sequence to hex string" [coll]
  (let [hex [\0 \1 \2 \3 \4 \5 \6 \7 \8 \9 \a \b \c \d \e \f]]
    (letfn [(hexify-byte [b]
              (let [v (bit-and b 0xFF)]
                [(hex (bit-shift-right v 4)) (hex (bit-and v 0x0F))]))]
      (apply str (mapcat hexify-byte coll)))))

(defn hexify-str [s]
  (hexify (.getBytes s)))

(defn unhexify "Convert hex string to byte sequence" [s]
  (letfn [(unhexify-2 [c1 c2]
            (unchecked-byte
              (+ (bit-shift-left (Character/digit ^char c1 16) 4)
                (Character/digit ^char c2 16))))]
    (map #(apply unhexify-2 %) (partition 2 s))))

(defn unhexify-str [s]
  (apply str (map char (unhexify s))))