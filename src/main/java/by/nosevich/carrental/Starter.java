package by.nosevich.carrental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import by.nosevich.carrental.config.properties.EmailProperties;
import by.nosevich.carrental.config.properties.ImageStorageProperties;

@SpringBootApplication
@EnableAutoConfiguration
@EnableTransactionManagement
@ComponentScan
@EnableConfigurationProperties({EmailProperties.class, ImageStorageProperties.class})
public class Starter {
	
	public static void main(String[] args) {
		SpringApplication.run(Starter.class, args);
	}
}