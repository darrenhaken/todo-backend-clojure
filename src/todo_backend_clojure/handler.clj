(ns todo-backend-clojure.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :as middleware])
  (:use [todo-backend-clojure.middleware :only [wrap-cors]]
        [todo-backend-clojure.todo-repository :as store]))

(defn- todo-url [todo-id]
  (str "http://localhost:3000/todos/" todo-id))

(defn- todo-representation
  "Creates a todo to send to client that also contains the URL for the todo"
  [todo]
  (let [todo-url (todo-url (:id todo))]
    (assoc todo :url todo-url)))

(defn- res->created [todo]
  {:status  201
   :headers {"Location" (:url todo)}
   :body    todo})

(defn- res->no-content []
  {:status 204})

(defn- res->ok [body]
  {:status 200 :body body})

(defroutes app-routes
  (OPTIONS "/todos" []
    {:status 200})
  (GET "/todos" []
    (->
      (store/get-all)
      (#(map todo-representation %))
      (res->ok)))
  (GET "/todos/:id" [id]
    (->
      (store/get-by-id id)
      (todo-representation)
      (res->ok)))
  (POST "/todos" {todo :body}
    (let [todo-representation (todo-representation (store/create-todo! todo))]
      (res->created todo-representation)))
  (PATCH "/todos/:id" {{id :id} :params todo :body}
    (-> todo
        (#(store/update-todo! id %))
        (todo-representation)
        (res->ok)))
  (DELETE "/todos" []
    (store/delete-all!)
    (res->no-content))
  (DELETE "/todos/:id" [id]
    (store/delete! id)
    (res->no-content))
  (route/not-found
    (res->no-content)))

(def app
  (->
    app-routes
    (wrap-cors)
    (middleware/wrap-json-response)
    (middleware/wrap-json-body)
    (wrap-defaults api-defaults)))
