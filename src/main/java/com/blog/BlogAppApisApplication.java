package com.blog;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BlogAppApisApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogAppApisApplication.class, args);
	}

	//@bean : it will create an object that can be used later.
	//we can create this ModelMapper bean in new Class file with annotation of @Config.
	@Bean
	public ModelMapper modelMapper()
	{
		return new ModelMapper();
	}

}
