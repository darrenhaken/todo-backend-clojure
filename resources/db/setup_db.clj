(ns todo-backend-clojure.setup-db
  (:require [clojure.java.jdbc :refer :all :as sql]))

(def db-spec {:classname   "org.h2.Driver"
              :subprotocol "h2:file"
              :subname     "resources/db/todo-app;DB_CLOSE_DELAY=-1"})

(sql/db-do-commands db-spec
                    (sql/create-table-ddl :todo
                                          [[:name "varchar(32)"]]))
