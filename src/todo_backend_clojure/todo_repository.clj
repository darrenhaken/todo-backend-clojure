(ns todo-backend-clojure.todo-repository
  (:require
    [clojure.java.jdbc :as sql]
    [clojure.set :refer [rename-keys]]))

(def db-spec {:classname   "org.h2.Driver"
              :subprotocol "h2:file"
              :subname     "resources/db/todo;DB_CLOSE_DELAY=-1"})

(defn- row->todo
  "Convert sequence field in DB to order field on todo"
  [row]
  (rename-keys row {:position :order}))

(defn- todo->row [todo]
  "Convert sequence field in DB to order field on todo"
  (rename-keys todo {"order" :position :order :position}))

(defn- mark-as-incomplete [todo]
  (merge todo {:completed false}))

(defn- sql-result->id [result-seq]
  (->
    result-seq
    first
    vals
    first))

(defn get-all []
  (sql/query db-spec
             ["SELECT * FROM todo"]
             {:row-fn row->todo}))

(defn get-by-id
  "Get todo by ID. Returns first todo in the vector"
  [id]
  (first (sql/query db-spec
                    ["SELECT * FROM todo WHERE id = ?" id]
                    {:row-fn row->todo})))

(defn create-todo!
  "Creates a todo and returns the ID"
  [todo]
  (let [incomplete-todo (mark-as-incomplete todo)]
    (let [id (sql-result->id
               (sql/insert! db-spec :todo (todo->row incomplete-todo)))]
      (merge incomplete-todo {:id id}))))

(defn update-todo! [id todo]
  (sql/update! db-spec :todo (todo->row todo) ["id = ?" id])
  (get-by-id id))

(defn delete!
  "Delete the todo. Returns a list containing the ID of the deleted record"
  [id]
  (sql/delete! db-spec :todo ["id = ?" id]))

(defn delete-all! []
  (sql/delete! db-spec :todo [true]))
