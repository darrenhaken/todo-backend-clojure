(ns todo-backend-clojure.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
    ;[clojure.data.json :as json]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]])
  (:use [todo-backend-clojure.middleware :only [wrap-cors]]))

;(defn parse [body]
;  (json/read-str (slurp body) :key-fn keyword))

(defn- res->created [result]
  {:status  201
   :headers {"Location" (:url result)}
   :body    result})

(defn- res->no-content []
  {:status 204})

(defn- res->ok [body]
  {:status 200 :body body})

(defroutes app-routes
  (OPTIONS "/todo" []
    {:status 200})
  (GET "/todo" []
    (res->ok "Hello World"))
  (POST "/todo" []
    (res->created {:body "body"}))
  (DELETE "/todo" []
    (res->ok "Hello World"))
  (route/not-found
    (res->no-content)))

(def app
  (->
    (wrap-defaults app-routes api-defaults)
    wrap-cors))
