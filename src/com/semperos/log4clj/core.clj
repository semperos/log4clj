(ns com.semperos.log4clj.core
  (:require [com.semperos.log4clj.appender.swing-appender :as swing]
            [com.semperos.log4clj.appender.console-appender :as console])
  (:import [org.apache.log4j ConsoleAppender Level Logger PatternLayout]))

;; RETURN LOGGER WHERE POSSIBLE

;; Object mangling
(defn attribute
  "Return the value of `attribute` for the given object `o`. Uses the `bean` function under the hood."
  [o attribute]
  (let [beany (bean o)]
    (get beany attribute)))

(defn attributes
  "Return all attributes available for the given object `o`. Uses the `bean` function under the hood."
  [o]
  (let [beany (bean o)]
    (keys beany)))

(defn values
  "Return the values of all attributes available for the given object `o`. Uses the `bean` function under the hood."
  [o]
  (let [beany (bean o)]
    (vals beany)))

;; Getting a Logger
(defn root-logger
  "Return the log4j root logger"
  []
  (. Logger getRootLogger))

(defn logger
  "Return the logger with the given `name`"
  [name]
  (.. (root-logger) getLoggerRepository (getLogger name)))

;; Level Setting
(defn- level-legend
  [level]
  (get {:all Level/ALL
        :debug Level/DEBUG
        :error Level/ERROR
        :fatal Level/FATAL
        :info Level/INFO
        :off Level/OFF
        :trace Level/TRACE
        :trace-int Level/TRACE_INT
        :warn Level/WARN} level))

(defn set-level
  "Set the Level of the given `logger`"
  ([level] (set-level (root-logger) level))
  ([logger level]
     (.setLevel logger (level-legend level))
     logger))

;; Layouts
(defn pattern-layout
  "Return a new instance of PatternLayout. Uses `pattern` if specified, other uses DEFAULT_LAYOUT_PATTERN"
  ([] (PatternLayout.))
  ([pattern] (PatternLayout. pattern)))

;; Appenders
(defn appenders
  "Return seq of appenders for given `logger` (fishy why we can't just use `seq` here)"
  ([] (appenders (root-logger)))
  ([logger]
     (let [appender-enum (.getAllAppenders logger)]
       (loop [final-seq nil]
         (if-not (.hasMoreElements appender-enum)
           final-seq
           (recur (conj final-seq (.nextElement appender-enum))))))))

(defn appender?
  "Return the appender with name `appender-name` if it is attached to the given `logger` instance, nil otherwise"
  ([appender-name] (appender? (root-logger) appender-name))
  ([logger appender-name]
     (.getAppender logger appender-name)))

(defn add-appender
  "Add the given `appender` instance to the given `logger` instance. Checks to see if an appender with the same name is already added to the `logger` instance."
  ([appender] (add-appender (root-logger) appender))
  ([logger appender]
     (if-not (appender? logger (attribute appender :name))
       (do
         (.addAppender logger appender)
         logger)
       nil)))

(defn remove-appender
  "Remove the given `appender` from the given `logger` instance. The `appender` parameter may be either a string or an actual instance of the appender. Defaults to root logger if none is specified."
  ([appender] (remove-appender (root-logger) appender))
  ([logger appender]
     (.removeAppender logger appender)))


;; ## Convenience Defaults ##
(defn default-console-appender
  "Returns a new instance of ConsoleAppender with a default PatternLayout for layout and a name of \"default\""
  ([] (console/console-appender "default" (pattern-layout)))
  ([name]
     (let [layout (pattern-layout)]
       (console/console-appender name layout))))

(defn default-swing-appender
  "Returns a new instance of SwingAppender."
  ([] (swing/swing-appender "swing"))
  ([name] (swing/swing-appender name))
  ([name component] (swing/swing-appender name component)))

;; ## Declarative API ##
(defn config-logger
  "Take a map of `opts` and configure your logger. If objects are passed in as values to keys, they are used as-is; if strings are passed in place of objects, then default-* functions will be called using the string as the first parameter."
  ([] (config-logger (root-logger) {}))
  ([opts] (config-logger (root-logger) opts))
  ([logger opts]
     (let [{:keys [appenders level]
            :or {appenders [(default-console-appender)]
                 level :debug}} opts]
       (doseq [appender appenders]
           (add-appender logger appender))
       (set-level logger level))))
