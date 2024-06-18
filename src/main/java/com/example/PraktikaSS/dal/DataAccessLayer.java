package com.example.PraktikaSS.dal;


import com.example.PraktikaSS.models.*;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Getter
public class DataAccessLayer {
    private final SessionFactory sessionFactory;

    @Autowired
    public DataAccessLayer(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    Session session = null;
    public User getUserFromDatabaseByEmail(String email) {
        session = sessionFactory.openSession();
        session.getTransaction().begin();
        Query query = session
                .createQuery("FROM User where email = :email")
                .setParameter("email", email);
        User userFrom = (User) query.uniqueResult();
        if (userFrom == null) {
            return null;
        }
        return userFrom;
    }
    public String newUserToDatabase(User user) {
        session = sessionFactory.openSession();
        session.beginTransaction();
        String email = user.getEmail();

        Query query = session
                .createQuery("FROM User where email = :email")
                .setParameter("email", email);
        User userFrom = (User) query.uniqueResult();

        if (userFrom != null) {
            return "Выберите другое имя";
        }
        session.persist(user);
        session.getTransaction().commit();
        session.close();
        return "ok";
    }
    public void createNotes(Notes newNotes) {
        session = sessionFactory.openSession();
        session.beginTransaction();
        session.persist(newNotes);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
    }
}