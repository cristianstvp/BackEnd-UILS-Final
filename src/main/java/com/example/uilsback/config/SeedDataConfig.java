package com.example.uilsback.config;


import com.example.uilsback.model.Role;
import com.example.uilsback.model.User;
import com.example.uilsback.repository.IUserRepository;
import com.example.uilsback.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeedDataConfig implements CommandLineRunner {

    private IUserRepository userRepository;

    private IUserService userService;

    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {
        /*if (userRepository.count() == 0) {

            User admin = User
                    .builder()
                    .fullName("Admin")
                    .studentCode("1111111")
                    .email("admin@admin.com")
                    .username("admin")
                    .password(passwordEncoder.encode("password"))
                    .role(Role.ROLE_ADMIN)
                    .build();

            userService.save(admin);
            log.debug("created ADMIN user - {}", admin);
        }*/
    }

    @Autowired
    public void setUserRepository(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setNotasService(@Qualifier("userServiceImpl") IUserService userService){
        this.userService = userService;
    }

}
