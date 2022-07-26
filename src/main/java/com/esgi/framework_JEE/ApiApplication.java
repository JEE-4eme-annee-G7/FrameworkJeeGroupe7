package com.esgi.framework_JEE;

import com.esgi.framework_JEE.role.domain.entity.Role;
import com.esgi.framework_JEE.role.domain.repository.RoleRepository;
import com.esgi.framework_JEE.user.Domain.entities.User;
import com.esgi.framework_JEE.user.Domain.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ApiApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleRepository roleRepository, UserRepository userRepository){
		return args -> {
			var role_USER = roleRepository.findRoleByTitlePermission("USER");
			if(role_USER == null)
				role_USER = roleRepository.save(createRole("USER"));


			var role_ADMIN = roleRepository.findRoleByTitlePermission("ADMIN");
			if (role_ADMIN == null)
				role_ADMIN = roleRepository.save(createRole("ADMIN"));


			System.out.println("----------------------------");
			System.out.println(role_USER.getId());
			System.out.println(role_USER.getTitlePermission());
			System.out.println(role_ADMIN.getId());
			System.out.println(role_ADMIN.getTitlePermission());
			System.out.println("----------------------------");

			var saved_user1 = userRepository.findByEmail("lucas@hotmail.fr");
			if (saved_user1 == null)
				saved_user1 = userRepository.save(createUser("Lucas","Jehanno","lucas@hotmail.fr","azerty1234"));



			var saved_user2 = userRepository.findByEmail("test@test.fr");
			if (saved_user2 == null)
				saved_user2 = userRepository.save(createUser("Test","Test","test@test.fr","test1234test"));

			if (saved_user1.getPermission() == null){
				saved_user1.setPermission(role_ADMIN);
				userRepository.save(saved_user1);
			}
			if (saved_user2.getPermission() == null){
				saved_user2.setPermission(role_USER);
				userRepository.save(saved_user2);
			}






			System.out.println("----------------------------");
			System.out.println(saved_user1.getId());
			System.out.println(saved_user1.getEmail());
			System.out.println(saved_user1.getPermission().getTitlePermission());
			System.out.println(saved_user2.getId());
			System.out.println(saved_user2.getEmail());
			System.out.println(saved_user2.getPermission().getTitlePermission());
			System.out.println("----------------------------");


		};
	}

	private Role createRole(String nameRole){
		Role role = new Role();
		role.setTitlePermission(nameRole);
		return role;
	}

	private User createUser(String firstName, String lastName, String email, String password){
		User user = new User();
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(password);

		user.setFirstname(firstName);
		user.setLastname(lastName);
		user.setPassword(encodedPassword);
		user.setEmail(email);
		return user;
	}

 
}
