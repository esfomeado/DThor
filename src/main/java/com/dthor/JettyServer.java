package com.dthor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import org.apache.jasper.servlet.JspServlet;
import org.eclipse.jetty.annotations.ServletContainerInitializersStarter;
import org.eclipse.jetty.apache.jsp.JettyJasperInitializer;
import org.eclipse.jetty.plus.annotation.ContainerInitializer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import com.dthor.servlets.TorrentDelete;
import com.dthor.servlets.TorrentSearch;
import com.dthor.servlets.TorrentUpload;

public class JettyServer {

    private final int SERVER_PORT = DThorConfig.JETTY_PORT;
    private final static String WEB_ROOT = "./webroot/";
    private static JettyServer instance = null;
    private final Server server;

    public static JettyServer getInstance() throws Exception {
        if(instance == null) {
            instance = new JettyServer();
        }
        return instance;
    }

    private JettyServer() throws Exception {
        server = new Server(SERVER_PORT);

        URL webRootURL = this.getClass().getResource(WEB_ROOT);

        if(webRootURL == null) {
            throw new FileNotFoundException("Unable to find " + WEB_ROOT);
        }

        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File writeDir = new File(tempDir.toString(), "dthor");

        if(!writeDir.exists()) {
            if(!writeDir.mkdirs()) {
                throw new IOException("Unable to create directory: " + writeDir);
            }
        }

        System.setProperty("org.apache.jasper.compiler.disablejsr199", "false");

        WebAppContext context = new WebAppContext();
        context.setContextPath("/");
        context.setAttribute("javax.servlet.context.tempdir", writeDir);
        context.setResourceBase(webRootURL.toURI().toASCIIString());

        context.addServlet(TorrentUpload.class, "/upload");
        context.addServlet(TorrentSearch.class, "/search");
        context.addServlet(TorrentDelete.class, "/delete");

        server.setHandler(context);

        JettyJasperInitializer sci = new JettyJasperInitializer();
        ServletContainerInitializersStarter sciStarter = new ServletContainerInitializersStarter(context);
        ContainerInitializer initializer = new ContainerInitializer(sci, null);
        List<ContainerInitializer> initializers = new ArrayList<>();
        initializers.add(initializer);

        context.setAttribute("org.eclipse.jetty.containerInitializers", initializers);
        context.addBean(sciStarter, true);

        ClassLoader jspClassLoader = new URLClassLoader(new URL[0], this.getClass().getClassLoader());
        context.setClassLoader(jspClassLoader);

        ServletHolder jspServlet = new ServletHolder("jsp", JspServlet.class);
        jspServlet.setInitOrder(0);
        jspServlet.setInitParameter("logVerbosityLevel", "WARNING");
        jspServlet.setInitParameter("fork", "false");
        jspServlet.setInitParameter("xpoweredBy", "false");
        jspServlet.setInitParameter("compilerTargetVM", "1.7");
        jspServlet.setInitParameter("compilerSourceVM", "1.7");
        jspServlet.setInitParameter("keepgenerated", "true");
        context.addServlet(jspServlet, "*.jsp");

        server.start();
    }

    public void waitForInterrupt() throws InterruptedException {
        server.join();
    }

    public void stop() throws Exception {
        server.stop();
    }
}
