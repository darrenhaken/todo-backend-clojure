(ns todo-backend-clojure.middleware)

(def ^:private cors-headers
  "Generic CORS headers"
  {"Access-Control-Allow-Origin"  "*"
   "Access-Control-Allow-Headers" "accept, content-type"
   "Access-Control-Allow-Methods" "GET,HEAD,POST,DELETE,OPTIONS,PUT,PATCH"})

(defn- preflight?
  "Returns true if the request is a preflight request"
  [request]
  (= (request :request-method) :options))

(defn all-cors
  "Allow requests from all origins - also check preflight"
  [handler]
  (fn [request]
    (if (preflight? request)
      {:status  200
       :headers cors-headers
       :body    "preflight complete"}
      (let [response (handler request)]
        (update-in response [:headers]
                   merge cors-headers)))))
