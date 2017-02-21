(ns sgf-reader.core
  (:use seesaw.core seesaw.chooser
        org.clojars.smee.binary.core
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
  (let [in (input-stream file-name)]
      (cl-format true "=== ~A ~A~%" in *in*)))
