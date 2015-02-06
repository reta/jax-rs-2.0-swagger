package com.example.rs;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.example.config.AppConfig;
import com.example.resource.Person;
import com.example.services.PeopleService;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.wordnik.swagger.jaxrs.config.BeanConfig;
import com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider;
import com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jaxrs.listing.ResourceListingProvider;

@ApplicationPath( "api" )
public class JaxRsApiApplication extends Application {
    public JaxRsApiApplication() {
		final BeanConfig config = new BeanConfig();

		config.setVersion( "1.0.0" );
		config.setScan( true );
		config.setResourcePackage( Person.class.getPackage().getName() );
		config.setBasePath( 
			String.format( "http://%s:%s%s",
				System.getProperty( AppConfig.SERVER_HOST ),
				System.getProperty( AppConfig.SERVER_PORT ),
				"/api" 
			) 
		);
	}
	
	@Override
	public Set< Object > getSingletons() {
		return new HashSet<>( Arrays.< Object >asList(
			new ApiDeclarationProvider(),
			new ApiListingResourceJSON(),
			new ResourceListingProvider(),
			new PeopleRestService( new PeopleService() ),
			new JacksonJsonProvider()
	    ) );
	}
}
