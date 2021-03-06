/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bd.com.sagar.rest.pubsub;

import com.google.api.gax.rpc.ApiException;
import com.google.cloud.ServiceOptions;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.pubsub.v1.SubscriptionAdminClient;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.PushConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingDeque;

import javax.annotation.PostConstruct;

@Component
public class SubscriberExample {

    public static final String PROJECT_ID = ServiceOptions.getDefaultProjectId();

    public static final String TOPIC_ID = "delete-json-topic";

    private static final BlockingQueue<PubsubMessage> messages = new LinkedBlockingDeque<>();

    @Value("${app.notification}")
    private String notification;

    @PostConstruct
    protected void init() {
        if (notification.equals("on")) {
            String subscriptionId = "delete-json-subscription";
            CompletableFuture.runAsync(() -> {
                createSubscription(subscriptionId);
                getMessage(subscriptionId);
            });
        }
    }

    public void createSubscription(String subscriptionId) {
        ProjectTopicName topicName = ProjectTopicName.of(PROJECT_ID, TOPIC_ID);
        ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(PROJECT_ID, subscriptionId);
        try (SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create()) {
            subscriptionAdminClient.createSubscription(subscriptionName, topicName, PushConfig.getDefaultInstance(), 0);
        } catch (ApiException e) {
            System.out.println("status code -> "+ e.getStatusCode().getCode());
            System.out.println("retryable -> " + e.isRetryable());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        System.out.printf("Subscription %s:%s created.\n", subscriptionName.getProject(),
                subscriptionName.getSubscription());
    }

    static class MessageReceiverExample implements MessageReceiver {
        @Override
        public void receiveMessage(PubsubMessage message, AckReplyConsumer consumer) {
            messages.offer(message);
            consumer.ack();
        }
    }

    public void getMessage(String subscriptionId) {
        ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(PROJECT_ID, subscriptionId);
        Subscriber subscriber = null;
        try {
            subscriber = Subscriber.newBuilder(subscriptionName, new MessageReceiverExample()).build();
            subscriber.startAsync().awaitRunning();
            while (true) {
                PubsubMessage message = messages.take();
                System.out.println("Message Id: " + message.getMessageId());
                System.out.println("Data: " + message.getData().toStringUtf8());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (subscriber != null) {
                subscriber.stopAsync();
            }
        }
    }
}
