# Phone shop

This project consists in 2 REST APIs totally isolated (microservices way): inventory-management + order-processor.

* **inventory-management**. Manages the phone catalog and offers 2 endpoints:

    - Search all phones in the catalog
    - Search a phone by id in the catalog

* **order-processor**. Manages the orders matters and offers 2 endpoints:

    - Create an order
    - Search an order by id


### Notes

- The project is structured in 2 sbt modules with a common root for managing library dependencies and building.
- Communication between the two services is via http
- Each microservice has its own database (microservices way)
- It doesn't implement inventory control (number of items in stock) when request an order.



### Built With

* [scala](https://www.scala-lang.org/) - The programming language
* [play framework](https://www.playframework.com/) - The web framework
* [slick](http://slick.lightbend.com/) - The FRM framework library
* [scala-guice](https://github.com/codingwell/scala-guice) - The DI framework
* [postgresql](https://www.postgresql.org/) - The database in production
* [h2database](http://www.h2database.com/html/main.html) - The database for testing
* [scalatest](http://www.scalatest.org/) - The library for testing
* [mockito](https://site.mockito.org/) - The mocking framework for testing
* [logback](https://logback.qos.ch/) - The logging producer
* [sbt](https://www.scala-sbt.org/) - The build tool
* [docker](https://www.docker.com/) - The deployment container platform
* [love](https://en.wikipedia.org/wiki/Love) - The basis


### Prerequisites

* scala (2.12.6) (building)
* sbt (1.1.6) (building)
* java 8 (building)
* docker-native (deploying)
* boot2docker hosts entry  ```127.0.0.1   boot2docker```
* httpie (http console client)


### Installing

For building and deploying the two apis on a local docker, needs to:

Move into the root project folder

```
$ cd ../phone-shop
```

Build the artifacts

```
$ ../phone-shop$./docker-build.sh
```

Deploy the artifacts on docker

```
$ ../phone-shop$./launch.sh
```


### Running the phone shop

Move into folder `json` in root project folder

```
$ cd ../phone-shop/json
```

**Listing the catalog**

```
$ ../phone-shop/json$ http boot2docker:9000/v1/phones
```
```
HTTP/1.1 200 OK
Content-Length: 467
Content-Security-Policy: default-src 'self'
Content-Type: application/json
Date: Wed, 03 Oct 2018 12:53:04 GMT
Referrer-Policy: origin-when-cross-origin, strict-origin-when-cross-origin
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-Permitted-Cross-Domain-Policies: master-only
X-XSS-Protection: 1; mode=block

[
    {
        "description": "Mobile phone 4G with 10 Mpx. Not bad.",
        "id": 1,
        "imageUrl": "http://boot2docker:9000/assets/images/moto-g-2014-1.jpg",
        "name": "Moto G2",
        "price": 150.52
    },
    {
        "description": "Mobile phone 4G very expensive",
        "id": 2,
        "imageUrl": "http://boot2docker:9000/assets/images/iphone_x.jpeg",
        "name": "iPhone X",
        "price": 1100.32
    },
    {
        "description": "Mobile phone 4G from Sony",
        "id": 3,
        "imageUrl": "http://boot2docker:9000/assets/images/xperia_l1.jpg",
        "name": "Xperia L1",
        "price": 200
    }
]
```

**Searching a cataloged phone**

```
$ ../phone-shop/json$ http boot2docker:9000/v1/phones/1
```
```
HTTP/1.1 200 OK
Content-Length: 163
Content-Security-Policy: default-src 'self'
Content-Type: application/json
Date: Wed, 03 Oct 2018 12:57:49 GMT
Referrer-Policy: origin-when-cross-origin, strict-origin-when-cross-origin
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-Permitted-Cross-Domain-Polici>es: master-only
X-XSS-Protection: 1; mode=block

{
    "description": "Mobile phone 4G with 10 Mpx. Not bad.",
    "id": 1,
   "imageUrl": "http://boot2docker:9000/assets/images/moto-g-2014-1.jpg",
   "name": "Moto G2",
    "price": 150.52
}
```

**Searching a non cataloged phone**

```
../phone-shop/json$ http boot2docker:9000/v1/phones/111
```
```
HTTP/1.1 404 Not Found
Content-Length: 43
Content-Security-Policy: default-src 'self'
Content-Type: application/json
Date: Wed, 03 Oct 2018 12:59:03 GMT
Referrer-Policy: origin-when-cross-origin, strict-origin-when-cross-origin
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-Permitted-Cross-Domain-Policie>s: master-only
X-XSS-Protection: 1; mode=block>

{
    "message": "Phone not found with id = 111"
}
```

**Creating a valid order**

```
../phone-shop/json$ http POST boot2docker:9003/v1/orders < order.json
```
```
HTTP/1.1 201 Created
Content-Length: 62
Content-Security-Policy: default-src 'self'
Content-Type: application/json
Date: Wed, 03 Oct 2018 13:00:12 GMT
Referrer-Policy: origin-when-cross-origin, strict-origin-when-cross-origin
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-Permitted-Cross-Domain-Policies: master-only
X-XSS-Protection: 1; mode=block>

{
    "location": "http://boot2docker:9003/v1/orders/2",
    "orderId": 2
}
```

**Searching an order**

```
../phone-shop/json$ http boot2docker:9000/v1/orders/2
```
```
HTTP/1.1 404 Not Found
Content-Length: 0
Content-Security-Policy: default-src 'self'
Content-Type: text/plain; charset=UTF-8
Date: Wed, 03 Oct 2018 13:01:48 GMT
Referrer-Policy: origin-when-cross-origin, strict-origin-when-cross-origin
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-Permitted-Cross-Domain-Policies: master-only
X-XSS-Protection: 1; mode=block
```


**Searching a non existed order**

```
../phone-shop/json$ http boot2docker:9000/v1/orders/5
```
```
HTTP/1.1 404 Not Found
Content-Length: 0
Content-Security-Policy: default-src 'self'
Content-Type: text/plain; charset=UTF-8
Date: Wed, 03 Oct 2018 13:03:57 GMT
Referrer-Policy: origin-when-cross-origin, strict-origin-when-cross-origin
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-Permitted-Cross-Domain-Policies: master-only
X-XSS-Protection: 1; mode=block
```

**Creating a invalid valid order**

```
../phone-shop/json$ http POST boot2docker:9003/v1/orders < order.json
```
```
HTTP/1.1 400 Bad Request
Content-Length: 52
Content-Security-Policy: default-src 'self'
Content-Type: application/json
Date: Wed, 03 Oct 2018 14:05:52 GMT
Referrer-Policy: origin-when-cross-origin, strict-origin-when-cross-origin
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-Permitted-Cross-Domain-Policies: master-only
X-XSS-Protection: 1; mode=block

{
    "exception": "Phones not found in catalog: Set(25)"
}
```

## Running the tests

Each project has its own suite with unit and functional tests.
To run the tests move into root project folder and run ´´´sbt run´´´.

Here's **inventory-management** results:
```
[info] ScalaTest
[info] Run completed in 3 seconds, 968 milliseconds.
[info] Total number of tests run: 8
[info] Suites: completed 3, aborted 0
[info] Tests: succeeded 8, failed 0, canceled 0, ignored 0, pending 0
[info] All tests passed.
[info] Passed: Total 8, Failed 0, Errors 0, Passed 8
[success] Total time: 14 s, completed Oct 3, 2018
```

Here's **order-processor** results:
```
[info] ScalaTest
[info] Run completed in 4 seconds, 632 milliseconds.
[info] Total number of tests run: 15
[info] Suites: completed 3, aborted 0
[info] Tests: succeeded 15, failed 0, canceled 0, ignored 0, pending 0
[info] All tests passed.
[info] Passed: Total 15, Failed 0, Errors 0, Passed 15
[success] Total time: 6 s, completed Oct 3, 2018
```

## Logging

To checking out the **inventory-management** logs:

```
$ docker logs -f inventory
```

To check out the **inventory-management** logs:

```
$ docker logs -f orderProcessor
```


## Author

* **Jose Velasco** - *jotason.velasco@gmail.com* - [Linkedin](https://www.linkedin.com/in/jose-velasco-developer/)

# phone_shop
# phone_shop
# phone_shop
