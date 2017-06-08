# Tweets: a dev assignment

This repo aims at solving a development [assignment](./assignment.md) using Spring Boot + Webflux. Leveraging reactive streams allows for potential scaling of the app far beyond the requirements of the assignment.

## Solution

Although the assignment only processes a very limited amount of data, the solution aims at providing a baseline for scalabilty. Using reactive streams allows for arbitrary large data sets to be processed. In this case, the amount of followers and applicable tweets for a user can easily reach in the millions resulting in standard relational `Collections` to be problematic. Another option would have been to use paging, but reactive stream have a range of properties making it a much better choice.
   
Some notes:
- The solution implements `application/stream+json` REST endpoints, by which one can query the data.
- The app is bootstrapped with the assignment's test data on startup, see the [ApplicationRunners](src/main/java/io/springtide/tweets/runners).
- The assignment requires a certain console output, this is present in the output of an [integration test](src/test/java/io/springtide/tweets/TweetsApplicationTests.java) since the actual rendering of the data in a specific format can be argued to be client specific.
- The repositories are designed to be easily split into micro services.
- Reactive streams was only implemented for reading, since writing does not require scalabilty at this point. 

## Getting Started  

Check of the repo and run the tests to see the required console output:
~~~~

./mvnw test 
(in Linux)

~~~~
~~~~
mvnw.bat test
(in Windows)

~~~~

## TODO

Some more work is required/desired:
- Split the "follower" relationships out of the `UserRepository` into it's own repository.
- If a repository uses data from other repositories, only reference it via `id`.
- Replace the custom (assignment specific) repositories with a proper JPA reactive datastore, like MongoDB.
