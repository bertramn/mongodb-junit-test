package io.fares.junit.mongodb;

import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.distribution.IFeatureAwareVersion;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static org.junit.platform.commons.support.AnnotationSupport.isAnnotated;

public class MongoExtension extends MongoTestBase implements BeforeEachCallback, AfterEachCallback {

  public static final String UNIT_TEST_DB = "unitdb";

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    if (isMongoEnabled(context)) {
      startMongo();
    }
  }

  @Override
  public void afterEach(ExtensionContext context) {
    shutdownMongo();
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

  public static Builder builder() {
    return new Builder();
  }

  public static MongoExtension defaultMongo() {
    return new Builder().build();
  }

  public static final class Builder {

    private MongoExtension extension;

    private Builder() {
      this.extension = new MongoExtension();
    }

    /**
     * Set the host name on which the monogd instance will be run on
     *
     * @param host the machine name or an IP address
     * @return this builder
     */
    public Builder host(String host) {
      extension.withMongoHost(host);
      return this;
    }

    /**
     * Set the mongod port on which the database will listen
     *
     * @param port the port number
     * @return this builder
     */
    public Builder port(int port) {
      extension.withMongoPort(port);
      return this;
    }

    /**
     * Specify the exact version of the monogdb instance to be started.
     *
     * @param version the version as valid mongodb version number
     * @return this builder
     */
    public Builder version(String version) {
      extension.setVersion(version);
      return this;
    }

    /**
     * Specify the exact version of the monogdb instance to be started.
     *
     * @param version the version as valid mongodb version number
     * @return this builder
     */
    public Builder version(IFeatureAwareVersion version) {
      extension.setVersion(version);
      return this;
    }



    /**
     * Specify the exact version of the monogdb instance to be started.
     *
     * @param version   the version as valid mongodb version number
     * @param syncDelay true if the download is to be retrieved with sync delay
     * @return this builder
     */
    public Builder version(String version, boolean syncDelay) {
      extension.setVersion(version, syncDelay);
      return this;
    }

    public MongoExtension build() {
      return extension;
    }

  }


}
