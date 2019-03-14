# MongoDB JUnit Test Rule

This library contains a simple JUnit rule that will bootstrap an embedded mongodb during junit integration test execution.

The library will use the embedded mongo framework from flapdoodle and will start a mongodb instance on localhost and port 27099. The port, host ip and mongodb version can be configured via fluent methods on `MongoRule`.


Simple Example for JUnit4: 

```java
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;

import com.mongodb.MongoClient;

public class SomeTestWithMongoDB {

  @Rule
  // this will start and stop mongodb for every test
  public MongoRule mongo = new MongoRule();

  @Test
  public void testSomethingWithMongoRule() {

    MongoClient client = mongo.getMongoClient();
    assertNotNull(client);
    // whatever needs to be done in mongo using the provided monog client

  }

  @Test
  @WithoutMongo
  public void testSomethingWithoutMongoRule() {

    MongoClient client = mongo.getMongoClient();
    assertNull(client);
    // whatever needs to be done without mongo

  }

}
```

Simple Example for JUnit5 starting a MongoDB for All Tests (only once):

```java
public class SomeTestWithMongoDB {

  @RegisterExtension
  static MongoForAllExtension mongo = MongoForAllExtension.defaultMongo();

  @BeforeEach
  void setupCollection() {
    MongoDatabase db = mongo.getMongoClient().getDatabase(MongoExtension.UNIT_TEST_DB);
    // e.g. drop a collection taht was used during prior test
    db.getCollection("TestCollection").drop();
  }

  @Test
  public void testSomethingWithMongoDB() {
    MongoClient client = mongo.getMongoClient();
    // whatever needs to be done in mongo using the provided mongo client
  }

  @Test
  public void testSomethingElseWithSameMongoDB() {
    MongoClient client = mongo.getMongoClient();
    // whatever needs to be done in mongo using the provided mongo client
  }

}
```

Simple Example for JUnit5 starting a MongoDB for Each Test (one per test case - slow):

```java
public class SomeTestWithMongoDB {

  @RegisterExtension
  MongoForEachExtension mongo = MongoForEachExtension.defaultMongo();

  @BeforeEach
  void setupCollection() {
    if (mongo.isStarted()) {
      MongoDatabase db = mongo.getMongoClient().getDatabase(MongoExtension.UNIT_TEST_DB);
      // setup collection for test
    }
  }

  @Test
  public void testSomethingWithMongoRule() {
    MongoClient client = mongo.getMongoClient();
    // whatever needs to be done in mongo using the provided mongo client
  }

  @Test
  @WithoutMongo
  public void testSomethingWithoutMongoRule() {
    // whatever needs to be done without mongo
  }
  
}
```
