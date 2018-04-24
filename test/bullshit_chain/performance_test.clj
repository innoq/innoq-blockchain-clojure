(ns bullshit-chain.performance-test
  (:require [bullshit-chain.main :refer :all]
            [clojure.test :refer :all]))

(def perf-runs 10)
(deftest performance
  (let [sum (loop [x perf-runs sum 0]
              (if (< x 1)
                sum
                (recur (dec x) (+ sum (first (take-time #(block->json (next-block genesis-block [] (->IncStrategy 1 0)))))))))]
    (printf "Hashing Speed: ~ %f ms during %s tests" (double (/ sum perf-runs)) perf-runs)))
