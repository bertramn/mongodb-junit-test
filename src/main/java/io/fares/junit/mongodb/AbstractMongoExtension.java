package io.fares.junit.mongodb;

import com.mongodb.MongoClient;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;

import static org.junit.platform.commons.support.AnnotationSupport.isAnnotated;

abstract class AbstractMongoExtension extends MongoTestBase implements MongoExtension {

  protected void startMongoWhenEnabled(ExtensionContext context) throws IOException {
    if (isMongoEnabled(context)) {
      startMongo();
    }
  }

  protected void stopMongoWhenEnabled(ExtensionContext context) throws IOException {
    if (isMongoEnabled(context)) {
      shutdownMongo();
    }
  }

  /**
   * @return <code>true</code> if the mongo database is started <code>false</code> otherwise
   */
  @Override
  public boolean isStarted() {
    return super.getMongoClient() != null;
  }

  /**
   * @return a functioning mongodb client that can connect to the started mongod process
   */
  public MongoClient getMongoClient() {
    return super.getMongoClient();
  }

  private boolean isMongoEnabled(ExtensionContext context) {
    return !context.getElement()
      .map(el -> isAnnotated(el, WithoutMongo.class))
      .orElse(false);
  }

}
