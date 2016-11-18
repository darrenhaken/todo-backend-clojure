(defproject todo-backend-clojure "0.1.0-SNAPSHOT"
  :description "a compojure implementation of https://github.com/moredip/todo-backend"
  :url "https://github.com/darrenhaken/todo-backend-clojure"
  :min-lein-version "2.0.0"
  :aot :all
  :java-target "1.8"
  :uberjar-name "todo-backend-clojure-standalone.jar"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.json "0.2.6"]
                 [compojure "1.5.1"]
                 [ring/ring-defaults "0.2.1"]
                 [jumblerg/ring.middleware.cors "1.0.1"]]
                 ;[hiccup "1.0.5"]
                 ;[org.clojure/java.jdbc "0.7.0-alpha1"]
                 ;[com.h2database/h2 "1.3.170"]]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler todo-backend-clojure.handler/app}
  :profiles
  {:dev {:plugins      [[lein-midje "3.2.1"]]
         :dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]
                        [midje "1.7.0" :exclusions [org.clojure/clojure]]]}})
