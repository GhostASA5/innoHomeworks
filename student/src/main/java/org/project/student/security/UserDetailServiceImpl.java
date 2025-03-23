package org.project.student.security;

import lombok.RequiredArgsConstructor;
import org.project.student.service.StudentService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final StudentService studentService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new AppUserPrincipal(studentService.getStudentByEmail(username));
    }
}
