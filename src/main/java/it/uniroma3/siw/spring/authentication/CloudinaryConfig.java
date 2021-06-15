package it.uniroma3.siw.spring.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

import com.cloudinary.utils.ObjectUtils;

@Configuration
public class CloudinaryConfig {

	@Bean
	public Cloudinary cloudinary() {
		return new Cloudinary(ObjectUtils.asMap(
				"cloud_name", "paolomos",
				"api_key", "346549517435613",
				"api_secret", "ugR9-s8qENtHlxPvXa_ShOz9E-k"));
	}

}
