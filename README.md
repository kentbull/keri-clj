# keri-clj

The Clojure implementation of the [KERI](https://trustoverip.github.io/tswg-keri-specification/draft-ssmith-keri.html) and [ACDC](https://trustoverip.github.io/tswg-acdc-specification/draft-ssmith-acdc.html) protocols
based on the Python reference implementation [KERIpy](https://github.com/WebOfTrust/keripy) and [KERIA](https://github.com/WebOfTrust/keria).

Client libraries will eventually include a SignifyCLJ styled after the Typescript client library [SignifyTS](https://github.com/WebOfTrust/signify-ts).

For reference see the [KERI whitepaper](https://github.com/SmithSamuelM/Papers/blob/master/whitepapers/KERI_WP_2.x.web.pdf)

## Installation

Download from https://github.com/kentbull/keri-clj

## Usage

TODO: Fix these instructions. They are the default instructions. Add in the first CLI command (KLI) to initialize the keystore for an identifier. 
Then show another example of creating a cryptographic salt.

Run the project directly, via `:exec-fn`:

    $ clojure -X:run-x
    Hello, KERI CLJ!    

Run the project directly, via `:main-opts` (`-m keri.keri-clj`):

    $ clojure -M:run-m
    Hello, KERI CLJ!

Run the project's tests (they'll fail until you edit them):

    $ clojure -T:build test

Run the project's CI pipeline and build an uberjar (this will fail until you edit the tests to pass):

    $ clojure -T:build ci

This will produce an updated `pom.xml` file with synchronized dependencies inside the `META-INF`
directory inside `target/classes` and the uberjar in `target`. You can update the version (and SCM tag)
information in generated `pom.xml` by updating `build.clj`.

If you don't want the `pom.xml` file in your project, you can remove it. The `ci` task will
still generate a minimal `pom.xml` as part of the `uber` task, unless you remove `version`
from `build.clj`.

Run that uberjar:

    $ java -jar target/keri-clj-0.1.0-SNAPSHOT.jar

## Options

greet

### Future options

- init
- incept
- salt


## Examples

...

### Bugs

...

### Libraries
[clj-matic](https://github.com/l3nz/cli-matic)

## License

Copyright © 2023 Kent Bull

_Licensed under the Apache License version 2.0

