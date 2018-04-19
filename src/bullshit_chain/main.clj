(ns bullshit-chain.main 
  (:require [digest :refer [sha-256]]))
  
(defn to-json [block] "123")

(defn to-hash [block]
  (sha-256 (to-json block)))
    
(defn -main [& args]
  (println (to-hash nil)))
