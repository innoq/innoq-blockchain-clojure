(ns bullshit-chain.main-test
  (:require [bullshit-chain.main :refer :all]
            [clojure.test :refer :all]))

(deftest test-block->json
  (is (= genesis (block->json genesis-block))))

(deftest test-block->hash
  (is (= "000000b642b67d8bea7cffed1ec990719a3f7837de5ef0f8ede36537e91cdc0e"
         (block->hash genesis-block)))) 


(deftest test-valid-hash?
  (are [hash expected] (= (valid-hash? hash)
                          expected)
    "000000b642b67d8bea7cffed1ec990719a3f7837de5ef0f8ede36537e91cdc0e" true
    "deadbeefb642b67d8bea7cffed1ec9907a3f7837de5ef0f8ede36537e91cdc0e" false
    nil false
    "" false
    "000000" true))
