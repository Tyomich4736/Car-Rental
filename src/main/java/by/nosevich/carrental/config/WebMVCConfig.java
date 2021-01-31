package by.nosevich.carrental.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import by.nosevich.carrental.config.properties.ImageStorageProperties;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

	@Autowired
	private ImageStorageProperties imageStorageProperties;
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("auth/login");
		registry.addViewController("/register").setViewName("auth/register");
		registry.addViewController("/successfulreg").setViewName("auth/successfulReg");
		registry.addViewController("/rentterms").setViewName("rentTerms");
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry
	      .addResourceHandler("/files/**")
	      .addResourceLocations("file:"+imageStorageProperties.getStoragePath()+"/");
	 }
}
