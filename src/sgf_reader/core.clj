(ns sgf-reader.core
  ;; http://stackoverflow.com/questions/17432282/clojures-require-and-instaparse
  (:require [instaparse.core :as insta])
  (:use seesaw.core
        seesaw.chooser
        rhizome.viz
        [clojure.pprint :only [cl-format]]
        clojure.java.io)
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
(defn expand-home [s]
  (if (.startsWith s "~")
    (clojure.string/replace-first s "~" (System/getProperty "user.home"))
    s))

(defn file-content
  [file-name]
  (slurp (expand-home file-name)))

(defn directories-tree [p]
  (filter (fn [x] (.isDirectory x))
          (file-seq (file (expand-home p)))))

(defn first-sgf-file [dir]
  (first
   (filter #(re-matches #".*\.(sgf|SGF)" %)
           (map #(.getPath %)
                (file-seq dir)))))

(def grammar-file (clojure.java.io/file
                   (clojure.java.io/resource
                    "grammar.bnf")))

(def sgf-data
  (insta/parser
   (slurp grammar-file)))

;;; getting parsed tree of sgf file
;; (insta/parse sgf-data (slurp (games-file-selector)) )

;; getting all parsed structures for grammar tests
;; (all-parsed-files "~/Documents/Go/Professional Games/")
;; (all-parsed-files "~/Documents/Go/Pro_collection")
(defn all-parsed-files [dir]
  (map #(insta/parse sgf-data (slurp %))
       (remove nil?
               (map first-sgf-file
                    (directories-tree dir)))))
