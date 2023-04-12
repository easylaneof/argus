package ru.tinkoff.edu.scrapper.jooq;

import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.*;

public class JooqCodegen extends IntegrationEnvironment {
    public static void main(String[] args) throws Exception {
        Jdbc jdbc = new Jdbc()
                .withDriver(POSTGRES_CONTAINER.getDriverClassName())
                .withUrl(POSTGRES_CONTAINER.getJdbcUrl())
                .withUsername(POSTGRES_CONTAINER.getUsername())
                .withPassword(POSTGRES_CONTAINER.getPassword());

        Database database = new Database()
                .withName("org.jooq.meta.postgres.PostgresDatabase")
                .withInputSchema("public");

        Generate options = new Generate()
                .withGeneratedAnnotation(true)
                .withGeneratedAnnotationDate(false)
                .withNullableAnnotation(true)
                .withNullableAnnotationType("org.jetbrains.annotations.Nullable")
                .withNonnullAnnotation(true)
                .withNonnullAnnotationType("org.jetbrains.annotations.NotNull")
                .withJpaAnnotations(false)
                .withValidationAnnotations(true)
                .withSpringAnnotations(true)
                .withConstructorPropertiesAnnotation(true)
                .withConstructorPropertiesAnnotationOnPojos(true)
                .withConstructorPropertiesAnnotationOnRecords(true)
                .withFluentSetters(false)
                .withDaos(false)
                .withPojos(true);

        Target target = new Target()
                .withPackageName("ru.tinkoff.edu.scrapper.entity.jooq")
                .withDirectory("scrapper/src/main/java");

        Configuration configuration = new Configuration()
                .withJdbc(jdbc)
                .withGenerator(
                        new Generator()
                                .withDatabase(database)
                                .withGenerate(options)
                                .withTarget(target)
                );

        GenerationTool.generate(configuration);
    }
}
