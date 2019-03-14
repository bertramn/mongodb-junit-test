package io.fares.junit.mongodb;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class MongoForAllExtension extends AbstractMongoExtension implements BeforeAllCallback, AfterAllCallback {

  @Override
  public void beforeAll(ExtensionContext context) throws Exception {
    startMongoWhenEnabled(context);
  }

  @Override
  public void afterAll(ExtensionContext context) throws Exception {
    stopMongoWhenEnabled(context);
  }

  public static Builder builder() {
    return new Builder();
  }

  public static MongoForAllExtension defaultMongo() {
    return new Builder().build();
  }

  public static final class Builder extends AbstractMongoExtensionBuilder<Builder, MongoForAllExtension> {

    private Builder() {
      super(new MongoForAllExtension());
    }

  }

}
