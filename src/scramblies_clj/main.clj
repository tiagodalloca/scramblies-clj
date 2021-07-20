(ns scramblies-clj.main
  (:require [scramblies-clj.system :as system])
  (:gen-class))

(defn -main
  []
  (system/start! "system-config.edn"))

