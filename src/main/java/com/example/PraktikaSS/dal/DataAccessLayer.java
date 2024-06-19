package com.example.PraktikaSS.dal;


import com.example.PraktikaSS.models.*;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Getter
public class DataAccessLayer {
    private final SessionFactory sessionFactory;

    @Autowired
    public DataAccessLayer(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    Session session = null;
    @Transactional
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
    @Transactional
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
    @Transactional
    public void createNotes(Notes newNotes) {
        session = sessionFactory.openSession();
        session.beginTransaction();
        session.persist(newNotes);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
    }
    @Transactional
    public void deleteNotesById(Long id){
        session = sessionFactory.openSession();
        session.beginTransaction();
        Notes notes = session.get(Notes.class, id);
        session.remove(notes);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
    }
    @Transactional
    public void updateNotesById(Long id, Notes updatedNotes){
        session = sessionFactory.openSession();
        session.beginTransaction();
        Notes notes = session.get(Notes.class, id);
        notes.setNoteContent(updatedNotes.getNoteContent());
        session.merge(notes);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
    }
    @Transactional
    public Notes getNotesById(Long id){
        session = sessionFactory.openSession();
        session.beginTransaction();
        Notes notes = session.get(Notes.class, id);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
        return notes;
    }
    @Transactional
    public List<Notes> getNotesByUserId(Long userId) {
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        return session.createQuery("SELECT b FROM Notes b WHERE b.user.id = :userId", Notes.class)
                .setParameter("userId", userId)
                .getResultList();
    }
    @Transactional
    public List<Notes> getNotesByStudentId(Long studentId) {
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        return session.createQuery("SELECT b FROM Notes b WHERE b.student.id = :studentId", Notes.class)
                .setParameter("studentId", studentId)
                .getResultList();
    }
    @Transactional
    public List<Notes> getNotesByUserIdAndStudentId(Long userId, Long studentId) {
        Session session = sessionFactory.openSession();
        try {
            session.getTransaction().begin();
            Query<Notes> query = session.createQuery(
                    "SELECT b FROM Notes b WHERE b.user.id = :userId AND b.student.id = :studentId",
                    Notes.class);
            query.setParameter("userId", userId);
            query.setParameter("studentId", studentId);
            List<Notes> result = query.getResultList();
            session.getTransaction().commit();
            return result;
        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }
    @Transactional
    public void createStudent(Student newStudent) {
        session = sessionFactory.openSession();
        session.beginTransaction();
        session.persist(newStudent);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
    }
//    public void deleteStudentById(Long id){
//        session = sessionFactory.openSession();
//        session.beginTransaction();
//        Student student = session.get(Student.class, id);
//        session.remove(student);
//        session.getTransaction().commit();
//        if (session != null) {
//            session.close();
//        }
//    }
@Transactional
public void deleteStudentById(Long studentId) {
    try (Session session = sessionFactory.openSession()) {
        session.beginTransaction();
        Student student = session.get(Student.class, studentId);
        if (student != null) {
            session.remove(student);
            session.getTransaction().commit();
        } else {
            session.getTransaction().rollback();
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    @Transactional
    public void deleteNotesByStudentId(Long studentId) {
        Session session = sessionFactory.openSession();
        try {
            session.getTransaction().begin();
            int deletedCount = session.createQuery("DELETE FROM Notes b WHERE b.student.id = :studentId")
                    .setParameter("studentId", studentId)
                    .executeUpdate();
            session.getTransaction().commit();
            System.out.println("Deleted " + deletedCount + " notes for student ID: " + studentId);
        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    @Transactional
    public void updateStudentById(Long id, Student updatedStudent){
        session = sessionFactory.openSession();
        session.beginTransaction();
        Student student = session.get(Student.class, id);
        student.setClassName(updatedStudent.getClassName());
        student.setUserMiddleName(updatedStudent.getUserMiddleName());
        student.setUserSurName(updatedStudent.getUserSurName());
        student.setUserName(updatedStudent.getUserName());
        session.merge(student);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
    }
    @Transactional
    public Student getStudentById(Long id){
        session = sessionFactory.openSession();
        session.beginTransaction();
        Student student = session.get(Student.class, id);
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
        return student;
    }
    @Transactional
    public List<Student> getStudents(){
        session = sessionFactory.openSession();
        session.getTransaction().begin();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Student> query = builder.createQuery(Student.class);
        Root<Student> root = query.from(Student.class);
        query.select(root);
        List<Student> resultList = session.createQuery(query).getResultList();
        return resultList;
    }
    @Transactional
    public List<Student> getStudentsFromDatabaseByClassName(String className) {
        session = sessionFactory.openSession();
        session.getTransaction().begin();
        Query<Student> query = session
                .createQuery("FROM Student WHERE className = :className", Student.class)
                .setParameter("className", className);
        List<Student> students = query.getResultList();
        session.getTransaction().commit();
        if (session != null) {
            session.close();
        }
        return students;
    }
}