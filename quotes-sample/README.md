# aggro #

A sample BTC/USD quotes aggregator making use of [Akka framework](http://akka.io/).
The quotes are crawled from [BTC-e](https://btc-e.com/) and [BTER](https://bter.com/) public narket API.

### Prerequisites ###
 * JDK
 * [SBT](http://www.scala-sbt.org/)

### How to run ###

    git clone https://bitbucket.org/euk/aggro.git

    cd aggro

    sbt run

### API description ###

```
    GET /quotes
```

Returns all the quotes gathered up to time in a well-formed JSON

```javascript
   [
     {
       bid: 230.577,
       ask: 230.586,
       source: "BTC-E",
       timestamp: 1443101808000
     },
     {
       bid: 210,
       ask: 150,
       source: "BTER",
       timestamp: 1443101809358
     },
     ...
   ]
```