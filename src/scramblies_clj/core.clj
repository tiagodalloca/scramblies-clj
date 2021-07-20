(ns scramblies-clj.core
  (:require [clojure.set :as s]))

(defn scramble? [str1 str2]
  (let [f1 (frequencies str1)
        f2 (frequencies str2)]
    (reduce-kv
     (fn [a char freq]
       (if a
         (>= (or (get f1 char) 0)
             freq)
         (reduced false)))
     true f2)))

(comment
  (scramble? "aaghtilo" "tiago"))

