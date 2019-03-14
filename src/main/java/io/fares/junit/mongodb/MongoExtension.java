package io.fares.junit.mongodb;

import com.mongodb.MongoClient;

public interface MongoExtension {

  String UNIT_TEST_DB = "unitdb";

  /**
   * Returns a handle to the mongo database that was started.
   *
   * @return the configured mongo client or <code>null</code> if disabled
   * @see #isStarted()
   */
  MongoClient getMongoClient();

  /**
   * @return <code>true</code> if the mongo database is started <code>false</code> otherwise
   */
  boolean isStarted();

}
