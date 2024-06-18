package com.example.PraktikaSS.service;

import com.example.PraktikaSS.models.Student;
import com.example.PraktikaSS.repositories.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> searchStudents(String query) {
        String[] parts = query.split(" ");
        Set<Student> resultSet = new HashSet<>();
        for (String part : parts) {
            resultSet.addAll(studentRepository.findByUserNameContainingIgnoreCaseOrUserSurNameContainingIgnoreCaseOrUserMiddleNameContainingIgnoreCase(part, part, part));
        }
        return new ArrayList<>(resultSet);
    }
}
