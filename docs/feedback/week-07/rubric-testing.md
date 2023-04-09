# Rubric: Testing

### Coverage

Testing is an integral part of the coding activities. Unit tests cover all parts of the application (client, server, commons). Excellent teams will also pay attention to the (unit test) code coverage of crucial system components (accordind to the build result over all components).

- Excellent: The project reaches a code coverage of more than 80%.
- Good: All conceptual parts of the application have several automated tests (client, server, commons).
- Sufficient: The average MR does not only contain business logic, but also unit tests.
- Insufficient: The application is barely tested or tests are only added as an afterthought.

For this, we would give ourselves a good since we have a current line test coverage of about 100% in commons, 6% in client and 65% in server. The most significant merge requests contributing to these coverages were !71 for client, !61 for commons, as well as !28 and !31 for server.


### Unit Testing

Classes are tested in isolation. Configurable *dependent-on-components* are passed to the *system-under-test* to avoid integration tests (for example, to avoid running a database or opening REST requests in each a test run).

- Excellent: Configurable subclasses are created to replace dependent-on-components in most of the tests.
- Good: The project contains some examples, in which configurable subclasses are created to replace dependent-on-components in the tests.
- Sufficient: The project contains unit tests, but only for classes without dependent-on-components.
- Insufficient: The project only contains integration tests, which test multiple components at once.

We would give ourselves an excellent for this, since we ensured all of the classes are tested in isolation using mock classes for controllers/repositories. For example, this can be seen in merge request !71 for client, as well as merge requests !28 and !31 for server.

### Indirection

The project applies the test patterns that have been covered in the lecture on *Dependency Injection*. More specifically, the test suite includes tests for indirect input/output and behavior.

- Excellent: The project contains tests that assert indirect input, indirect output, and behavior for multiple systems-under-test.
- Good: The project contains at least one test for each of the following assert types: 1. indirect input, 2. indirect output, and 3. behavior.
- Sufficient: The project contains at least one exemplary test that goes beyond asserting direct input/output of a system-under-test. For example, by asserting indirect input, indirect output, or behavior.
- Insufficient: The project does not test or only tests direct input/output.

We would give ourselves a good for this, since we test for indirect input/output and behaviour in multiple tests. This can mostly be seen in the server side tests, of which a significant portion was added in merge requests !28 and !31.

### Endpoint Testing

The REST API is tested through automated JUnit tests.

- Excellent: The project contains automated tests that cover regular and exceptional use of most endpoints.
- Good: The project contains automated JUnit tests that cover the regular use of multiple endpoints.
- Sufficient: The project contains one automated JUnit test that covers one use of one endpoint.
- Insufficient: The project does not use JUnit to test the REST API.

We would give ourselves a good for this, since we test a significant portion (about 65%) of the server side, covering the regular use of multiple endpoints. Again, this can be seen mostly in merge requests !28 and !31. 
