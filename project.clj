(defproject bullshit-chain "0.0.1-SNAPSHOT"
  :min-lein-version "2.8.1"
  :dependencies [[digest "1.4.8"]
                 [org.clojure/clojure "1.9.0"]
                 [org.clojure/data.json "0.2.6"]]
  :main bullshit-chain.main
  :uberjar-name "bullshit-chain.jar"
  :profiles {:uberjar {:aot [bullshit-chain.main]}})
