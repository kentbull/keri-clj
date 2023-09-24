(ns keri.helping)

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


