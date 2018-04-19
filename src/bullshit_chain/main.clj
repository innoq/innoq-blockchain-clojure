(ns bullshit-chain.main
  (:require [digest :refer [sha-256]]))

(def genesis "{\"index\":1,\"timestamp\":0,\"proof\":1917336,\"transactions\":[{\"id\":\"b3c973e2-db05-4eb5-9668-3e81c7389a6d\",\"timestamp\":0,\"payload\":\"I am Heribert Innoq\"}],\"previousBlockHash\":\"0\"}")

(defn transaction [id timestamp payload]
  {:id id
   :timestamp timestamp
   :payload payload})

(defn block [index timestamp proof transactions previous-block-hash]
  {:index index
   :timestamp timestamp
   :proof proof
   :transactions transactions
   :previous-block-hash previous-block-hash})

(def genesis-block (block 1
                          0
                          1917336
                          [(transaction
                             "b3c973e2-db05-4eb5-9668-3e81c7389a6d"
                             0
                             "I am Heribert Innoq")]
                          "0"))

(defn transaction->json [{:keys [id timestamp payload]}]
  (str "{\"id\":\"" id "\",\"timestamp\":" timestamp ",\"payload\":\"" payload "\"}"))

(defn transactions->json [transactions]
  (str "[" (reduce str (map transaction->json transactions))  "]"))

(defn block->json [{:keys [index timestamp proof transactions previous-block-hash]}]
  (str "{"
       "\"index\":" index ","
       "\"timestamp\":" timestamp ","
       "\"proof\":" proof ","
       "\"transactions\":" (transactions->json transactions) ","
       "\"previousBlockHash\":\"" previous-block-hash "\""
       "}"))

(defn to-json [block] block)

(defn to-hash [block]
  (sha-256 (to-json block)))

(defn -main [& args]
  (println (to-hash genesis)))
