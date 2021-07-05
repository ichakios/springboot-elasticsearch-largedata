# springboot-elasticsearch-largedata
## _Large Data Generator Built In_

Pre-requisites.

- Postman for testing.
- Elastic Server installed as docker or as singleton server
- Create a databse in ES called test and an index named tag
## Features

- Generating 2,400,000 Records in the tag collection when the spring boot first run.
- Exposing 3 APIs (count, Search By User UUID, Get Tags by ContentId)


## Installation

This example requires the below to be installed:
- Maven
- Elastic Search Server (Latest)
- Java 8+
- Postman

Clone this repository and build the source code using maven or your favorite IDE.
Make sure you have a running ES contains a database named test and an index inside the test db named tag.
run your spring boot, it should takes time for the first time since we are generating 2,400,000 record.
after that use the postman collection _Spring-ES.postman_collection.json_ (located inside the project) to test your APIs
