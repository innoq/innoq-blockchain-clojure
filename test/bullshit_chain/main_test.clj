(ns bullshit-chain.main-test
  (:require [bullshit-chain.main :refer :all]
            [clojure.test :refer :all]))

(deftest test-block->json
  (is (= genesis (block->json genesis-block))))

