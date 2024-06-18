package com.example.PraktikaSS.repositories;

import com.example.PraktikaSS.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByUserNameContainingIgnoreCaseOrUserSurNameContainingIgnoreCaseOrUserMiddleNameContainingIgnoreCase(String userName, String userSurName, String userMiddleName);
}
