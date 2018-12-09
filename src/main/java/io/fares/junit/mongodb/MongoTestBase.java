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
import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.config.RuntimeConfigBuilder;
import de.flapdoodle.embed.mongo.distribution.Feature;
import de.flapdoodle.embed.mongo.distribution.IFeatureAwareVersion;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.distribution.Versions;
import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.distribution.GenericVersion;
import de.flapdoodle.embed.process.runtime.Network;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.UnknownHostException;

public abstract class MongoTestBase {

  private Logger log = LoggerFactory.getLogger(getClass());

  private IFeatureAwareVersion version = Version.Main.PRODUCTION;

  private String mongodHost = "localhost";
  private int mongodPort = 27099;

  protected MongodExecutable mongoExec = null;
  protected MongodProcess mongod = null;
  protected MongoClient mongoClient = null;

  /**
   *
   * Starts a mongodb instance and returns a working client to it.
   *
   * @return the client that can connect to the working instance
   *
   * @throws UnknownHostException the provided host and port is not available
   * @throws IOException          some low level error connecting the the mongo db failed
   */
  protected MongoClient startMongo() throws UnknownHostException, IOException {

    // start embedded mongo
    IRuntimeConfig runConfig = new RuntimeConfigBuilder().defaultsWithLogger(Command.MongoD, log).build();

    MongodStarter runtime = MongodStarter.getInstance(runConfig);

    IMongodConfig mongodConfig = new MongodConfigBuilder().version(version)
      .net(new Net(mongodPort, Network.localhostIsIPv6())).build();

    mongoExec = runtime.prepare(mongodConfig);
    mongod = mongoExec.start();

    int waitCount = 1;
    while (!mongod.isProcessRunning() || waitCount > 10) {
      try {
        if (log.isDebugEnabled()) {
          log.debug("Force wait start {}", waitCount);
        }
        waitCount++;
        Thread.sleep(1000);
      } catch (InterruptedException ignore) {
      }
    }

    // setup the client for the test
    mongoClient = new MongoClient(getMongoHost(), getMongoPort());

    return this.mongoClient;

  }

  protected void shutdownMongo() {

    if (mongod != null) {
      mongod.stop();

      int waitCount = 1;
      while (mongod.isProcessRunning() || waitCount > 10) {
        try {
          if (log.isDebugEnabled()) {
            log.debug("Force wait shutdown {}", waitCount);
          }
          waitCount++;
          Thread.sleep(1000);
        } catch (InterruptedException ignore) {
        }
      }

    }

    if (mongoExec != null)
      mongoExec.stop();


  }

  public String getMongoHost() {
    return mongodHost;
  }

  protected void setMogoHost(String host) {
    this.mongodHost = host;
  }

  public int getMongoPort() {
    return mongodPort;
  }

  protected void setMogoPort(int port) {
    this.mongodPort = port;
  }

  protected MongoClient getMongoClient() {
    return mongoClient;
  }

  protected MongoTestBase withMongoHost(String host) {
    setMogoHost(host);
    return this;
  }

  protected MongoTestBase withMongoPort(int port) {
    setMogoPort(port);
    return this;
  }

  public String getVersion() {
    return version == null ? Version.Main.PRODUCTION.asInDownloadPath() : version.asInDownloadPath();
  }

  public void setVersion(String version) {
    setVersion(version, true);
  }

  public void setVersion(String version, boolean syncDelay) {
    if (syncDelay) {
      this.version = Versions.withFeatures(new GenericVersion(version), Feature.SYNC_DELAY);
    } else {
      this.version = Versions.withFeatures(new GenericVersion(version));
    }
  }

  public MongoTestBase withVersion(String version) {
    setVersion(version);
    return this;
  }

  public MongoTestBase withVersion(String version, boolean syncDelay) {
    setVersion(version, syncDelay);
    return this;
  }

}
