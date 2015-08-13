# MongoDB JUnit Test Rule

This library contains a simple JUnit rule that will bootstrap an embedded mongodb during junit integration test execution.

The library will use the embedded mongo framework from flapdoodle and will start a mongodb instance on localhost and port 27099. The port, host ip and mongodb version can be configured via fluent methods on `MongoRule`.


Simple Example: 

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
