(ns bullshit-chain.main
  (:require [digest :refer [sha-256]]
            [clojure.string :as str]))

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
  (str "{"
       "\"id\":\"" id "\","
       "\"timestamp\":" timestamp ","
       "\"payload\":\"" payload "\""
       "}"))

; TODO: serialize transactions with commas
(defn transactions->json [transactions]
  (str "["
       (->> transactions
            (map transaction->json)
            (reduce str))
       "]"))

(defn block->json [{:keys [index timestamp proof transactions previous-block-hash]}]
  (str "{"
       "\"index\":" index ","
       "\"timestamp\":" timestamp ","
       "\"proof\":" proof ","
       "\"transactions\":" (transactions->json transactions) ","
       "\"previousBlockHash\":\"" previous-block-hash "\""
       "}"))

(defn block->hash [block]
  (sha-256 (block->json block)))

(defn valid-hash? [hash] 
  (if hash
    (str/starts-with? hash "000")
    false))

(defn inc-strategy 
  ([] {:proof 0, :tries 1})
  ([{:keys [proof tries]}] {:proof (inc proof), :tries (inc tries)}))

(defn next-block [previous-block transactions strategy]
  ;TODO check for valid previous hash
  (let [previous-block-hash (block->hash previous-block)]
    (loop [proof-state (strategy)]
      (let [block (block (inc (:index previous-block)) 0 (:proof proof-state) transactions previous-block-hash)
            hash (block->hash block)]
        (if (valid-hash? hash)
          (do (println (str "Tries: " (:tries proof-state)))
              block)
          (recur (strategy proof-state)))))))
  
(defn -main [& args]
  (println (block->hash genesis-block))
  (println (block->json (next-block genesis-block [] inc-strategy))))
