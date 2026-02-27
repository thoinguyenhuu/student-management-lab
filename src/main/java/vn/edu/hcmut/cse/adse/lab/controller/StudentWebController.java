package vn.edu.hcmut.cse.adse.lab.controller;
import org.springframework.stereotype.Controller; // Luu y: su dung @Controller, KHONG dung
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.edu.hcmut.cse.adse.lab.service.StudentService;
import vn.edu.hcmut.cse.adse.lab.entity.Student;
import java.util.List;
@Controller
@RequestMapping("/students")
public class StudentWebController {
    private final StudentService service;

    public StudentWebController(StudentService service) {
        this.service = service;
    }
    // Route: GET http://localhost:8080/students
    @GetMapping
    public String getAllStudents(@RequestParam(required = false) String keyword, Model model) {
        List<Student> students = service.searchByName(keyword);
        model.addAttribute("students", students);
        model.addAttribute("keyword", keyword == null ? "" : keyword);
        return "students";
    }

    // Route: GET http://localhost:8080/students/{id}
    @GetMapping("/{id}")
    public String getStudentDetail(@PathVariable String id, Model model) {
        Student student = service.getById(id);
        if (student == null) {
            return "redirect:/students";
        }
        model.addAttribute("student", student);
        return "student-detail";
    }

    // Route: GET http://localhost:8080/students/new
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("formTitle", "Create Student");
        model.addAttribute("formAction", "/students");
        model.addAttribute("isEdit", false);
        return "student-form";
    }

    // Route: POST http://localhost:8080/students
    @PostMapping
    public String createStudent(Student student) {
        service.create(student);
        return "redirect:/students";
    }

    // Route: GET http://localhost:8080/students/edit/{id}
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        Student student = service.getById(id);
        if (student == null) {
            return "redirect:/students";
        }
        model.addAttribute("student", student);
        model.addAttribute("formTitle", "Edit Student");
        model.addAttribute("formAction", "/students/update/" + student.getId());
        model.addAttribute("isEdit", true);
        return "student-form";
    }

    // Route: POST http://localhost:8080/students/update/{id}
    @PostMapping("/update/{id}")
    public String updateStudent(@PathVariable String id, Student updated) {
        Student saved = service.update(id, updated);
        if (saved == null) {
            return "redirect:/students";
        }
        return "redirect:/students/" + id;
    }

    // Route: POST http://localhost:8080/students/delete/{id}
    @PostMapping("/delete/{id}")
    public String deleteStudent(@PathVariable String id) {
        service.delete(id);
        return "redirect:/students";
    }
}
