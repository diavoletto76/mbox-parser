# mbox-parser

Pure Clojure implementation of a lazy Mbox parser.


## Usage

Utility function to create `MimeMessages` from _sequence of strings_
representing mail message.

``` clojure
(import javax.mail.Session)
(import javax.internet.MimeMessage)

(defn parse-message
  [los]
  (->> (clojure.string/join "\n" los)
       (mbox-parser.utils/string->stream)
       (MimeMessage. (-> (Properties.) (Session/getInstance)))))
```


Utility function to parse Mbox returning lazy sequence of `MimeMessages`.

``` clojure
(import javax.mail.Session)
(import javax.internet.MimeMessage)

(defn mbox->messages
  [path]
  (with-open [rdr (clojure.java.io/reader path)]
    (->> (parse-reader rdr)
         (map parse-message)
         (doall))))
```

`mbox-parser` doesn't implement these functions because it doesn't
relay on javamail library and doesn't take care of opening resources
for reading messages.


Given previous functions `mbox-parser` can be used like this.

``` clojure
(defn list-of-subjects
    [mbox]
    (->> (mbox->messages mbox)
         (map #(.getSubject %)))
```


## License

Copyright © 2019 Diavoletto76

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
