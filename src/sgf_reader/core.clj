(ns sgf-reader.core
  ;; http://stackoverflow.com/questions/17432282/clojures-require-and-instaparse
  (:require [instaparse.core :as insta])
  (:use seesaw.core
        seesaw.chooser
        rhizome.viz
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

(defn file-content
  [file-name]
  (slurp file-name))

(def grammar-file (clojure.java.io/file
                   (clojure.java.io/resource
                    "grammar.bnf")))

(def sgf-data
  (insta/parser
   (slurp grammar-file)))

;;; getting parsed tree of sgf file
;; (insta/parse sgf-data (slurp (games-file-selector)) )
