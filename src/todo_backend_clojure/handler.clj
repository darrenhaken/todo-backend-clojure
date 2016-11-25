(ns todo-backend-clojure.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :as middleware])
  (:use [todo-backend-clojure.middleware :only [wrap-cors]]
        [todo-backend-clojure.todo-repository :as store]))

(defn- todo-url [todo-id]
  (str "/todos/" todo-id))

(defn- res->created [id]
  {:status  201
   :headers {"Location" (todo-url id)}
   :body    (todo-url id)})

(defn- res->no-content []
  {:status 204})

(defn- res->ok [body]
  {:status 200 :body body})

(defn todo-representation
  "Creates a todo to send to client that also contains the URL for the todo"
  [todo]
  (assoc todo :url (todo-url (:id todo))))

(defroutes app-routes
  (OPTIONS "/todos" []
    {:status 200})
  (GET "/todos" []
    (->
      (store/get-all)
      (res->ok)))
  (GET "/todos/:id" [id]
    (->
      (store/get-by-id id)
      (res->ok)))
  (POST "/todos" {todo :body}
    (->
      (store/create-todo! todo)
      (res->created)))
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
