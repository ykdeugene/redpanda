package com.tmsjsb.redpanda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.tmsjsb.redpanda.Config.CorsConfig;

@SpringBootApplication
@Import(CorsConfig.class)
public class RedpandaApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedpandaApplication.class, args);
	}

}
