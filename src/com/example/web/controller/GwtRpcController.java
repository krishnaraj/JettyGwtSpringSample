package com.example.web.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@Controller
public class GwtRpcController extends RemoteServiceServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3204102606415034711L;

	@Autowired 
	private ServletContext servletContext;
	
	@Autowired
	private RemoteService remoteService;

	private Class<?> remoteServiceClass;
	
	private HttpServletRequest request;
	
	@RequestMapping(value = "/jettygwtspringsampleapp/*.rpc")
	public ModelAndView handleRequest( HttpServletRequest request, HttpServletResponse response ) throws Exception {
		super.doPost( request, response );
		return null;
	}

	@Override
	public String processCall( String payload ) throws SerializationException {
		try {

			RPCRequest rpcRequest = RPC.decodeRequest( payload, this.remoteServiceClass );

			// delegate work to the spring injected service
			return RPC.invokeAndEncodeResponse( this.remoteService, rpcRequest.getMethod(), rpcRequest.getParameters() );
		} catch ( IncompatibleRemoteServiceException ex ) {
			getServletContext().log( "An IncompatibleRemoteServiceException was thrown while processing this call.", ex );
			return RPC.encodeResponseForFailure( null, ex );
		}
	}

	@Override
	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext( ServletContext servletContext ) {
		this.servletContext = servletContext;
	}

	public void setRemoteService( RemoteService remoteService ) {
		this.remoteService = remoteService;
		this.remoteServiceClass = this.remoteService.getClass();
	}
	
	public HttpServletRequest getThreadLocalHttpRequest() {
		return getThreadLocalRequest();
	}

}