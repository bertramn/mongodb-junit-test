package io.fares.junit.mongodb;

import de.flapdoodle.embed.mongo.distribution.IFeatureAwareVersion;

abstract class AbstractMongoExtensionBuilder<B extends AbstractMongoExtensionBuilder, E extends AbstractMongoExtension> {

  private E extension;

  protected AbstractMongoExtensionBuilder(E extension) {
    this.extension = extension;
  }

  /**
   * Set the host name on which the monogd instance will be run on
   *
   * @param host the machine name or an IP address
   * @return this builder
   */
  @SuppressWarnings("unchecked")
  public B host(String host) {
    extension.withMongoHost(host);
    return (B) this;
  }

  /**
   * Set the mongod port on which the database will listen
   *
   * @param port the port number
   * @return this builder
   */
  @SuppressWarnings("unchecked")
  public B port(int port) {
    extension.withMongoPort(port);
    return (B) this;
  }

  /**
   * Specify the exact version of the monogdb instance to be started.
   *
   * @param version the version as valid mongodb version number
   * @return this builder
   */
  @SuppressWarnings("unchecked")
  public B version(String version) {
    extension.setVersion(version);
    return (B) this;
  }

  /**
   * Specify the exact version of the monogdb instance to be started.
   *
   * @param version the version as valid mongodb version number
   * @return this builder
   */
  @SuppressWarnings("unchecked")
  public B version(IFeatureAwareVersion version) {
    extension.setVersion(version);
    return (B) this;
  }

  /**
   * Specify the exact version of the monogdb instance to be started.
   *
   * @param version   the version as valid mongodb version number
   * @param syncDelay true if the download is to be retrieved with sync delay
   * @return this builder
   */
  @SuppressWarnings("unchecked")
  public B version(String version, boolean syncDelay) {
    extension.setVersion(version, syncDelay);
    return (B) this;
  }

  public E build() {
    return extension;
  }

}
