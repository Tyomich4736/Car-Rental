package by.nosevich.carrental.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "image.storage")
public class ImageStorageProperties {
	String storagePath;
}
