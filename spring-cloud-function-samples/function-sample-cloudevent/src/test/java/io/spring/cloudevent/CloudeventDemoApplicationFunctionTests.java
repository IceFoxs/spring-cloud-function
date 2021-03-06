/*
 * Copyright 2020-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.cloudevent;

import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.function.cloudevent.CloudEventMessageBuilder;
import org.springframework.cloud.function.cloudevent.CloudEventMessageUtils;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

/**
 *
 * @author Oleg Zhurakousky
 *
 */
public class CloudeventDemoApplicationFunctionTests {

	@Test
	public void demoPureFunctionInvocation() {

		try(ConfigurableApplicationContext context = new SpringApplicationBuilder(CloudeventDemoApplication.class)
				.web(WebApplicationType.NONE).run()) {
			FunctionCatalog catalog = context.getBean(FunctionCatalog.class);

			Message<String> inputMessage = CloudEventMessageBuilder
					.withData("{\"releaseDate\":\"24-03-2004\", \"releaseName\":\"Spring Framework\", \"version\":\"1.0\"}")
					.setSource("https://spring.io/spring-event")
					.setType("com.example.springevent")
					.build();

			/*
			 * NOTE how it makes no difference what the actual function signature
			 * is (see `asPOJOMessage` and `asPOJO` specifically). Type conversion will happen
			 * inside spring-cloud-function.
			 */
			Function<Message<String>, Message<String>> asPojoMessage = catalog.lookup("asPOJOMessage");
			System.out.println(asPojoMessage.apply(inputMessage));

			Function<Message<String>, Message<String>> asPojo = catalog.lookup("asPOJO");
			System.out.println(asPojo.apply(inputMessage));

			Function<Message<String>, Message<String>> asString = catalog.lookup("asString");
			System.out.println(asString.apply(inputMessage));

			Function<Message<String>, Message<String>> asStringMessage = catalog.lookup("asStringMessage");
			System.out.println(asStringMessage.apply(inputMessage));
		}
	}

	@Test
	public void demoPureFunctionProduceConsumeCloudEvent() {
		try(ConfigurableApplicationContext context = new SpringApplicationBuilder(CloudeventDemoApplication.class)
				.web(WebApplicationType.NONE).run()) {
			FunctionCatalog catalog = context.getBean(FunctionCatalog.class);

			Message<String> inputMessage = CloudEventMessageBuilder
					.withData("{\"releaseDate\":\"24-03-2004\", \"releaseName\":\"Spring Framework\", \"version\":\"1.0\"}")
					.setSource("https://spring.io/spring-event")
					.setType("com.example.springevent")
					.build();

			/*
			 * NOTE how it makes no difference what the actual function signature
			 * is (see `asPOJOMessage` and `asPOJO` specifically). Type conversion will happen
			 * inside spring-cloud-function.
			 */
			Function<Message<String>, Message<String>> asPojoMessage = catalog.lookup("consumeAndProduceCloudEvent");
			System.out.println(asPojoMessage.apply(inputMessage));
		}
	}

	@Test
	public void demoPureFunctionProduceConsumeCloudEventAsPojo() {
		try(ConfigurableApplicationContext context = new SpringApplicationBuilder(CloudeventDemoApplication.class)
				.web(WebApplicationType.NONE).run()) {
			FunctionCatalog catalog = context.getBean(FunctionCatalog.class);

			Message<String> inputMessage = CloudEventMessageBuilder
					.withData("{\"releaseDate\":\"24-03-2004\", \"releaseName\":\"Spring Framework\", \"version\":\"1.0\"}")
					.setSource("https://spring.io/spring-event")
					.setType("com.example.springevent")
					.build();

			/*
			 * NOTE how it makes no difference what the actual function signature
			 * is (see `asPOJOMessage` and `asPOJO` specifically). Type conversion will happen
			 * inside spring-cloud-function.
			 */
			Function<Message<String>, Message<String>> asPojoMessage = catalog.lookup("consumeAndProduceCloudEventAsPojoToPojo");
			System.out.println(asPojoMessage.apply(inputMessage));
		}
	}
}
