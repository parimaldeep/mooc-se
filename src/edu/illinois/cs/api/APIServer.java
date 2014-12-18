package edu.illinois.cs.api;

import org.eclipse.jetty.server.Server;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

class APIServer {
	public static void main(String[] args) throws Exception {
		Server server = new Server(8081);

		ServletContextHandler context = new ServletContextHandler(
				ServletContextHandler.SESSIONS);

		context.setContextPath("/");
		server.setHandler(context);

		context.addServlet(new ServletHolder(new HelloWorldServlet()), "/hello");
		context.addServlet(new ServletHolder(new SearchServlet()), "/search");

		server.start();
		server.join();
	}
};
