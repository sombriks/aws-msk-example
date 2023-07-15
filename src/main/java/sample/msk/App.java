package sample.msk;

import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 */
public class App {

    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        Javalin.create(javalinConfig -> {
        }).routes(() -> {
            ApiBuilder.get("/list", ctx -> {
                LOG.info("aaa");
                ctx.json("aaa");
            });
        })
        .start(7070);

    }
}
