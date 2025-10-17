package com.example.database;

import com.example.ExampleMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class PostgresConfig {
    private static final String FILE_NAME = ExampleMod.MOD_ID + "-postgres.properties";
    private static final String KEY_JDBC_URL = "jdbcUrl";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    private final String jdbcUrl;
    private final String username;
    private final String password;

    private PostgresConfig(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    public static PostgresConfig loadOrCreate() {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve(FILE_NAME);
        ensureFileExists(configPath);

        Properties properties = new Properties();
        try (Reader reader = Files.newBufferedReader(configPath, StandardCharsets.UTF_8)) {
            properties.load(reader);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read Postgres config: " + configPath, e);
        }

        String jdbcUrl = properties.getProperty(KEY_JDBC_URL, "").trim();
        String username = properties.getProperty(KEY_USERNAME, "").trim();
        String password = properties.getProperty(KEY_PASSWORD, "").trim();

        if (jdbcUrl.isEmpty() || username.isEmpty()) {
            throw new IllegalStateException("Postgres config is incomplete. Please update " + configPath.toAbsolutePath());
        }

        return new PostgresConfig(jdbcUrl, username, password);
    }

    private static void ensureFileExists(Path configPath) {
        if (Files.exists(configPath)) {
            return;
        }

        try {
            Files.createDirectories(configPath.getParent());
            Properties defaults = new Properties();
            defaults.setProperty(KEY_JDBC_URL, "jdbc:postgresql://localhost:5432/messages");
            defaults.setProperty(KEY_USERNAME, "postgres");
            defaults.setProperty(KEY_PASSWORD, "postgres");

            try (Writer writer = Files.newBufferedWriter(configPath, StandardCharsets.UTF_8)) {
                defaults.store(writer, "Configure PostgreSQL connection for modid");
            }

            ExampleMod.LOGGER.warn("Created default Postgres config at {}. Please review credentials.", configPath.toAbsolutePath());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create Postgres config: " + configPath, e);
        }
    }

    public String jdbcUrl() {
        return jdbcUrl;
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }
}
