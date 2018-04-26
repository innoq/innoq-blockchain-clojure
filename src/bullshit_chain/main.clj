(ns bullshit-chain.main
  (:require [digest :refer [sha-256]]
            [clojure.string :as str]))

(defprotocol JsonValueSerializer
  (to-json [this]))

(extend-type String
  JsonValueSerializer
    (to-json [this] (str "\"" this "\"")))

(extend-type Number
  JsonValueSerializer
    (to-json [this] this))

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
       "\"id\":" (to-json id) ","
       "\"timestamp\":" (to-json timestamp) ","
       "\"payload\":" (to-json payload)
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
       "\"index\":" (to-json index) ","
       "\"timestamp\":" (to-json timestamp) ","
       "\"proof\":" (to-json proof) ","
       "\"transactions\":" (transactions->json transactions) ","
       "\"previousBlockHash\":" (to-json previous-block-hash)
       "}"))

(defn block->hash [block]
  (sha-256 (block->json block)))

(defn valid-hash? [hash]
  (if hash
    (str/starts-with? hash "000")
    false))

(defprotocol Strategy
  (next-proof [this])
  (get-name [this]))

(defrecord IncStrategy [tries proof]
  Strategy
  (next-proof [this] (update (update this :proof inc) :tries inc))
  (get-name [this] "IncStrategy"))

(defn next-random [_] (rand-int 10000000))
(defrecord RandStrategy [tries proof]
  Strategy
  (next-proof [this] (update (update this :proof next-random) :tries inc))
  (get-name [this] "RandStrategy"))

(defn next-block [previous-block transactions strategy]
  ;TODO check for valid previous hash
  (let [previous-block-hash (block->hash previous-block)
        start (System/currentTimeMillis)]
    (loop [proof-state strategy]
      (let [block (block (inc (:index previous-block)) 0 (:proof proof-state) transactions previous-block-hash)
            hash (block->hash block)]
        (if (valid-hash? hash)
          (let [time (- (System/currentTimeMillis) start)]
            (do (printf "Mined new Block with %s tries in %sms (%.2f MH/s) with %s\n"
                        (:tries proof-state)
                        time
                        (double (/ (:tries proof-state) time))
                        (get-name proof-state))
              block))
          (recur (next-proof proof-state)))))))

(defn take-time [code]
  (let [start (System/currentTimeMillis) return-values (code)]
    (let [end (System/currentTimeMillis)]
      [(- end start) return-values])))

(defn print-hash-time [[time hash]]
      (printf "%sms for %s\n" time hash) hash)
  
(defn -main [& args]
  
  ;(print-hash-time (take-time #(block->hash genesis-block)))
  ;(print-hash-time (take-time #(block->json (next-block genesis-block [] (->IncStrategy 1 0))))))
  ;(println (block->json (next-block genesis-block [] (->RandStrategy 1 0))))
  
  (.start (Thread. (fn [] (println (next-block genesis-block [] (->IncStrategy 1 0))))))
  (.start (Thread. (fn [] (println (next-block genesis-block [] (->RandStrategy 1 0))))))
  
  (println "threads started"))
