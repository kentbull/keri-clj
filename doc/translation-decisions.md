# Translation Decisions

When translating the Python implementation of KERI, KERIpy, decisions must be made when certain Pythonic expressions 
do not map well, or do not map directly, to a similar expression in Clojure. This document is a record of all of those 
translation decisions. Some are major differences and some are minor.

# Minor differences

## Matter.Bards

Use byte vectors rather than byte arrays for the keys in the Bards definition.

Example:
- Python: {Key of "A" : Value of 1} would be {b'\x00': 1}
  - Python supports using a byte literal, printed as the hex value x00, and will compare byte arrays
    for you in the background. This is compare by value since the contents of the byte array are checked.
  - Since byte arrays in Clojure are objects Clojure compares by reference and not value.
    This means you must compare the contents of the array.
- Clojure: {Key of "A" : Value of 1} would be {
Since byte arrays in the JVM, and thus in Clojure, 

# Major differences