(ns scramblies-clj.http.ring-extensions
  (:require [clojure.java.io :as io]))

(defn- response-writer [response output-stream]
  (if-let [charset (ring.util.response/get-charset response)]
    (io/writer output-stream :encoding charset)
    (io/writer output-stream)))

(extend-protocol ring.core.protocols/StreamableResponseBody
  java.lang.Boolean
  (write-body-to-stream [body response output-stream]
    (doto (response-writer response output-stream)
      (.write (if body "true" "false"))
      (.close))))

