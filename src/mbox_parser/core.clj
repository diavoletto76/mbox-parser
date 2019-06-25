(ns mbox-parser.core)


(defn mbox-separator?
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
         ;; (map #(do (println "log") (identity %)))
         (partition-by (fn [[a b]] (mbox-separator? a b)))
         (filter (fn [[[a b]]] ((complement mbox-separator?) a b)))
         (map #(map first %))
         (map rest))))


(defn parse-mbox-file
  ""
  [path]
  (with-open [rdr (clojure.java.io/reader path)]
    (->> (line-seq rdr)
         (parse-lines)
         ;; (take 2)
         (doall))))

;;;;

(def mboxxx1 "var/Archivio/Folder1.mbox/mbox")

(defn usage1
  []
  (parse-mbox-file mboxxx1))
