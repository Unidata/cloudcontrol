package edu.ucar.unidata.cloudcontrol.init;

import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import java.nio.charset.StandardCharsets;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * Done at application initialization.
 * Loads the application.properties information.
 */
public class ApplicationInitialization implements ServletContextListener {
    protected static Logger logger = Logger.getLogger(ApplicationInitialization.class);

    private static final String DEFAULT_HOME = System.getProperty("catalina.base") + "/cloudcontrol";
    private static final String DEFAULT_DATABASE = "derby";

    private String cloudcontrolHome = null;
    private String databaseSelected = null;
    
    /**
     * Find the application home ${cloudcontrol.home} and make sure it exists.  if not, create it.
     * Find out what database was selected for use and create the database if it doesn't exist.
     * 
     * @param servletContextEvent  The event class.
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent)  {
        ServletContext servletContext = servletContextEvent.getServletContext();
        logger.info("Application context initialization..."); 
        try {
            File configFile = new File(servletContext.getRealPath("") + "/WEB-INF/classes/application.properties");
            if (!configFile.exists()) {
                logger.info("Configuration file not provided.");  
                logger.info("Using ${cloudcontrol.home} default: " + DEFAULT_HOME);    
                cloudcontrolHome = DEFAULT_HOME; 
                logger.info("Using ${cloudcontrol.db} default: " + DEFAULT_DATABASE);    
                databaseSelected = DEFAULT_DATABASE;
            } else {
                logger.info("Reading configuration file.");  
                String currentLine;
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8));
                    while ((currentLine = reader.readLine()) != null) {
                        String lineData;
                        if ((lineData = StringUtils.stripToNull(currentLine)) != null) {
                            if (lineData.startsWith("cloudcontrol.home")) {
                                cloudcontrolHome = StringUtils.removeStart(lineData, "cloudcontrol.home=");
                                logger.info("${cloudcontrol.home} set to: " + cloudcontrolHome);  
                            }
                            if (lineData.startsWith("cloudcontrol.db")) {
                                databaseSelected = StringUtils.removeStart(lineData, "cloudcontrol.db=");
                                logger.info("${cloudcontrol.db} set to: " + databaseSelected);  
                            }
                        }
                    }                    
                } catch (SecurityException e) {
                    logger.error("Unable to access cloudcontrol configuration file: " + e);
                } catch (FileNotFoundException e) {
                    logger.error("Unable to find cloudcontrol configuration file: " + e);
                } catch (IOException e) {
                    logger.error("Issues reading from the cloudcontrol configuration file: " + e); 
                } catch (Exception e) {
                    logger.error(e); 
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            logger.error("Unable to close BufferedReader: " + e); 
                        }
                    }
                }
                
                if (cloudcontrolHome == null) {
                    logger.info("Configuration file does not contain ${cloudcontrol.home} information.");  
                    logger.info("Using ${cloudcontrol.home} default: " + DEFAULT_HOME);  
                    cloudcontrolHome = DEFAULT_HOME;      
                }
                if (databaseSelected == null) {
                    logger.info("Configuration file does not contain ${cloudcontrol.db} information.");  
                    logger.info("Using ${cloudcontrol.db} default: " + DEFAULT_DATABASE);  
                    databaseSelected = DEFAULT_DATABASE;      
                }
            }
            createDirectory(new File(cloudcontrolHome));
        } catch (NullPointerException e) {            
            logger.error("Show-stopping file creation error: " + e );   
            throw new RuntimeException(e.getMessage());  
        }
        
        try {
            createDatabase(cloudcontrolHome, databaseSelected, servletContext);
        } catch (Exception e) {            
            logger.error(e.getMessage());   
            throw new RuntimeException(e.getMessage());  
        }
    }  

    /**
     * Shutdown the database if it hasn't already been shutdown.
     * 
     * @param servletContextEvent  The event class.
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        Connection connection = null;
        logger.info("Application context destruction..."); 
        if (databaseSelected.equals("derby")) { 
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                Driver driver = drivers.nextElement();
                try { 
                    DriverManager.deregisterDriver(driver);
                    logger.info("Deregistering jdbc driver."); 
                } catch (SQLException e) {
                    logger.error("Error deregistering driver: " + e.getMessage()); 
                }  
            }
        }
        logger.error("Goodbye."); 
    }


    /**
     * Creates a directory (and parent directories as needed) using the provided file.
     * 
     * @param file  The directory to create.
     * @throws RuntimeException  If we are unable to create the directory.
     */
    public void createDirectory(File file) throws RuntimeException {
        if (!file.exists()) {
            logger.info("Creating ${cloudcontrol.home}...");
            if (!file.mkdirs()) {
                throw new RuntimeException("Unable to create the following directory: " + file);
            }                   
        } 
    }

    /**
     * Creates a directory (and parent directories as needed) using the provided file.
     * 
     * @param cloudcontrolHome  The value of ${cloudcontrol.home}.
     * @param databaseSelected  The value of ${cloudcontrol.db}.
     */
    public void createDatabase(String cloudcontrolHome, String databaseSelected, ServletContext servletContext)  {
        if (databaseSelected.equals("derby")) {
            Connection connection = null;
            String derbyDriver = "org.apache.derby.jdbc.EmbeddedDriver";
            String derbyUrl = "jdbc:derby:" + cloudcontrolHome + "/db/cloudcontrol";
            String createUsersTableSql = "CREATE TABLE users" +
                                         "(" +
                                         "userId INTEGER primary key not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                                         "userName VARCHAR(50) not null, " +
                                         "password VARCHAR(80) not null, " +
                                         "accessLevel INTEGER not null, " +
                                         "accountStatus INTEGER not null, " +
                                         "emailAddress VARCHAR(75) not null, " +
                                         "fullName VARCHAR(75) not null, " +
                                         "dateCreated TIMESTAMP not null, " +
                                         "dateModified TIMESTAMP not null" +
                                         ")";

        
            String createClientConfigTableSql = "CREATE TABLE clientConfig" +
                                                "(" +
                                                "id INTEGER primary key not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                                                "dockerHost VARCHAR(100) not null, " +
                                                "dockerCertPath VARCHAR(120) not null, " +
                                                "dockerTlsVerify INTEGER not null, " +
                                                "createdBy VARCHAR(75) not null, " +
                                                "lastUpdatedBy VARCHAR(75) not null, " +
                                                "dateCreated TIMESTAMP not null, " +
                                                "dateModified TIMESTAMP not null" +
                                                ")";
            String createDisplayImageTableSql = "CREATE TABLE displayImages" +
                                                "(" +
                                                "id INTEGER primary key not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                                                "imageId VARCHAR(120) not null" +
                                                ")";
            
            if (!new File(cloudcontrolHome + "/db/cloudcontrol").exists()) {
                logger.info("Database does not exist yet.  Creating...");
                try { 
                    createDirectory(new File(cloudcontrolHome + "/db")); 
                } catch (Exception e) {
                    logger.error("Failed to create directory: " + e); 
                }  
                
                try { 
                    createTables(createUsersTableSql, derbyDriver, derbyUrl + ";create=true", null, null);
                    addDefaultAdminUser(derbyDriver, derbyUrl + ";create=true", null, null);
                    createTables(createClientConfigTableSql, derbyDriver, derbyUrl + ";create=true", null, null);
                    createTables(createDisplayImageTableSql, derbyDriver, derbyUrl + ";create=true", null, null);
                    
                    connection = DriverManager.getConnection(derbyUrl + ";shutdown=true");
                } catch (SQLException e) {
                    if (e.getSQLState().equals("XJ004")) {
                        logger.info("Database is already shutdown" + e.getMessage()); 
                    } else {
                        logger.error("Error trying to get a database connection: " + e.getMessage()); 
                    }
                } finally { 
                    if (connection != null) {
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            logger.error("Unable to close database connection: " + e); 
                        }
                    } 
                }      
            } else {
                logger.info("Database already exists.");
                logger.info("Our work here is done.");
            }
        } else {
            // mySQL or whatever
        }
    }


    /**
     * Creates the tables in the database.
     *
     * @param createSql  The table creation sql to execute.
     * @param driver  The jdbc driver to load.
     * @param url  The database url with which to make the connection.
     * @param username  The database username (null if not used).
     * @param password  The database password (null if not used).
     * @throws SQLException  If an SQL error occurs when trying to close the preparedStatement or conenction.
     */
    private static void createTables(String createSql, String driver, String url, String username, String password) throws SQLException { 
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        
        try {
            try {
                Class.forName(driver); 
            } catch (ClassNotFoundException e) { 
                logger.error("Unable to find database drive class: " + e); 
            }
            try { 
                if ((username != null) && (password != null)){
                    connection = DriverManager.getConnection(url, username, password);
                } else {
                    connection = DriverManager.getConnection(url);
                }
            } catch (SQLException e) {
                logger.error("Unable to establish database connection: " + e); 
            } 
            
            preparedStatement = connection.prepareStatement(createSql);
            preparedStatement.executeUpdate();

        } catch (SQLException e) { 
            logger.error("Unable to prepare and/or execute prepared statement: " + e); 
        } finally { 
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Unable to close prepared statement: " + e); 
                }
            } 
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Unable to close database connection: " + e); 
                }
            } 
        } 
    }

    /**
     * Adds default admin user to the users table in the database.
     *
     * @param driver  The jdbc driver to load.
     * @param url  The database url with which to make the connection.
     * @param username  The database username (null if not used).
     * @param password  The database password (null if not used).
     * @throws SQLException  If an SQL error occurs when trying to close the preparedStatement or conenction.
     */
    private static void addDefaultAdminUser(String driver, String url, String username, String password) throws SQLException { 
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String insertSql = "INSERT INTO users " +
                                    "(userName, password, accessLevel, accountStatus, emailAddress, fullName, dateCreated, dateModified) VALUES " +
                                    "(?,?,?,?,?,?,?,?)"; 
     
        try {
            try {
                Class.forName(driver); 
            } catch (ClassNotFoundException e) { 
                logger.error("Unable to find database drive class: " + e); 
            }
            try { 
                if ((username != null) && (password != null)){
                    connection = DriverManager.getConnection(url, username, password);
                } else {
                    connection = DriverManager.getConnection(url);
                }
            } catch (SQLException e) {
                logger.error("Unable to establish database connection: " + e); 
            } 
            
            preparedStatement = connection.prepareStatement(insertSql);
            preparedStatement.setString(1, "admin");
            preparedStatement.setString(2, "$2a$10$gJ4ITtIMNpxsU0xmx6qoE.0MGZ2fv8HpoaL1IlgNdhBlUgmcVwRDO");
            preparedStatement.setInt(3, 2);
            preparedStatement.setInt(4, 1);
            preparedStatement.setString(5, "admin@foo.bar.baz");
            preparedStatement.setString(6, "Cloud Control Administrator");
            preparedStatement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
            preparedStatement.executeUpdate();
            
        } catch (SQLException e) { 
            logger.error("Unable to prepare and/or execute prepared statement: " + e); 
        } finally { 
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error("Unable to close prepared statement: " + e); 
                }
            } 
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Unable to close database connection: " + e); 
                }
            } 
        } 
    }
    
}
