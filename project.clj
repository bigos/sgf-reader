(defproject sgf-reader "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [smee/binary "0.5.1"]
                 [seesaw "1.4.5"]]
  :main ^:skip-aot sgf-reader.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
