(ns todo-backend-clojure.todo-repository
  (:require [clojure.java.jdbc :as sql]))

(def db-spec {:classname   "org.h2.Driver"
              :subprotocol "h2:file"
              :subname     "resources/db/todo;DB_CLOSE_DELAY=-1"})

(defn create-todo! [todo]
  (->
    (sql/insert! db-spec :todo todo)
    first
    vals
    first))

(defn get-by-id [id]
  (sql/query db-spec ["SELECT * FROM todo WHERE id = ?" id]))

(defn delete! [id]
  (sql/delete! db-spec :todo ["id = ?" id]))
