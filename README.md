# Log4clj: Log4j DSL in Clojure

DSL is probably too gracious a term. Right now, this is just a thin layer of Clojure to allow configuring log4j loggers programmatically.

I've built this to abstract out a custom appender that I wrote in Clojure to output log4j logs to a Swing app. For that reason, this library also comes with a SwingAppender class.

## Usage

```clj
(use 'com.semperos.log4clj.core)

(config-logger)
```

This sets up the root logger with a default ConsoleAppender with a level of DEBUG. For now, look at the source code to see what else I've exposed as Clojure functions.

## License

Copyright (C) 2011 Daniel L. Gregoire (semperos)

Distributed under the Eclipse Public License, the same as Clojure.
