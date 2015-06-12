package com.example.rs;

import org.glassfish.jersey.server.ResourceConfig;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider;
import com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jaxrs.listing.ResourceListingProvider;

public class JaxRsApiApplication extends ResourceConfig {
	public JaxRsApiApplication() {
		register( PeopleRestService.class );
		register( JacksonJsonProvider.class );
		register( ApiDeclarationProvider.class );
		register( ApiListingResourceJSON.class );
		register( ResourceListingProvider.class );
	}	
}
