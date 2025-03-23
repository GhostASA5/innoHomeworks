package org.project.student.api;

import lombok.RequiredArgsConstructor;
import org.project.model.Course;
import org.project.student.service.LkService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/lk")
@RequiredArgsConstructor
public class LkController {

    private final LkService lkService;

    @GetMapping("/courses")
    public List<Course> getCourses(Principal principal) {
        return lkService.getCourses(principal.getName());
    }
}
