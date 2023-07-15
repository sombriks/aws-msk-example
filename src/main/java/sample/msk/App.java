package sample.msk;

import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Properties;

/**
 * Hello world!
 */
public class App {

    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.load(App.class.getResourceAsStream("/client.properties"));
        Admin admin = Admin.create(props);
        CreateTopicsResult result = admin.createTopics(Collections.singleton(
                new NewTopic("teste", 12, (short) 3)));
        LOG.info("{}", result);
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        Javalin.create()
                .routes(() -> {
                    ApiBuilder.get("/topics", ctx -> {
                        ctx.json(admin.listTopics());
                    });
                    ApiBuilder.get("/send", ctx -> {
                        producer.send(
                                new ProducerRecord<>(
                                        "teste",
                                        ctx.queryParam("key"),
                                        ctx.queryParam("value")),

                                (recordMetadata, exception) -> {
                                    LOG.info("{}", recordMetadata);
                                    if (exception != null)
                                        LOG.warn("problema: ", exception);
                                    ctx.json("DONE!");
                                }
                        );
                    });
                })
                .start(7070);

    }
}
