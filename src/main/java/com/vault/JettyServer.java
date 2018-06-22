package com.vault;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

/**
 * A jetty server for retrieving files on file system.
 */
public class JettyServer {
    private static final Logger logger = LoggerFactory.getLogger(JettyServer.class);
    private static final int PORT_NUM = 39527;

    public static void main(String[] args) {
        Server server = new Server(PORT_NUM);

        Injector injector = Guice.createInjector(
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(GuiceFilter.class);
                    }
                });

        FilterHolder filterHolder = new FilterHolder(injector.getInstance(GuiceFilter.class));
        ServletContextHandler servletContextHandler = new ServletContextHandler(server, "/");
        servletContextHandler.addFilter(filterHolder, "/*", EnumSet.allOf(DispatcherType.class));

        try {
            server.start();
        } catch (Exception e) {
            logger.error("Failed to start server.", e);
        }
    }
}
