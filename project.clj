(defproject fiber "0.1.0"

  :repositories [["conjars" {:url "https://conjars.org/repo"}]
                 ["clojars" {:url "https://clojars.org/repo"}]]

  :min-lein-version "2.0.0"

  :plugins []
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/core.async "0.4.490"]
                 [org.clojure/data.csv "0.1.4"]
                 [clj-time "0.15.0"]

                 [com.datomic/datomic-free "0.9.5656"]

                 ;
                 ]

  :repl-options {:init-ns          main
                 :main             main
                 :host             "0.0.0.0"
                 :port             4001}

  :main ^{:skip-aot true} main
  :jvm-opts ["-Xms768m" "-Xmx2048m" "-Xmx1g"]

  :source-paths ["src"]
  :java-source-paths ["src"]
  :test-paths ["test"]
  :resource-paths ["resources" "config"]

  :auto-clean false)
