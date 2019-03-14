package io.fares.junit.mongodb;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class MongoForEachExtension extends AbstractMongoExtension implements BeforeEachCallback, AfterEachCallback {

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    startMongoWhenEnabled(context);
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    stopMongoWhenEnabled(context);
  }

  public static Builder builder() {
    return new Builder();
  }

  public static MongoForEachExtension defaultMongo() {
    return new Builder().build();
  }

  public static final class Builder extends AbstractMongoExtensionBuilder<Builder, MongoForEachExtension> {

    private Builder() {
      super(new MongoForEachExtension());
    }

  }


}
