(defproject org.diavoletto76/mbox-parser "0.1.0-SNAPSHOT"
  :description "Pure Clojure implementation of a lazy Mbox parser"
  :url "https://github.com/diavoletto76/mbox-parser"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]]
  :repl-options {:init-ns mbox-parser.core}
  :profiles {:dev  {:dependencies [[javax.mail/mail "1.4.7"]]}
             :test {:dependencies [[javax.mail/mail "1.4.7"]]}})
