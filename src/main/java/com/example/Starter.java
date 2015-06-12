package com.example;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.glassfish.jersey.servlet.ServletContainer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import com.example.config.AppConfig;
import com.example.rs.JaxRsApiApplication;

public class Starter {
	private static final int SERVER_PORT = 8080;
	private static final String CONTEXT_PATH = "rest/api";
	
	public static void main( final String[] args ) throws Exception {
		Resource.setDefaultUseCaches( false );
		
		final Server server = new Server( SERVER_PORT );		
		System.setProperty( AppConfig.SERVER_PORT, Integer.toString( SERVER_PORT ) );
		System.setProperty( AppConfig.SERVER_HOST, "localhost" );
		System.setProperty( AppConfig.CONTEXT_PATH, CONTEXT_PATH );				

		// Configuring Apache CXF servlet and Spring listener  
		final ServletHolder servletHolder = new ServletHolder( new ServletContainer() );
		servletHolder.setInitParameter( "javax.ws.rs.Application", JaxRsApiApplication.class.getName() );
		
 		final ServletContextHandler context = new ServletContextHandler();
 		context.setContextPath( "/" );
 		context.addServlet( servletHolder, "/" + CONTEXT_PATH + "/*" );
 		context.addEventListener( new ContextLoaderListener() );
 		context.setInitParameter( "contextClass", AnnotationConfigWebApplicationContext.class.getName() );
 		context.setInitParameter( "contextConfigLocation", AppConfig.class.getName() ); 		
 		
 	    // Configuring Swagger as static web resource
 		final ServletHolder swaggerHolder = new ServletHolder( new DefaultServlet() );
 		final ServletContextHandler swagger = new ServletContextHandler();
 		swagger.setContextPath( "/swagger" );
 		swagger.addServlet( swaggerHolder, "/*" );
        swagger.setResourceBase( new ClassPathResource( "/webapp" ).getURI().toString() );

 		final HandlerList handlers = new HandlerList();
 		handlers.addHandler( swagger );
 		handlers.addHandler( context );
 		
        server.setHandler( handlers );
        server.start();
        server.join();	
	}
}

