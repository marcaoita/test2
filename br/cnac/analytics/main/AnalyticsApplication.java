package br.cnac.analytics.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@EnableWebSecurity
@ComponentScan({"br.cnac.analytics.api", "br.cnac.analytics.domain.dao", "br.cnac.analytics.domain.dto", "br.cnac.analytics.configuration", "br.cnac.analytics.service.model", "br.cnac.analytics.service.enumeration", "br.cnac.analytics.service.interfaces"})
@EntityScan("br.cnac.analytics.service.model")
@EnableJpaRepositories(basePackages = "br.cnac.analytics.service.interfaces")
@SpringBootApplication
public class AnalyticsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnalyticsApplication.class, args);
	}


}
