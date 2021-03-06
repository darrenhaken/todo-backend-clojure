(ns todo-backend-clojure.setup-db
  (:require [clojure.java.jdbc :refer :all :as sql]))

(def db-spec {:classname   "org.h2.Driver"
              :subprotocol "h2:file"
              :subname     "resources/db/todo;DB_CLOSE_DELAY=-1"
              :username    "sa"
              :password    ""})

(defn create-todo-table []
  (sql/db-do-commands db-spec
    (sql/create-table-ddl :todo
      [[:id "bigint" :primary :key "auto_increment"]
       [:title "varchar(32)"]
       [:position :int]
       [:completed :boolean]])))

(defn drop-todo-table []
  (sql/drop-table-ddl "todo"))
