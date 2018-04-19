(defproject bullshit-chain "0.0.1-SNAPSHOT"
  :min-lein-version "2.8.1"
  :dependencies [[org.clojure/clojure "1.9.0"]]
  :main bullshit-chain.main
  :uberjar-name "bullshit-chain.jar"
  :profiles {:uberjar {:aot [bullshit-chain.main]}})
