package io.micronaut.test.support.sql;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.Experimental;
import io.micronaut.core.annotation.Internal;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;
import org.hibernate.SessionFactory;
import org.hibernate.reactive.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

@Singleton
@Internal
@Experimental
@Requires(classes = {SessionFactory.class})
public class SessionFactoryHandler implements SqlHandler<SessionFactory> {

    private static final Logger LOG = LoggerFactory.getLogger(SessionFactoryHandler.class);

    @Override
    public void handle(@NonNull SessionFactory sessionFactory, @NonNull String sql) {
        Stage.SessionFactory reactiveSessionFactory = sessionFactory.unwrap(Stage.SessionFactory.class);
        LOG.debug("--- executeUpdate: {}", sql);

        reactiveSessionFactory.withSession((session) -> {
            return Mono.fromCompletionStage(session.createNativeQuery(sql).executeUpdate()).toFuture();
        }).toCompletableFuture().join();
    }
}
