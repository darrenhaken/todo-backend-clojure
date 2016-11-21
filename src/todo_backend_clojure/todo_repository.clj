(ns todo-backend-clojure.todo-repository)

(def todo-db (atom {}))

(defn add [table doc] (swap! todo-db update-in [table] conj doc))
