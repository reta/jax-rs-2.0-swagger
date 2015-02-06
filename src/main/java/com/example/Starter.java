package com.example;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.jboss.resteasy.plugins.server.servlet.FilterDispatcher;
import com.example.config.AppConfig;
import com.example.rs.JaxRsApiApplication;

public class Starter {
	private static final int SERVER_PORT = 8080;
	private static final String CONTEXT_PATH = "rest";
	
	public static void main( final String[] args ) throws Exception {
		Resource.setDefaultUseCaches( false );
		
		final Server server = new Server( SERVER_PORT );		
		System.setProperty( AppConfig.SERVER_PORT, Integer.toString( SERVER_PORT ) );
		System.setProperty( AppConfig.SERVER_HOST, "localhost" );
		System.setProperty( AppConfig.CONTEXT_PATH, CONTEXT_PATH );				
		
		FilterDispatcher dispatcher = new FilterDispatcher();
		FilterHolder filterHolder = new FilterHolder( dispatcher ); 
		filterHolder.setInitParameter("javax.ws.rs.Application", JaxRsApiApplication.class.getName());
		 
		// Configuring Apache CXF servlet and Spring listener  
// 		final ServletContextHandler context = new ServletContextHandler(); 		
// 		context.setContextPath( "/" );
// 		context.addFilter(filterHolder, "/*", EnumSet.allOf( DispatcherType.class ) ); 	 		
 		
 	    // Configuring Swagger as static web resource
 		final ServletHolder swaggerHolder = new ServletHolder( new DefaultServlet() );
 		final ServletContextHandler swagger = new ServletContextHandler();
 		swagger.setContextPath( "/" );
 		swagger.addServlet( swaggerHolder, "/*" );
 		swagger.addFilter(filterHolder, "/*", EnumSet.allOf( DispatcherType.class ) );
        swagger.setResourceBase( Resource.newClassPathResource( "/webapp" ).getURI().toString() );

 		final HandlerList handlers = new HandlerList();
 		handlers.addHandler( swagger );
 		
        server.setHandler( handlers );
        server.start();
        server.join();	
	}
}

