package com.example.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.example.client.GreetingService;
import com.example.shared.FieldVerifier;
import com.example.web.controller.GwtRpcController;

/**
 * The server side implementation of the RPC service.
 */
@Configuration
public class GreetingServiceImpl implements GreetingService {

	@Autowired
	GwtRpcController rpcController;

	public String greetServer( String input ) throws IllegalArgumentException {
		// Verify that the input is valid.
		if ( !FieldVerifier.isValidName( input ) ) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException( "Name must be at least 4 characters long" );
		}

		String serverInfo = rpcController.getServletContext().getServerInfo();
		String userAgent = rpcController.getThreadLocalHttpRequest().getHeader( "User-Agent" );

		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml( input );
		userAgent = escapeHtml( userAgent );

		return "Hello, " + input + "!<br><br>I am running " + serverInfo + ".<br><br>It looks like you are using:<br>" + userAgent;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to prevent cross-site script vulnerabilities.
	 * 
	 * @param html
	 *            the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml( String html ) {
		if ( html == null ) {
			return null;
		}
		return html.replaceAll( "&", "&amp;" ).replaceAll( "<", "&lt;" ).replaceAll( ">", "&gt;" );
	}
}
