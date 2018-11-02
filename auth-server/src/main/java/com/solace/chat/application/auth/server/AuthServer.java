package com.solace.chat.application.auth.server;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthServer implements CommandLineRunner {


	public static void main(String[] args) {
		SpringApplication.run(AuthServer.class, args);
		// Set up ...
		try {
			Object lock = new Object();
			synchronized (lock) {
				while (true) {
					lock.wait();
				}
			}
		} catch (InterruptedException ex) {
		}
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
