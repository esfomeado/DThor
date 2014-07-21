package pt.ipb.dthor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import org.apache.jasper.servlet.JspServlet;
import org.apache.tomcat.InstanceManager;
import org.apache.tomcat.SimpleInstanceManager;
import org.eclipse.jetty.annotations.ServletContainerInitializersStarter;
import org.eclipse.jetty.apache.jsp.JettyJasperInitializer;
import org.eclipse.jetty.plus.annotation.ContainerInitializer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import pt.ipb.dthor.servlets.TorrentUpload;


public class JettyServer {

    private final String WEBROOT = "/pt/ipb/dthor/webroot/";
    private final int port = 8080;
    private  Server server = null;
    private static JettyServer instance = null;
    
    public static JettyServer getInstance() throws IOException, URISyntaxException, Exception{
        
        if(instance == null) {
            instance = new JettyServer();
        }
        return instance;
    } 

    private JettyServer() throws FileNotFoundException, URISyntaxException, IOException, Exception {
        
        server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.addConnector(connector);

        URL indexURL = this.getClass().getResource(WEBROOT);

        if (indexURL == null) {
            throw new FileNotFoundException("Unable to find " + WEBROOT);
        }

        URI indexURI = indexURL.toURI();

        //Pasta temporaria
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File writeDir = new File(tempDir.toString(), "dthor");

        //Verifica se a pasta existe
        if (!writeDir.exists()) {
            //Tenta criar a pasta
            if (!writeDir.mkdirs()) {
                throw new IOException("Unable to create directory: " + writeDir);
            }
        }

        //Obriga os ficheiros a ser compilados com o JAVAC
        System.setProperty("org.apache.jasper.compiler.disablejsr199", "false");

        WebAppContext context = new WebAppContext();
        context.setContextPath("/");
        context.setAttribute("javax.servlet.context.tempdir", writeDir);
        context.setResourceBase(indexURI.toASCIIString());
        context.setAttribute(InstanceManager.class.getName(), new SimpleInstanceManager());
        server.setHandler(context);

        //Adiciona os servlets
        context.addServlet(TorrentUpload.class, "/upload");
        
        //Garante que o JSP é inicializado sem problemas
        JettyJasperInitializer sci = new JettyJasperInitializer();
        ServletContainerInitializersStarter sciStarter = new ServletContainerInitializersStarter(context);
        ContainerInitializer initializer = new ContainerInitializer(sci, null);
        List<ContainerInitializer> initializers = new ArrayList<>();
        initializers.add(initializer);

        context.setAttribute("org.eclipse.jetty.containerInitializers", initializers);
        context.addBean(sciStarter, true);

        ClassLoader jspClassLoader = new URLClassLoader(new URL[0], this.getClass().getClassLoader());
        context.setClassLoader(jspClassLoader);

        //JSP Servlet
        ServletHolder jspServlet = new ServletHolder("jsp", JspServlet.class);
        jspServlet.setInitOrder(0);
        jspServlet.setInitParameter("logVerbosityLevel", "WARNING");
        jspServlet.setInitParameter("fork", "false");
        jspServlet.setInitParameter("xpoweredBy", "false");
        jspServlet.setInitParameter("compilerTargetVM", "1.7");
        jspServlet.setInitParameter("compilerSourceVM", "1.7");
        jspServlet.setInitParameter("keepgenerated", "true");
        context.addServlet(jspServlet, "*.jsp");

        ServletHolder holderDefault = new ServletHolder("default", DefaultServlet.class);
        holderDefault.setInitParameter("resourceBase", indexURI.toASCIIString());
        holderDefault.setInitParameter("dirAllowed", "true");
        context.addServlet(holderDefault, "/");

        server.start(); 
    }

    public void stop() throws Exception {
        server.stop();
    }

    public void waitForInterrupt() throws InterruptedException {
        server.join();
    }
}