package sample.msk;

import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;
import org.apache.kafka.clients.KafkaClient;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Hello world!
 */
public class App {

    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.load(App.class.getResourceAsStream("/client.properties"));
        KafkaProducer<String, Object> producer = new KafkaProducer<>(props);

        Javalin
                .create()
                .routes(() -> {
                    ApiBuilder.get("/list", ctx -> {
                        ctx.json(producer.metrics());
                    });
                })
                .start(7070);

    }
}
