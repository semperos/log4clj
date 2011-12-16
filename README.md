# Log4clj: Log4j DSL in Clojure

DSL is probably too gracious a term. Right now, this is just a thin layer of Clojure to allow configuring log4j loggers programmatically. The all-in-one entry-point is the `config-logger` function. Reading the source code is highly recommended.

I've built this to abstract out a custom appender that I wrote in Clojure to output log4j logs to a Swing app. For that reason, this library also comes with a SwingAppender class.

## Usage

### Bare-bones ###

```clj
(use 'com.semperos.log4clj.core)

(config-logger)
```

This sets up the root logger with a default `ConsoleAppender` with a level of `DEBUG`. For now, look at the source code to see what else I've exposed as Clojure functions.

### Adding Appenders ###

```clj
(config-logger {:appenders[ (default-swing-appender "swingy" my-swing-component)
                            (default-console-appender "consoley") ]})
```

This sets up the root logger with two appenders. The appenders both have custom names, so you can interact with them via the logger. The `my-swing-component` parameter should be something like a `JTextArea` where you intend the log data to be appended.

### Low-Level API ###

For now, read the source code in the core namespace to see what lower-level functions are available.


## License

Copyright (C) 2011 Daniel L. Gregoire (semperos)

Distributed under the Eclipse Public License, the same as Clojure.
