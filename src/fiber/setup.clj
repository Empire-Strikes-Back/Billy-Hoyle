(ns fiber.setup
  (:require [clojure.repl :refer :all]
            [app.db.core :as db]
            [datomic.api :as d]
            [app.db.query :as dbq]
            [app.data.usda]
            [clojure.java.shell :refer [sh]]
            [clojure.java.io :as io]))

(def ^:dynamic *stage* (System/getenv "STAGE"))

(defn download-data!
  []
  (try
    (when (not (.exists (io/file "/opt/data")))
      (do
        (sh "/bin/bash" "-c"
            "git clone https://github.com/fiber.data /opt/data/"
            :dir "/opt")))
    (catch Exception e (do (prn (.getMessage e)) false))))

#_(download-data!)
#_(sh "/bin/bash" "-c" "ls /opt/data1/fiber.data" :dir "/opt")

(defn db-sane?
  []
  (try
    (and
     (= 150 (ffirst (d/q (:q dbq/count-nutrients) (db/db-now))))
     (= 8789 (ffirst (d/q (:q dbq/count-food-items) (db/db-now)))))
    (catch Exception e (do (prn (.getMessage e)) false))))

#_(db-populated?)

(defn upload-data!
  []
  (download-data!)
  (app.data.usda/del-files!)
  (app.data.usda/create-files!)
  (app.db.core/populate!))

(defn init!
  []
  (if
   (and
    (not (= fiber.setup/*stage* "dev"))
    (not (app.db.core/db-exists?)))
    (do
      (app.db.core/connect!)
      (upload-data!))
    (app.db.core/connect!)))