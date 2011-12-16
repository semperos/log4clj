(ns com.semperos.log4clj.appender.console-appender
  (:import org.apache.log4j.ConsoleAppender))

(defn console-appender
  "Return a new instance of ConsoleAppender. Params do NOT follow the constructor's logic."
  ([] (ConsoleAppender.))
  ([name] (doto (ConsoleAppender.)
            (.setName name)))
  ([name layout] (doto (ConsoleAppender. layout)
                   (.setName name)))
  ([name layout target] (doto (ConsoleAppender. layout target)
                          (.setName name))))