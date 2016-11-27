(ns todo-backend-clojure.middleware
  (:require
    [clojure.walk :refer [prewalk prewalk-demo]]))

(def ^:private cors-headers
  "Generic CORS headers"
  {"Access-Control-Allow-Origin"  "*"
   "Access-Control-Allow-Headers" "accept, content-type"
   "Access-Control-Allow-Methods" "GET,HEAD,POST,DELETE,OPTIONS,PUT,PATCH"})

(defn- preflight?
  "Returns true if the request is a preflight request"
  [request]
  (= (request :request-method) :options))

(defn wrap-cors
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

(defn wrap-response-expand-location [app]
  (fn [request]
    (let [response (app request)
          scheme (name (:scheme request))
          host (get-in request [:headers "host"])
          location (get-in response [:headers "Location"])]
      (if location
        (assoc-in response [:headers "Location"] (str scheme "://" host location))
        response))))

(defn url-node? [node]
  (and
    (vector? node)
    (= (first node) :url)))

(defn expand-url-node [prefix node]
  (let [url (second node)]
    (if (.startsWith url "/")
      {:url (str prefix url)}
      node)))

(defn expand-url-body [prefix body]
  (prewalk #(if (url-node? %) (expand-url-node prefix %) %) body))

(defn wrap-response-expand-url-body [app]
  (fn [request]
    (let [response (app request)
          scheme (name (:scheme request))
          host (get-in request [:headers "host"])
          body (:body response)]
      (assoc response :body (expand-url-body (str scheme "://" host) body)))))
