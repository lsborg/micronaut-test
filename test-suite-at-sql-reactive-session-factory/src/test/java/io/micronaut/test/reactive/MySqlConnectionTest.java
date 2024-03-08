package io.micronaut.test.reactive;

import io.micronaut.test.annotation.Sql;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.hibernate.SessionFactory;
import org.hibernate.reactive.stage.Stage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
@Sql(value = {"classpath:create.sql", "classpath:datasource_1_insert.sql"}, resourceType = SessionFactory.class)
class MySqlConnectionTest  {

    @Inject
    SessionFactory sessionFactory;

    @Test
    void testSqlHasBeenInjected() {
        var result = sessionFactory
            .unwrap(Stage.SessionFactory.class)
            .withTransaction((session, tx) -> session.createNativeQuery("SELECT name from MyTable where id = 2").getSingleResult())
            .toCompletableFuture()
            .join();

        assertEquals("Albatross", result);
    }
}
