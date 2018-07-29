(ns reitit-test.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [clojure.pprint :refer [pprint]]
            [reitit.ring :as ring]
            reitit.coercion.spec
            [ring.middleware.params :refer [wrap-params]]
            [muuntaja.core :as muuntaja]
            [muuntaja.middleware :refer [wrap-format]]
            [reitit.ring.coercion :as rrc]
            [ring.middleware.reload :refer [wrap-reload]])
  (:gen-class))

(def m (muuntaja/create (update muuntaja/default-options :formats select-keys ["application/json"])))

(def app
  (ring/ring-handler
    (ring/router
      [["/"
        {:get
         {:handler (fn [req]
                     {:body {:x "got stuff"}})}}]
       ["/post"
        {:post
         {:handler (fn [req]
                     {:body (:body-params req)})}}]
       ["/api"
        {:get
         {:handler (fn [req]
                     (pprint req)
                     {:body {:y "Hello World2"}})}}]]
      {:data
       {:middleware [wrap-params
                     #(wrap-format % m)]}})
    (ring/create-default-handler)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (run-jetty (wrap-reload #'app) {:port 3000}))
