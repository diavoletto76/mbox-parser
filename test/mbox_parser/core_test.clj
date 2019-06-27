(ns mbox-parser.core-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [mbox-parser.core :refer :all])
  (:import [javax.mail Session Transport Message$RecipientType]
           [javax.mail.internet MimeMessage InternetAddress]
           [java.util Properties]))

(def mbox1 "var/Folder.mbox/mbox")
(def session (-> (Properties.) (Session/getInstance)))


(defn string->stream
  ([s] (string->stream s "UTF-8"))
  ([s encoding]
   (-> s
       (.getBytes encoding)
       (java.io.ByteArrayInputStream.))))


(defn parse-message
  [los]
  (->> (clojure.string/join "\n" los)
       (string->stream)
       (MimeMessage. session)))


(defn mbox->seq
  [path]
  (with-open [rdr (clojure.java.io/reader path)]
    (-> (parse-reader rdr)
        (doall))))


(defn mbox->messages
  [path]
  (->> (mbox->seq path)
       (map parse-message)))


(deftest test-count-messages
  (testing "count messages"
    (is (= 5 (count (mbox->seq mbox1)))
        (= 5 (count (mbox->messages mbox1))))))


(deftest test-message-1
  (testing "message 1"
    (let [m (nth (mbox->messages mbox1) 0)
          f (aget (.getFrom m) 0)]
      (is (= "A formatted message" (.getSubject m))
          (= "diavoletto76@gmail.com" (.getAddress f))))))


(deftest test-message-2
  (testing "message 2"
    (let [m (nth (mbox->messages mbox1) 1)
          f (aget (.getFrom m) 0)]
      (is (= "Two attachments" (.getSubject m))
          (= "diavoletto76@gmail.com" (.getAddress f))))))


(deftest test-subjects
  (testing "subjects"
    (let [expected '("A formatted message"
                     "Two attachments"
                     "A message with an attachment"
                     "Second message"
                     "First message")
          subjects (->> (mbox->messages mbox1)
                        (map #(.getSubject %)))]
      (is (every? true? (map = expected subjects))))))
