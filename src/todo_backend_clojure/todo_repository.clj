(ns todo-backend-clojure.todo-repository
  (:require [clojure.java.jdbc :as sql]))

(def db-spec {:classname   "org.h2.Driver"
              :subprotocol "h2:file"
              :subname     "resources/db/todo;DB_CLOSE_DELAY=-1"})

(defn- extract-id-from-result [result-sec]
  (->
    result-sec
    first
    vals
    first))

(defn create-todo!
  "Creates a todo and returns the ID"
  [todo]
  (comp (sql/insert! db-spec :todo todo) extract-id-from-result))

(defn get-all [])

(defn get-by-id
  "Get todo by ID. Returns a list of todos"
  [id]
  (sql/query db-spec ["SELECT * FROM todo WHERE id = ?" id]))

(defn delete!
  "Delete the todo. Returns a list containing the ID of the deleted record"
  [id]
  (sql/delete! db-spec :todo ["id = ?" id]))

(defn delete-all!)
