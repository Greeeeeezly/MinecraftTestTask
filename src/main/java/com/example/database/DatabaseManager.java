package com.example.database;

import com.example.ExampleMod;
import com.example.database.entity.MessageEntity;
import com.example.service.MessageService;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.util.Optional;
import java.util.Properties;

public final class DatabaseManager {
    private static volatile MessageRepository messageRepository;
    private static volatile MessageService messageService;
    private static volatile SessionFactory sessionFactory;
    private static volatile StandardServiceRegistry serviceRegistry;

    private DatabaseManager() {
    }

    public static void initialize() {
        if (messageRepository != null) {
            return;
        }

        try {
            PostgresConfig config = PostgresConfig.loadOrCreate();

            Configuration configuration = new Configuration();
            configuration.addAnnotatedClass(MessageEntity.class);

            Properties settings = new Properties();
            settings.put(Environment.DRIVER, "org.postgresql.Driver");
            settings.put(Environment.URL, config.jdbcUrl());
            settings.put(Environment.USER, config.username());
            settings.put(Environment.PASS, config.password());
            settings.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
            settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
            settings.put(Environment.HBM2DDL_AUTO, "validate");
            settings.put(Environment.SHOW_SQL, "false");
            settings.put(Environment.FORMAT_SQL, "false");
            settings.put("hibernate.bytecode.provider", "javassist");

            configuration.setProperties(settings);

            serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            messageRepository = new MessageRepository(sessionFactory);
            messageService = new MessageService(messageRepository);
            ExampleMod.LOGGER.info("PostgreSQL connection initialized for {}", config.jdbcUrl());
        } catch (Exception e) {
            ExampleMod.LOGGER.error("Failed to initialize PostgreSQL connection", e);
            if (messageRepository != null) {
                try {
                    messageRepository.close();
                } catch (Exception closeEx) {
                    ExampleMod.LOGGER.warn("Suppressed error while rolling back failed database initialization", closeEx);
                } finally {
                    messageRepository = null;
                    messageService = null;
                }
            }
            if (sessionFactory != null) {
                try {
                    sessionFactory.close();
                } catch (Exception closeFactory) {
                    ExampleMod.LOGGER.warn("Suppressed error while closing SessionFactory after failure", closeFactory);
                } finally {
                    sessionFactory = null;
                }
            }
            if (serviceRegistry != null) {
                StandardServiceRegistryBuilder.destroy(serviceRegistry);
                serviceRegistry = null;
            }
        }
    }

    public static Optional<MessageRepository> getMessageRepository() {
        return Optional.ofNullable(messageRepository);
    }

    public static Optional<MessageService> getMessageService() {
        return Optional.ofNullable(messageService);
    }

    public static void shutdown() {
        if (messageRepository != null) {
            try {
                messageRepository.close();
            } catch (Exception e) {
                ExampleMod.LOGGER.warn("Error while closing PostgreSQL connection", e);
            } finally {
                messageRepository = null;
                messageService = null;
                if (sessionFactory != null) {
                    try {
                        sessionFactory.close();
                    } catch (Exception closeEx) {
                        ExampleMod.LOGGER.warn("Error while closing SessionFactory during shutdown", closeEx);
                    } finally {
                        sessionFactory = null;
                    }
                }
                if (serviceRegistry != null) {
                    StandardServiceRegistryBuilder.destroy(serviceRegistry);
                    serviceRegistry = null;
                }
            }
        }
    }
}
