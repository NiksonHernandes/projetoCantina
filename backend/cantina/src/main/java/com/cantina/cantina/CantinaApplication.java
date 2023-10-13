package com.cantina.cantina;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class CantinaApplication {

	public static void main(String[] args) {
		SpringApplication.run(CantinaApplication.class, args);

		//System.out.println(new BCryptPasswordEncoder().encode("123"));
	}

}
