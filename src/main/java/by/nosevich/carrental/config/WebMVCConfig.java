package by.nosevich.carrental.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("auth/login");
		registry.addViewController("/register").setViewName("auth/register");
		registry.addViewController("/successfulreg").setViewName("auth/successfulReg");
		registry.addViewController("/").setViewName("fragments/navBar");
	}

	/*@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
        .addResourceHandler("/resources")
        .addResourceLocations("/resources/static/img/");	
	}*/
	
}
