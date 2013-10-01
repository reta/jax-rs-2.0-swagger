package com.example.config;

import java.util.Arrays;

import javax.ws.rs.ext.RuntimeDelegate;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;

import com.example.resource.Person;
import com.example.rs.JaxRsApiApplication;
import com.example.rs.PeopleRestService;
import com.example.services.PeopleService;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.wordnik.swagger.jaxrs.config.BeanConfig;
import com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider;
import com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jaxrs.listing.ResourceListingProvider;

@Configuration
public class AppConfig {
	public static final String SERVER_PORT = "server.port";
	public static final String SERVER_HOST = "server.host";
	public static final String CONTEXT_PATH = "context.path";		
	
	@Bean( destroyMethod = "shutdown" )
	public SpringBus cxf() {
		return new SpringBus();
	}
	
	@Bean @DependsOn( "cxf" )
	public Server jaxRsServer() {
		JAXRSServerFactoryBean factory = RuntimeDelegate.getInstance().createEndpoint( jaxRsApiApplication(), JAXRSServerFactoryBean.class );
		factory.setServiceBeans( Arrays.< Object >asList( peopleRestService(), apiListingResourceJson() ) );
		factory.setAddress( factory.getAddress() );
		factory.setProviders( Arrays.< Object >asList( jsonProvider(), resourceListingProvider(), apiDeclarationProvider() ) );
		return factory.create();
	}
	
	@Bean @Autowired
	public BeanConfig swaggerConfig( Environment environment ) {
		final BeanConfig config = new BeanConfig();

		config.setVersion( "1.0.0" );
		config.setScan( true );
		config.setResourcePackage( Person.class.getPackage().getName() );
		config.setBasePath( 
			String.format( "http://%s:%s/%s%s",
				environment.getProperty( SERVER_HOST ),
				environment.getProperty( SERVER_PORT ),
				environment.getProperty( CONTEXT_PATH ),
				jaxRsServer().getEndpoint().getEndpointInfo().getAddress() 
			) 
		);
		
		return config;
	}

	@Bean
	public ApiDeclarationProvider apiDeclarationProvider() {
		return new ApiDeclarationProvider();
	}
	
	@Bean
	public ApiListingResourceJSON apiListingResourceJson() {
		return new ApiListingResourceJSON();
	}
	
	@Bean
	public ResourceListingProvider resourceListingProvider() {
		return new ResourceListingProvider();
	}
	
	@Bean 
	public JaxRsApiApplication jaxRsApiApplication() {
		return new JaxRsApiApplication();
	}
	
	@Bean 
	public PeopleRestService peopleRestService() {
		return new PeopleRestService();
	}
	
	@Bean 
	public PeopleService peopleService() {
		return new PeopleService();
	}
		
	@Bean
	public JacksonJsonProvider jsonProvider() {
		return new JacksonJsonProvider();
	}
}
