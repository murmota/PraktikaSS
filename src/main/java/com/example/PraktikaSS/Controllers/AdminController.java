package com.example.PraktikaSS.Controllers;

import com.example.PraktikaSS.dal.DataAccessLayer;
import com.example.PraktikaSS.models.Notes;
import com.example.PraktikaSS.models.Student;
import com.example.PraktikaSS.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/admin")
public class AdminController {
    private final DataAccessLayer dataAccessLayer;
    @Autowired
    public AdminController(DataAccessLayer dataAccessLayer, StudentService studentService) {
        this.dataAccessLayer = dataAccessLayer;
        this.studentService = studentService;
    }
    private final StudentService studentService;
    @PostMapping("/create/notes")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createNotes(@RequestBody Notes notes) {
        dataAccessLayer.createNotes(notes);
        return ResponseEntity.ok("Notes added successfully!");
    }
    @DeleteMapping("/delete/notes/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteNotesById(@PathVariable("id") long id) {
        dataAccessLayer.deleteNotesById(id);
        return ResponseEntity.ok("Notes deleted successfully!");
    }
    @PutMapping("/update/notes/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateNotesById(@PathVariable("id") long id, @RequestBody Notes updatedNotes) {
        dataAccessLayer.updateNotesById(id, updatedNotes);
        return ResponseEntity.ok("Notes updated successfully!");
    }
    @GetMapping("/get/notes/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Notes> getNotesById(@PathVariable("id") long id) {
        Notes notes = dataAccessLayer.getNotesById(id);
        return ResponseEntity.ok(notes);
    }
    @GetMapping("/get/notes/user/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Notes>> getNotesByUserId(@PathVariable("userId") long userId) {
        return ResponseEntity.ok(dataAccessLayer.getNotesByUserId(userId));
    }
    @GetMapping("/get/notes/student/{studentId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Notes>> getNotesByStudentId(@PathVariable("studentId") long studentId) {
        return ResponseEntity.ok(dataAccessLayer.getNotesByStudentId(studentId));
    }
    @GetMapping("/get/notes/byUserAndStudent")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Notes>> getNotesByUserIdAndStudentId(@RequestParam Long userId, @RequestParam Long studentId) {
        return ResponseEntity.ok(dataAccessLayer.getNotesByUserIdAndStudentId(userId, studentId));
//        http://localhost:8080/admin/get/notes/byUserAndStudent?userId=1&studentId=1
    }
    @GetMapping("/students/search")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Student> searchStudents(@RequestParam String query) {
        return studentService.searchStudents(query);
        //http://localhost:8080/admin/students/search?query=свин
    }

    @PostMapping("/create/student")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createStudent(@RequestBody Student student) {
        dataAccessLayer.createStudent(student);
        return ResponseEntity.ok("Student added successfully!");
    }
    @DeleteMapping("/delete/student/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteStudentById(@PathVariable("id") long id) {
        dataAccessLayer.deleteStudentById(id);
        return ResponseEntity.ok("Student deleted successfully!");
    }
    @DeleteMapping("/delete/student/notes/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteStudentAndNotes(@PathVariable("id") Long id) {
        dataAccessLayer.deleteNotesByStudentId(id);
        dataAccessLayer.deleteStudentById(id);
        return ResponseEntity.ok("Student and associated notes deleted successfully!");
    }
    @PutMapping("/update/student/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateStudentById(@PathVariable("id") long id, @RequestBody Student updatedStudent) {
        dataAccessLayer.updateStudentById(id, updatedStudent);
        return ResponseEntity.ok("Student updated successfully!");
    }
    @GetMapping("/get/student/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Student> getStudentById(@PathVariable("id") long id) {
        Student student = dataAccessLayer.getStudentById(id);
        return ResponseEntity.ok(student);
    }
    @GetMapping("/get/students")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity getStudents(){
        return ResponseEntity.ok(dataAccessLayer.getStudents());
    }
    @GetMapping("/get/students/byClassName")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Student>> getStudentsByClassName(@RequestParam String className) {
        return ResponseEntity.ok(dataAccessLayer.getStudentsFromDatabaseByClassName(className));
        //http://localhost:8080/admin/get/students/byClassName?className=10A
    }
}