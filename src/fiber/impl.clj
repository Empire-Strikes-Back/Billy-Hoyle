(ns fiber.impl
  (:require [clojure.repl :refer :all]
            [clojure.reflect :refer :all]
            [clojure.pprint :as pp]
            [clojure.java.io :as io]
            [clojure.string :as cstr]
            [datomic.api :as d]
            ;
            ))


(defn read-nth-line
  "Read line-number from the given text file. The first line has the number 1."
  [filename line-number]
  (with-open [rdr (clojure.java.io/reader filename)]
    (nth (line-seq rdr) (dec line-number))))

(defn count-lines
  [filename]
  (with-open [rdr (clojure.java.io/reader filename)]
    (count (line-seq rdr))))

(defn nl
  "append newline char to str"
  [s]
  (str s "\n"))

(defn write-lines
  "Write lines vector to file"
  [filename lines-vec]
  (with-open [w (clojure.java.io/writer filename :append true)]
    (doseq [line lines-vec]
      (.write w (nl line)))))

(defn delete-files
  [& filenames]
  (doseq [filename filenames]
    (.delete (java.io.File. filename))))

(defn mk-dirs
  "Make directories in the path"
  [path]
  (.mkdirs (java.io.File. path)))

(defn create-file
  [filename]
  (.createNewFile (java.io.File. filename)))

(defn read-binary-file
  "returns bute-array , accepts :offset :limit"
  [filename & {:keys [offset limit]
               :or   {offset 0}}]
  (let [f   (io/file filename)
        lim (or limit (- (.length f) offset))
        buf (byte-array lim)]
    ; (prn lim offset)
    (with-open [in (io/input-stream  f)]
      (.skip in offset)
      (.read in buf 0 lim)
      buf
      ;
      )))

(defn bytes->int [bytes]
  "Converts a byte array into an integer."
  (->>
   bytes
   (map (partial format "%02x"))
   (apply (partial str "0x"))
   read-string))

(defn read-int-from-file
  "returns the number represented by bytes in range [offset , + offset  limit]"
  [filename offset limit]
  (->   (read-binary-file  filename :offset offset :limit limit)
        bytes->int))



(defn get-paginted-entity
  "Returns entities and total count given limit,offset and attribute keword"
  [{:keys [db attribute limit offset]
    :or   {limit  10
           offset 0}}]
  {:entities (->>
              (d/q '{:find  [?e (count ?e)]
                     :in    [$ ?attribute]
                     :where [[?e ?attribute]]}
                   db attribute)
              (drop offset)
              (take limit)
              (map  #(identity (d/pull db '[*] (first %))))
              (into []))
   :count    (d/q '{:find  [(count ?e) .]
                    :in    [$ ?attribute]
                    :where [[?e ?attribute]]}
                  db attribute)})


(defn count-total
  [db attribute]
  (d/q '{:find  [(count ?e) .]
         :in    [$ ?attribute]
         :where [[?e ?attribute]]}
       db attribute))

(defn q-idents
  "find the idents of all schema elements in the system"
  [db]
  (sort (d/q '[:find [?ident ...]
               :where [_ :db/ident ?ident]]
             db)))


(defn q-attrs
  "find just the attributes"
  [db]
  (sort (d/q '[:find [?ident ...]
               :where
               [?e :db/ident ?ident]
               [_ :db.install/attribute ?e]]
             db)))

(defn excise
  [conn eids]
  (do @(d/transact conn
                   (mapv (fn [e]
                           {:db/excise e}) eids))))

(comment
  ;
  )

(defn java-version
  []
  (System/getProperty "java.vm.version"))

(comment


  (System/getProperty "java.vm.version")
  (System/getProperty "java.version")
  (System/getProperty "java.specification.version")
  (clojure-version)

  ;
  )