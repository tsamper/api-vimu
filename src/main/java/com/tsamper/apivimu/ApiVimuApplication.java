package com.tsamper.apivimu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tsamper.apivimu.modelo.bbdd.ConexionBBDD;

@SpringBootApplication
public class ApiVimuApplication {

	public static void main(String[] args) {
		ConexionBBDD.conectar();
		SpringApplication.run(ApiVimuApplication.class, args);
	
	}
}
