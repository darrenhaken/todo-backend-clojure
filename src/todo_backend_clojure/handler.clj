(ns todo-backend-clojure.handler
  (:require
    [compojure.core :refer :all]
    [compojure.route :as route]
    [ring.middleware.json :as middleware]
    [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
    [todo-backend-clojure.todo_routes :as routes]
    [todo-backend-clojure.middleware :as cors]))

(defroutes app-routes
  routes/todo-routes)

(def app
  (->
    app-routes
    (cors/wrap-cors)
    (middleware/wrap-json-response)
    (middleware/wrap-json-body)
    (wrap-defaults api-defaults)))
