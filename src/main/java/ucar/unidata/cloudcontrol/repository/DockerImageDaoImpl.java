package edu.ucar.unidata.cloudcontrol.repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.dao.support.DaoSupport;

import edu.ucar.unidata.cloudcontrol.domain.DockerImage;

/**
 * The DockerImageDao implementation.  Data comes from commands issued to docker via the commandline. 
 */

public class DockerImageDaoImpl extends DaoSupport implements DockerImageDao {
  
    protected static Logger logger = Logger.getLogger(DockerImageDaoImpl.class);
	
	final protected void checkDaoConfig() { 
		BufferedReader bufferReader = null;
		try {
            ProcessBuilder processBuilder = new ProcessBuilder("docker", "version");
            logger.info("getting docker version to see if docker is running.");
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            bufferReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = bufferReader.readLine()) != null) {
                String[] lineData = line.split("\\s{2,}");
            }
            int errCode = process.waitFor();
            if (errCode > 0) {
			    logger.error("Unable to get Docker version.  Process exitValue: " + errCode);
			    throw new IllegalArgumentException("Docker needs to be running.");
            }
			bufferReader.close();
        } catch (Exception e) {
			logger.error("Unable to get Docker version: " + e.getMessage());
			throw new IllegalArgumentException("Docker needs to be running.", e);
        }
	}

    /**
     * Requests a List of all available DockerImages.
     * 
     * @return  A List of available DockerImages.   
     */
    public List<DockerImage> getDockerImageList() {
		List<DockerImage> dockerImages = new ArrayList<DockerImage>();
		BufferedReader bufferReader = null;
		try {
            ProcessBuilder processBuilder = new ProcessBuilder("docker", "images");
            logger.info("getting docker images");
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            bufferReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = bufferReader.readLine()) != null) {
                String[] lineData = line.split("\\s{2,}");
		        DockerImage dockerImage = new DockerImage();	
			    dockerImage.setRepository(lineData[0]);
			    dockerImage.setTag(lineData[1]);
			    dockerImage.setImageId(lineData[2]);
			    dockerImage.setCreated(lineData[3]);
			    dockerImage.setVirtualSize(lineData[4]);
				dockerImages.add(dockerImage);
            }
            int errCode = process.waitFor();
            if (errCode > 0) {
			    logger.error("Unable to get DockerImage list.  Process exitValue: " + errCode);
			    throw new RecoverableDataAccessException("Unable to get DockerImage list");
            }
		    bufferReader.close();
			dockerImages.remove(0);
        } catch (IOException e) {
			logger.error("Unable to get DockerImage list: " + e.getMessage());
			throw new RecoverableDataAccessException("Unable to get DockerImage list", e);
        } catch (InterruptedException e) {
			logger.error("Unable to get DockerImage list: " + e.getMessage());
			throw new RecoverableDataAccessException("Unable to get DockerImage list", e);
        } catch (Exception e) {
			logger.error("Unable to get DockerImage list: " + e.getMessage());
			throw new RecoverableDataAccessException("Unable to get DockerImage list", e);
        }
        return dockerImages;
    }
    
    /**
     * Returns the number of available DockerImages.
     * 
     * @return  The total number of available DockerImages.   
     */
    public int getDockerImageCount() {
        List<DockerImage> dockerImages = getDockerImageList();
        return dockerImages.size();
    }


}
