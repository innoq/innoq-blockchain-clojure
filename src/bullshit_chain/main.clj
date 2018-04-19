(ns bullshit-chain.main 
  (:require [digest :refer [sha-256]]))
  
(def genesis "{\"index\":1,\"timestamp\":0,\"proof\":1917336,\"transactions\":[{\"id\":\"b3c973e2-db05-4eb5-9668-3e81c7389a6d\",\"timestamp\":0,\"payload\":\"I am Heribert Innoq\"}],\"previousBlockHash\":\"0\"}")

(defn to-json [block] block)

(defn to-hash [block]
  (sha-256 (to-json block)))
    
(defn -main [& args]
  (println (to-hash genesis)))
