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

import com.google.api.core.ApiFuture;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bd.com.sagar.rest.core.bean.EmployeeBean;

@Component
public class PublisherExample {

    @Value("${app.notification}")
    private String notification;

    public void publishNotification(EmployeeBean employeeBean) {
        if (!notification.equals("on")) {
            return;
        }
        String topicId = SubscriberExample.TOPIC_ID;
        ProjectTopicName topicName = ProjectTopicName.of(SubscriberExample.PROJECT_ID, topicId);
        Publisher publisher = null;
        List<ApiFuture<String>> futures = new ArrayList<>();

        try {
            publisher = Publisher.newBuilder(topicName).build();
            String message = "Employee Record deleted -> " + employeeBean.toString();
            ByteString data = ByteString.copyFromUtf8(message);
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();
            ApiFuture<String> future = publisher.publish(pubsubMessage);
            futures.add(future);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (publisher != null) {
                try {
                    publisher.shutdown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
