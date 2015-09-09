package edu.ucar.unidata.cloudcontrol.init;

import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import java.sql.Connection;
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


/**
 * Done at application initialization.
 * Loads the cloudcontrol.properties information.
 */
public class ApplicationInitialization implements ServletContextListener {
    protected static Logger logger = Logger.getLogger(ApplicationInitialization.class);

    private static final String DEFAULT_HOME = System.getProperty("catalina.base") + "/cloudcontrol";
    private static final String DEFAULT_DATABASE = "derby";

    private String cloudcontrolHome = null;
    private String databaseSelected = null;

    /**
     * Find the application home (cloudcontrol.home) and make sure it exists.  if not, create it.
     * TODO: add -Dname=value JVM argument 
     * Find out what database was selected for use and create the database if it doesn't exist.
     * 
     * @param servletContextEvent  The event class.
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent)  {
        ServletContext servletContext = servletContextEvent.getServletContext();

        try {
            File configFile = new File(servletContext.getRealPath("") + "/WEB-INF/classes/cloudcontrol.properties");
            if (!configFile.exists()) {
                logger.info("Configuration file not provided.");  
                logger.info("Using cloudcontrol.home default: " + DEFAULT_HOME);    
                cloudcontrolHome = DEFAULT_HOME; 
                logger.info("Using cloudcontrol.db default: " + DEFAULT_DATABASE);    
                databaseSelected = DEFAULT_DATABASE;
            } else {
                logger.info("Reading configuration file.");  
                String currentLine;
                BufferedReader reader = new BufferedReader(new FileReader(configFile));
                while ((currentLine = reader.readLine()) != null) {
                    String lineData;
                    if ((lineData = StringUtils.stripToNull(currentLine)) != null) {
                        if (lineData.startsWith("cloudcontrol.home")) {
                            cloudcontrolHome = StringUtils.removeStart(lineData, "cloudcontrol.home=");
                            logger.info("cloudcontrol.home set to: " + cloudcontrolHome);  
                        }
                        if (lineData.startsWith("cloudcontrol.db")) {
                            databaseSelected = StringUtils.removeStart(lineData, "cloudcontrol.db=");
                            logger.info("cloudcontrol.db set to: " + databaseSelected);  
                        }
                    }
                }
                if (cloudcontrolHome == null) {
                    logger.info("Configuration file does not contain cloudcontrol.home information.");  
                    logger.info("Using cloudcontrol.home default: " + DEFAULT_HOME);  
                    cloudcontrolHome = DEFAULT_HOME;      
                }
                if (databaseSelected == null) {
                    logger.info("Configuration file does not contain cloudcontrol.db information.");  
                    logger.info("Using cloudcontrol.db default: " + DEFAULT_DATABASE);  
                    databaseSelected = DEFAULT_DATABASE;      
                }
            }
            createDirectory(new File(cloudcontrolHome));
        } catch (Exception e) {            
            logger.error(e.getMessage());   
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
     * @param sce  The event class.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (databaseSelected.equals("derby")) {
            String derbyUrl = "jdbc:derby:" + cloudcontrolHome + "/db/cloudcontrol";
            try { 
                DriverManager.getConnection( derbyUrl + ";shutdown=true");
            } catch (SQLException e) {
                logger.error(e.getMessage()); 
            }  
        }
    }


    /**
     * Creates a directory (and parent directories as needed) using the provided file.
     * 
     * @param file  The directory to create.
     * @throws RuntimeException  If we are unable to create the directory.
     */
    public void createDirectory(File file) throws RuntimeException {
        if (!file.exists()) {
            logger.info("Creating cloudcontrol.home...");
            if (!file.mkdirs()) {
                throw new RuntimeException("Unable to create the following directory: " + file);
            }                   
        } 
    }

    /**
     * Creates a directory (and parent directories as needed) using the provided file.
     * 
     * @param cloudcontrolHome  The value of cloudcontrol.home.
     * @param databaseSelected  The value of cloudcontrol.db.
     */
    public void createDatabase(String cloudcontrolHome, String databaseSelected, ServletContext servletContext)  {
        if (databaseSelected.equals("derby")) {
            String derbyDriver = "org.apache.derby.jdbc.EmbeddedDriver";
            String derbyUrl = "jdbc:derby:" + cloudcontrolHome + "/db/cloudcontrol";
            if (!new File(cloudcontrolHome + "/db/cloudcontrol").exists()) {
                logger.info("Database does not exist yet.  Creating...");
                try { 
                    createDirectory(new File(cloudcontrolHome + "/db")); 
                } catch (Exception e) {
                    logger.error(e.getMessage()); 
                }  
                try { 
                    createTables(derbyDriver, derbyUrl + ";create=true", null, null, servletContext);
                    DriverManager.getConnection( derbyUrl + ";shutdown=true");
                } catch (SQLException e) {
                    logger.error(e.getMessage()); 
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
     * Creates the tables in the databases.
     *
     * @param driver  The jdbc driver to load.
     * @param url  The database url with which to make the connection.
     * @param username  The database username (null if not used).
     * @param password  The database password (null if not used).
     * @throws SQLException  If an SQL error occurs when trying to close the preparedStatement or conenction.
     */
    private static void createTables(String driver, String url, String username, String password, ServletContext servletContext) throws SQLException { 
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String createUsersTableSQL = "CREATE TABLE users" +
                                     "(" +
                                     "userId INTEGER primary key not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                                     "userName VARCHAR(50) not null, " +
                                     "password CHAR(32) not null, " +
                                     "accessLevel INTEGER not null, " +
                                     "accountStatus INTEGER not null, " +
                                     "emailAddress VARCHAR(75) not null, " +
                                     "firstName VARCHAR(75) not null, " +
                                     "lastName VARCHAR(75) not null, " +
                                     "dateCreated TIMESTAMP not null, " +
                                     "dateModified TIMESTAMP not null" +
                                     ")";

        String insertAdminUserSQL = "INSERT INTO users " +
                                    "(userName, password, accessLevel, accountStatus, emailAddress, firstName, lastName, dateCreated, dateModified) VALUES " +
                                    "(?,?,?,?,?,?,?,?,?)"; 

     
        try {
            connection = getDatabaseConnection(driver, url, username, password);
            preparedStatement = connection.prepareStatement(createUsersTableSQL);
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement(insertAdminUserSQL);
            preparedStatement.setString(1, "admin");
            preparedStatement.setString(2, "4cb9c8a8048fd02294477fcb1a41191a");
            preparedStatement.setInt(3, 2);
            preparedStatement.setInt(4, 1);
            preparedStatement.setString(5, "plaza@unidata.ucar.edu");
            preparedStatement.setString(6, "Cloud Control");
            preparedStatement.setString(7, "Administrator");
            preparedStatement.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(insertAdminUserSQL);
            preparedStatement.setString(1, "test");
            preparedStatement.setString(2, "4cb9c8a8048fd02294477fcb1a41191a");
            preparedStatement.setInt(3, 1);
            preparedStatement.setInt(4, 0);
            preparedStatement.setString(5, "plaza@unidata.ucar.edu");
            preparedStatement.setString(6, "test");
            preparedStatement.setString(7, "user");
            preparedStatement.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
            preparedStatement.executeUpdate();

        } catch (SQLException e) { 
            logger.error(e.getMessage()); 
        } finally { 
			if (preparedStatement != null) {
				preparedStatement.close();
			} 
			if (connection != null) {
				connection.close();
			} 
		} 
	}


    /**
     * Loads the appropriate JDBC driver and makes the database connection.
     *
     * @param driver  The jdbc driver to load.
     * @param url  The database url with which to make the connection.
     * @param username  The database username (null if not used).
     * @param password  The database password (null if not used).
     * @return  The the database connection.
     */
	private static Connection getDatabaseConnection(String driver, String url, String username, String password) { 
        Connection connection = null;
        try {
            Class.forName(driver); 
        } catch (ClassNotFoundException e) { 
            logger.error(e.getMessage()); 
        }

        try { 
            if ((username != null) && (password != null)){
                connection = DriverManager.getConnection(url, username, password);
            } else {
                connection = DriverManager.getConnection(url);
            }
            return connection; 
        } catch (SQLException e) {
            logger.error(e.getMessage()); ;
        } 
        return connection; 
    }

}
