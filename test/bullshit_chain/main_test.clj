(ns bullshit-chain.main-test
  (:require [bullshit-chain.main :refer :all]
            [clojure.test :refer :all]))

(def genesis "{\"index\":1,\"timestamp\":0,\"proof\":1917336,\"transactions\":[{\"id\":\"b3c973e2-db05-4eb5-9668-3e81c7389a6d\",\"timestamp\":0,\"payload\":\"I am Heribert Innoq\"}],\"previousBlockHash\":\"0\"}")

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
