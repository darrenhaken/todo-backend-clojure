(ns todo-backend-clojure.handler-test
  (:use midje.sweet)
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [todo-backend-clojure.handler :refer :all]))

(facts "get todos"
 (fact "get main returns Hello World"
  (let [response (app (mock/request :get "/"))]
    (:status response) => 200
    (:body response) => "Hello World")))

(facts "not found route"
    (fact "should not show route"
      (let [response (app (mock/request :get "/invalid"))]
      (:status response) => 404)))
