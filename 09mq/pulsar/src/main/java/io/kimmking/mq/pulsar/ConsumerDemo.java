package io.kimmking.mq.pulsar;

import lombok.SneakyThrows;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.springframework.stereotype.Component;

@Component
public class ConsumerDemo {

    @SneakyThrows
    public void consume() {

        Consumer consumer = Config.createClient().newConsumer()
                .topic("my-topic")
                .subscriptionName("my-subscription")
                .subscribe();

        while (true) {
            // Wait for a message
            Message msg = consumer.receive();

            try {
                // Do something with the message
                System.out.printf("Message received from pulsar: %s \n", new String(msg.getData()));

                // Acknowledge the message so that it can be deleted by the message broker
                consumer.acknowledge(msg);
            } catch (Exception e) {
                // Message failed to process, redeliver later
                consumer.negativeAcknowledge(msg);
            }
        }

//
//        client.newConsumer()
//                .deadLetterPolicy(DeadLetterPolicy.builder().maxRedeliverCount(10)
//                 .deadLetterTopic("your-topic-name").build())
//                .subscribe();


//        Consumer consumer = client.newConsumer()
//                .topic("my-topic")
//                .subscriptionName("my-subscription")
//                .ackTimeout(10, TimeUnit.SECONDS)
//                .subscriptionType(SubscriptionType.Exclusive)
//                .subscribe();


//
//        CompletableFuture<Message> asyncMessage = consumer.receiveAsync();
//
//        Messages messages = consumer.batchReceive();
//        for (Object message : messages) {
//            // do something
//        }
//        consumer.acknowledge(messages);
//
//        BatchReceivePolicy.builder()
//                .maxNumMessage(-1)
//                .maxNumBytes(10 * 1024 * 1024)
//                .timeout(100, TimeUnit.MILLISECONDS)
//                .build();
//
//        Consumer consumer = client.newConsumer().topic("my-topic").subscriptionName("my-subscription")
//                .batchReceivePolicy(BatchReceivePolicy.builder().
//                        maxNumMessages(100).maxNumBytes(1024 * 1024)
//                        .timeout(200, TimeUnit.MILLISECONDS)
//                .build()).subscribe();


//
//        ConsumerBuilder consumerBuilder = pulsarClient.newConsumer()
//                .subscriptionName(subscription);
//
//// Subscribe to all topics in a namespace
//        Pattern allTopicsInNamespace = Pattern.compile("public/default/.*");
//        Consumer allTopicsConsumer = consumerBuilder
//                .topicsPattern(allTopicsInNamespace)
//                .subscribe();
//
//// Subscribe to a subsets of topics in a namespace, based on regex
//        Pattern someTopicsInNamespace = Pattern.compile("public/default/foo.*");
//        Consumer allTopicsConsumer = consumerBuilder
//                .topicsPattern(someTopicsInNamespace)
//                .subscribe();
//        In the above example, the consumer subscribes to the persistent topics that can match the topic name pattern. If you want the consumer subscribes to all persistent and non-persistent topics that can match the topic name pattern, set subscriptionTopicsMode to RegexSubscriptionMode.AllTopics.
//
//                Pattern pattern = Pattern.compile("public/default/.*");
//        pulsarClient.newConsumer()
//                .subscriptionName("my-sub")
//                .topicsPattern(pattern)
//                .subscriptionTopicsMode(RegexSubscriptionMode.AllTopics)
//                .subscribe();
//        Note
//        By default, the subscriptionTopicsMode of the consumer is PersistentOnly. Available options of subscriptionTopicsMode are PersistentOnly, NonPersistentOnly, and AllTopics.
//
//                你还可以订阅明确的主题列表 (如果愿意, 可跨命名空间):
//
//        List<String> topics = Arrays.asList(
//                "topic-1",
//                "topic-2",
//                "topic-3"
//        );
//
//        Consumer multiTopicConsumer = consumerBuilder
//                .topics(topics)
//                .subscribe();
//
//// Alternatively:
//        Consumer multiTopicConsumer = consumerBuilder
//                .topic(
//                        "topic-1",
//                        "topic-2",
//                        "topic-3"
//                )
//                .subscribe();
//        You can also subscribe to multiple topics asynchronously using the subscribeAsync method rather than the synchronous subscribe method. The following is an example.
//
//                Pattern allTopicsInNamespace = Pattern.compile("persistent://public/default.*");
//        consumerBuilder
//                .topics(topics)
//                .subscribeAsync()
//                .thenAccept(this::receiveMessageFromConsumer);
//
//        private void receiveMessageFromConsumer(Object consumer) {
//            ((Consumer)consumer).receiveAsync().thenAccept(message -> {
//                // Do something with the received message
//                receiveMessageFromConsumer(consumer);
//            });
//        }


    }

}
