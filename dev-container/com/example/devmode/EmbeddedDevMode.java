package com.example.devmode;

import java.io.File;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.webapp.WebAppContext;

import com.example.web.Application;
import com.google.gwt.core.ext.ServletContainer;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.dev.shell.jetty.JettyLauncher;

public class EmbeddedDevMode extends JettyLauncher {

	@Override
	public ServletContainer start( TreeLogger logger, int port, File appRootDir ) throws Exception {
		Application.start();
		return new JettyServletContainer( logger, Application.server, Application.context, port, appRootDir );
	}

	/**
	 * Copied from gwt-dev.jar and modified a bit
	 *
	 */
	protected static class JettyServletContainer extends ServletContainer {
		private final int actualPort;
		private final File appRootDir;
		private final TreeLogger logger;
		private final Server server;
		private final WebAppContext wac;

		public JettyServletContainer( TreeLogger logger, Server server, WebAppContext wac, int actualPort, File appRootDir ) {
			this.logger = logger;
			this.server = server;
			this.wac = wac;
			this.actualPort = actualPort;
			this.appRootDir = appRootDir;
		}

		@Override
		public int getPort() {
			return actualPort;
		}

		@Override
		public void refresh() throws UnableToCompleteException {
			String msg = "Reloading web app to reflect changes in " + appRootDir.getAbsolutePath();
			TreeLogger branch = logger.branch( TreeLogger.INFO, msg );
			// Temporarily log Jetty on the branch.
			Log.setLog( new JettyTreeLogger( branch ) );
			try {
				wac.stop();
				server.stop();
				wac.start();
				server.start();
				branch.log( TreeLogger.INFO, "Reload completed successfully" );
			} catch ( Exception e ) {
				branch.log( TreeLogger.ERROR, "Unable to restart embedded Jetty server", e );
				throw new UnableToCompleteException();
			} finally {
				// Reset the top-level logger.
				Log.setLog( new JettyTreeLogger( logger ) );
			}
		}

		@Override
		public void stop() throws UnableToCompleteException {
			TreeLogger branch = logger.branch( TreeLogger.INFO, "Stopping Jetty server" );
			// Temporarily log Jetty on the branch.
			Log.setLog( new JettyTreeLogger( branch ) );
			try {
				server.stop();
				server.setStopAtShutdown( false );
				branch.log( TreeLogger.TRACE, "Stopped successfully" );
			} catch ( Exception e ) {
				branch.log( TreeLogger.ERROR, "Unable to stop embedded Jetty server", e );
				throw new UnableToCompleteException();
			} finally {
				// Reset the top-level logger.
				Log.setLog( new JettyTreeLogger( logger ) );
			}
		}
	}

	public static class JettyTreeLogger implements Logger {
		private final TreeLogger logger;

		public JettyTreeLogger( TreeLogger logger ) {
			if ( logger == null ) {
				throw new NullPointerException();
			}
			this.logger = logger;
		}
		
		@Override
		public void debug( String msg, Object... args ) {
			if ( logger.isLoggable( TreeLogger.SPAM ) ) {
				logger.log( TreeLogger.SPAM, format( msg, args[0], args[1] ) );
			}
		}
		
		@Override
		public void debug( String msg, Throwable th ) {
			logger.log( TreeLogger.SPAM, msg, th );
		}
		
		@Override
		public Logger getLogger( String name ) {
			return this;
		}
		
		@Override
		public void info( String msg, Object... args ) {
			if ( logger.isLoggable( TreeLogger.TRACE ) ) {
				logger.log( TreeLogger.TRACE, format( msg, args[0], args[1] ) );
			}
		}
		
		@Override
		public boolean isDebugEnabled() {
			return logger.isLoggable( TreeLogger.SPAM );
		}
		
		@Override
		public void setDebugEnabled( boolean enabled ) {
			// ignored
		}
		
		@Override
		public void warn( String msg, Object... args ) {
			if ( logger.isLoggable( TreeLogger.WARN ) ) {
				logger.log( TreeLogger.WARN, format( msg, args[0], args[1] ) );
			}
		}
		
		@Override
		public void warn( String msg, Throwable th ) {
			logger.log( TreeLogger.WARN, msg, th );
		}

		/**
		 * Copied from org.mortbay.log.StdErrLog.
		 */
		private String format( String msg, Object... args ) {
			int i0 = msg.indexOf( "{}" );
			int i1 = i0 < 0 ? -1 : msg.indexOf( "{}", i0 + 2 );

			if ( args[1] != null && i1 >= 0 ) {
				msg = msg.substring( 0, i1 ) + args[1] + msg.substring( i1 + 2 );
			}
			if ( args[0] != null && i0 >= 0 ) {
				msg = msg.substring( 0, i0 ) + args[0] + msg.substring( i0 + 2 );
			}
			return msg;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void warn( Throwable thrown ) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void info( Throwable thrown ) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void info( String msg, Throwable thrown ) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void debug( Throwable thrown ) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void ignore( Throwable ignored ) {
			// TODO Auto-generated method stub
			
		}

	}

}
