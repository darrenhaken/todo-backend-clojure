(ns todo-backend-clojure.handler
  (:require
    [compojure.core :refer :all]
    [compojure.route :as route]
    [ring.adapter.jetty :as ring]
    [ring.middleware.json :as json-middleware]
    [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
    [todo-backend-clojure.todo_routes :as routes]
    [todo-backend-clojure.middleware :as todo-middleware])
  (:gen-class))

(defroutes app-routes
  routes/todo-routes)

(def app
  (->
    app-routes
    (todo-middleware/wrap-cors)
    (todo-middleware/wrap-response-expand-url-body)
    (todo-middleware/wrap-response-expand-location)
    (json-middleware/wrap-json-response)
    (json-middleware/wrap-json-body)
    (wrap-defaults api-defaults)))

(defn start [port]
  (ring/run-jetty app {:port  port
                               :join? false}))

(defn -main []
  (let [port (Integer. (or (System/getenv "PORT") "8080"))]
    (start port)))
