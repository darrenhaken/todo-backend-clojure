(ns todo-backend-clojure.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.data.json :as json]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.reload :as reload]
            [ring.middleware.cors :refer [wrap-cors]]))

(defn parse [body]
  (json/read-str (slurp body) :key-fn keyword))

(defn- res->created [result]
  {:status  201
   :headers {"Location" (:url result)}
   :body    result})

(defn- res->no-content []
  {:status 204})

(defn- res->ok [body]
  {:status 200 :body body})

(defroutes app-routes
  (OPTIONS "/todos" []
    {:status 200})
  (OPTIONS "/todos/:id" [id]
    {:status 200})
  (GET "/todo" []
    (res->ok "Hello World"))
  (POST "/todo" []
    res->created {:body "body"})
  (GET "/todo" []
    (res->ok "Hello World"))
  (GET "/" []
    (res->ok "Hello World"))
  (route/not-found
    (res->no-content)))

(def app
  (->
    (wrap-defaults app-routes (assoc site-defaults :session false))
    (wrap-cors :access-control-allow-origin [#"http://localhost"]
               :access-control-allow-methods [:get :put :post :delete])))
