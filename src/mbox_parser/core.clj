(ns mbox-parser.core
  (:require [clojure.java.io :as io])
  (:import [javax.mail Session Transport Message$RecipientType]
           [javax.mail.internet MimeMessage InternetAddress]
           [java.util Properties]))


(defn- mbox-separator?
  "As per RFC 4155 (https://tools.ietf.org/html/rfc4155) Mbox separator is
  a sequence of empty line + line containing \"From <email> <timestamp>\""

  ;; TODO Enhance regexp to better match line2
  [line1 line2]
  (and (empty? line1)
       (boolean (re-matches #"^From .*@.* .*$" line2))))


(defn parse-lines
  [lines]
  (let [fixed-lines (cons "" lines)]
    (->> (map list fixed-lines (rest fixed-lines))
         (partition-by (fn [[a b]] (mbox-separator? a b)))
         (filter (fn [[[a b]]] ((complement mbox-separator?) a b)))
         (map #(map first %))
         (map rest))))


(defn parse-mbox-file
  ""
  [path]
  (with-open [rdr (io/reader path)]
    (->> (line-seq rdr)
         (parse-lines)
         (doall))))


(defn string->stream
  ([s] (string->stream s "UTF-8"))
  ([s encoding]
   (-> s
       (.getBytes encoding)
       (java.io.ByteArrayInputStream.))))


(def session (Session/getInstance (Properties.)))


(defn get-message [los]
  (->> (clojure.string/join "\n" los)
       (string->stream)
       (MimeMessage. session)))
