(ns scramblies-clj.system
  (:require [scramblies-clj.system.config]
            [clojure.java.io :as io]
            [integrant.core :as ig]
            [aero.core :as aero]))

(defmethod aero/reader 'ig/ref
  [_ tag value]
  (ig/ref value))

(defn read-classpath-config
  [file-name]
  (aero/read-config (io/resource file-name)))

(comment
  (read-classpath-config "system-config.edn"))

(defonce state (atom nil))

(defn start!
  [config-file]
  (let [config (read-classpath-config config-file)]
    (reset!
     state
     (-> (ig/prep config)
         (ig/init)))))

(defn stop!
  []
  (reset! state (ig/halt! @state)))

(comment
  (load-config! "system-config.edn")
  (stop!))

