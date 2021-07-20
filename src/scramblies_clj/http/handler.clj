(ns scramblies-clj.http.handler
  (:require
   [scramblies-clj.core :refer [scramble?]]
   [scramblies-clj.http.ring-extensions]
   [malli.util :as mu]
   [muuntaja.core :as m]
   reitit.coercion.malli
   [reitit.ring :as ring]
   [reitit.ring.coercion :as coercion]
   reitit.ring.malli
   [reitit.ring.middleware.exception :as exception]
   [reitit.ring.middleware.multipart :as multipart]
   [reitit.ring.middleware.muuntaja :as muuntaja]
   [reitit.ring.middleware.parameters :as parameters]
   [clojure.data.json :as json]))

(defn get-routes []
  ["/api"
   ["/is_scramble"
    {:get
     {:parameters {:query [:map [:str1 string?] [:str2 string?]]}
      :responses (do
                   (comment {200 {:body [:map ["isScramble" boolean?]]}})
                   {200 {:body boolean?}})
      :handler (fn [{{{:keys [str1 str2]} :query } :parameters :as request}]
                 {:body (scramble? str1 str2)})}}]])

(def options
  {:data
   {:coercion
    (reitit.coercion.malli/create
     {:error-keys
      #{:type :coercion :in :schema :value :errors :humanized :transformed}
      :compile mu/open-schema      
      :strip-extra-keys false
      :default-values true
      :options nil})
    
    :muuntaja m/instance
    :middleware [parameters/parameters-middleware
                 muuntaja/format-negotiate-middleware
                 muuntaja/format-response-middleware
                 (exception/create-exception-middleware
                  (merge
                   exception/default-handlers
                   {clojure.lang.ExceptionInfo
                    (fn [ex request]
                      {:status 500
                       :body (ex-data ex)})
                    ::exception/wrap (fn [handler e request]
                                       (.printStackTrace e)
                                       (handler e request))}))
                 muuntaja/format-request-middleware
                 coercion/coerce-request-middleware
                 multipart/multipart-middleware]}})

(defn get-handler []
  (let [routes (get-routes)]
    (ring/ring-handler
     (ring/router routes options))))

