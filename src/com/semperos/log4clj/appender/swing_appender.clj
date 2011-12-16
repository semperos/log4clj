(ns com.semperos.log4clj.appender.swing-appender
  (:import org.apache.log4j.AppenderSkeleton)
  (:gen-class :name com.semperos.log4clj.appender.SwingAppender
              :extends org.apache.log4j.AppenderSkeleton))

(def target-component-obj (atom nil))

(defn target-component
  []
  @target-component-obj)

(defn target-component!
  [component]
  (reset! target-component-obj component))

(defn swing-appender
  ([] (com.semperos.log4clj.appender.SwingAppender.))
  ([name] (doto (com.semperos.log4clj.appender.SwingAppender.)
            (.setName name)))
  ([name component]
     (target-component! component)
     (doto (com.semperos.log4clj.appender.SwingAppender.)
       (.setName name))))

(defn -append
  [_ event]
  (let [target @target-component-obj]
    (if (nil? target)
      (throw (NoSuchFieldException. "No Swing component has been configured for this appender. Please use the `com.semperos.log4clj.appender.swing-appender/target-component!` function to designate an appropriate Swing component as the target for this appenders log data."))
      (.append target (.getMessage event)))))

(defn -close [_])

(defn -requiresLayout [_] false)