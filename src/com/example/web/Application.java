package com.example.web;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.example.web.config.WebConfig;

public class Application {

	private static final AnnotationConfigWebApplicationContext APP_CTX = new AnnotationConfigWebApplicationContext();
	
	public static Server server = null;
	public static WebAppContext context = null;
	

	public static void main( String[] args ) throws Exception {
		start();
	}

	public static void start() throws Exception {
		APP_CTX.register( WebConfig.class );

		context = new WebAppContext();
        context.setResourceBase("./war");
        context.setDescriptor("./war/WEB-INF/web.xml");
        context.setContextPath("/");
        context.setParentLoaderPriority(true);
        
        final ServletHolder servletHolder = new ServletHolder( new DispatcherServlet( APP_CTX ) );
        context.addServlet( servletHolder, "*.rpc" );
		
		server = new Server( 8888 );

		server.setHandler( context );

		server.start();
		//server.join();
	}

	public static AnnotationConfigWebApplicationContext getContext() {
		return APP_CTX;
	}

}
