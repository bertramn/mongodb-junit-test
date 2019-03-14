/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.fares.junit.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MongoForAllExtensionIntegrationTestExample {

  private static final String COLLECTION_NAME = "TestCollection";

  @RegisterExtension
  static MongoForAllExtension mongo = MongoForAllExtension.defaultMongo();

  @BeforeEach
  void setupCollection() {
    MongoClient client = mongo.getMongoClient();
    MongoDatabase db = client.getDatabase(MongoExtension.UNIT_TEST_DB);
    MongoCollection<Document> collection = db.getCollection(COLLECTION_NAME);
    collection.drop();
  }

  @Test
  public void testSomethingWithMongoRule() {
    MongoClient client = mongo.getMongoClient();
    assertNotNull(client);
    // whatever needs to be done in mongo using the provided mongo client
  }

  @Test
  public void testSomethingElseWithMongoRule() {
    assertTrue(mongo.isStarted());
    MongoClient client = mongo.getMongoClient();
    assertNotNull(client);
    // whatever needs to be done in the same database
  }

}
