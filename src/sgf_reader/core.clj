(ns sgf-reader.core
  ;; http://stackoverflow.com/questions/17432282/clojures-require-and-instaparse
  (:require [instaparse.core :as insta])
  (:use seesaw.core
        seesaw.chooser
        [clojure.pprint :only [cl-format]]
        [clojure.java.io :only [input-stream]])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Good bye javaFX!")
  (native!)
  (invoke-later
   (-> (frame :title "Hello",
              :content "Hello, hello hello my dear Seesaw",
              :on-close :exit)
       pack!
       show!)))

(defn games-file-selector
  []
  (choose-file :filters [["Games" ["sgf"]
                          ["Folders" #(.isDirectory %)]
                          (file-filter "All files" (constantly true))]]))

;; (defn file-content
;;   [file-name]
;;   (let [in (input-stream file-name)]
;;       (cl-format true "=== ~A ~A~%" in *in*)))
(defn file-content
  [file-name]
  (slurp file-name))

;; http://stackoverflow.com/questions/17432282/clojures-require-and-instaparse
(def as-and-bs
  (insta/parser
   "S = AB*
     AB = A B
     A = 'a'+
     B = 'b'+"))

(def sgf-data
  (insta/parser
   "S = GameTree+
  GameTree   = '(' Sequence GameTree* ')'
  Sequence   = Node+
  Node       = Newline? ';' Property* Newline?
  Property   = ( (Newline)? PropIdent PropValue+)+ Newline?
  PropIdent  = UcLetter+
  PropValue  = '[' CValueType ']'
  CValueType = (ValueType | Compose)
  Newline = #\"$\"
  Digit = ('0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9')
  Digits = Digit+
  Number = ('+' '-')? Digits
  Real = Number '.' Digits
  Double = Real 'e' Real
  Color = 'white' 'black'
  SimpleText = (#\".\")+
  Text = SimpleText
  Point = (LcLetter LcLetter)
  ValueType  = ( Number | Real | Double | Color | SimpleText | Text )?
  LcLetter = #\"[a-z]\"
  UcLetter   = ('A'|'B'|'C'|'D'|'E'|'F'|'G'|'H'|'I'|'J'|'K'|'L'|'M'|'N'|'O'|'P'|'Q'|'R'|'S'|'T'|'U'|'V'|'W'|'X'|'Y'|'Z')
  Compose    = ValueType ':' ValueType
   "))
