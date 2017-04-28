(ns sgf-reader.core
  ;; http://stackoverflow.com/questions/17432282/clojures-require-and-instaparse
  ;; wiki https://github.com/daveray/seesaw/wiki
  (:require [instaparse.core :as insta])
  (:use seesaw.core
        seesaw.graphics
        seesaw.color
        seesaw.dev                      ;if you comment it out you can use (show-options)
        seesaw.chooser
        rhizome.viz
        [clojure.pprint :only [cl-format]]
        clojure.java.io)
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Good bye javaFX!")
  ;; (native!)

  ;; menu actions
  (let [exit-action (action
                     :handler (fn [e]
                                (.dispose (to-frame e)))
                     :name "Exit"
                     :tip "Close me")
        help-action (action
                     :handler (fn [e]
                                (alert (clojure.string/join
                                        "\n")
                                       ["My first Clojure Program"
                                        "expect bugs"
                                        "and a bad code"]))
                     :name "About"
                     :tip "More information about the program")]
    ;; frame
    (invoke-later
     (-> (frame :title "Hello",
                :size [800 :by 600],
                :on-close :hide,
                :menubar (menubar :items
                                  [(menu :text "File"
                                         :items [exit-action])
                                   (menu :text "Edit" :items [])
                                   (menu :text "Help"
                                         :items [help-action])
                                   ])
                :content (border-panel
                          :north (vertical-panel :items [
                                                         "This"
                                                         "is"
                                                         "a"
                                                         "vertical"
                                                         "stack of"
                                                         "JLabels"])
                          :south (label :border ""  :id :status :text "Status Bar"))
                )
         pack!
         show!))))

;; stuck with drawing, following link should provide a clue
;; https://github.com/daveray/seesaw/blob/develop/test/seesaw/test/examples/game_of_life.clj#L139

;;; sgf format stuff -----------------------------------------------------------
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
