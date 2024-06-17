package com.example.PraktikaSS.Configs;
import com.example.PraktikaSS.dal.DataAccessLayer;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class DataConfiguration {
    private SessionFactory sessionFactory = null;
    @Autowired
    public DataConfiguration(EntityManager entityManager) {
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        sessionFactory = session.getSessionFactory();
    }
    @Bean
    public DataAccessLayer dataAccessLayer(){
        return new DataAccessLayer(sessionFactory);
    }
}