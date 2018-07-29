(ns reitit-test.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [clojure.pprint :refer [pprint]]
            [reitit.ring :as ring]
            reitit.coercion.spec
            [reitit.ring.coercion :as rrc]
            [ring.middleware.reload :refer [wrap-reload]])
  (:gen-class))


(def app
  (ring/ring-handler
    (ring/router
      [["/"
        {:get
         {:handler (fn [req]
                     {:status  200
                      :headers {"Content-Type" "text/html"}
                      :body    "Hello World"})}}]
       ["/api"
        {:get
         {:handler (fn [req]
                     (pprint req)
                     {:status  200
                      :headers {"Content-Type" "text/html"}
                      :body    "Hello World2"})}}]])))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (run-jetty (wrap-reload #'app) {:port 3000}))
