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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.AnnotatedElement;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MongoForEachExtensionIntegrationTest {

  @Mock
  ExtensionContext context;

  @Spy
  MongoForEachExtension extension = MongoForEachExtension.defaultMongo();

  @Test
  public void itShouldStartBefore() throws Exception {
    extension.beforeEach(context);
    MongoClient client = extension.getMongoClient();
    assertNotNull(client);
    extension.shutdownMongo();
    verify(extension, times(1)).beforeEach(eq(context));
    verify(extension, times(1)).startMongoWhenEnabled(eq(context));
    verify(extension, times(1)).startMongo();
    verify(extension, times(1)).getMongoHost();
    verify(extension, times(1)).getMongoPort();
    verify(extension, times(1)).getMongoClient();
    verify(extension, times(1)).shutdownMongo();
    verifyNoMoreInteractions(extension);
  }

  @Test
  public void itShouldNotStartIfDisabled() throws Exception {
    WithoutMongo ann = mock(WithoutMongo.class);
    AnnotatedElement element = mock(AnnotatedElement.class);
    when(element.getDeclaredAnnotation(eq(WithoutMongo.class))).thenReturn(ann);
    when(context.getElement()).thenReturn(Optional.of(element));
    extension.beforeEach(context);
    extension.afterEach(context);
    verify(extension, times(1)).startMongoWhenEnabled(eq(context));
    verify(extension, times(1)).beforeEach(eq(context));
    verify(extension, times(1)).afterEach(eq(context));
    verify(extension, times(1)).stopMongoWhenEnabled(eq(context));
    verifyNoMoreInteractions(extension);
  }

  @Test
  public void itShouldStopAfter() throws Exception {
    extension.startMongo();
    extension.afterEach(context);
    verify(extension, times(1)).startMongo();
    verify(extension, times(1)).afterEach(eq(context));
    verify(extension, times(1)).getMongoHost();
    verify(extension, times(1)).getMongoPort();
    verify(extension, times(1)).stopMongoWhenEnabled(eq(context));
    verify(extension, times(1)).shutdownMongo();
    verifyNoMoreInteractions(extension);
  }

}
