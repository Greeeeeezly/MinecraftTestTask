package com.example.database;

import com.example.database.entity.MessageEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.UUID;

public class MessageRepository implements AutoCloseable {
    private final SessionFactory sessionFactory;

    public MessageRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(UUID playerUuid, String text) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.persist(new MessageEntity(playerUuid, text));
            tx.commit();
        } catch (Exception ex) {
            if (tx != null && tx.getStatus() == TransactionStatus.ACTIVE) {
                tx.rollback();
            }
            throw ex;
        }
    }

    @Override
    public void close() {
        sessionFactory.close();
    }
}
