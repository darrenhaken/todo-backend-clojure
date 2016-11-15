(ns todo-backend-clojure.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.data.json :as json]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defn parse [body]
  (json/read-str (slurp body) :key-fn keyword))

(defn res->created [result]
  {:status  201
   :headers {"Location" (:url result)}
   :body    result})

(defn res->no-content []
  {:status 204})

(defn res->ok [body]
  {:status 200 :body body})

(defroutes app-routes
     (GET "/todo" [] (res->ok "Hello World"))
     (GET "/" [] (res->ok "Hello World"))
     (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
