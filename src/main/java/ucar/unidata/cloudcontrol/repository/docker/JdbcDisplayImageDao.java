package edu.ucar.unidata.cloudcontrol.repository.docker;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import edu.ucar.unidata.cloudcontrol.domain.docker.DisplayImage;

/**
 * The DisplayImageDao implementation.  Persistence mechanism is a database. 
 */

public class JdbcDisplayImageDao extends JdbcDaoSupport implements DisplayImageDao {
  
    protected static Logger logger = Logger.getLogger(JdbcDisplayImageDao.class);

    private SimpleJdbcInsert insertActor;

    /**
     * Looks up and retrieves the DisplayImage from the persistence mechanism using the Image ID.
     * 
     * @param imageId   The ID of the Image (will be unique for each DisplayImage). 
     * @return  The DisplayImage.   
     */
    public DisplayImage lookupDisplayImage(String imageId) {
        String sql = "SELECT * FROM displayImages WHERE imageId = ?";
        List<DisplayImage> displayImages = getJdbcTemplate().query(sql, new DisplayImageMapper(), imageId);        
        if (displayImages.isEmpty()) {
            return null;
        }   
        return displayImages.get(0);
    }
    
    /**
     * Looks up and retrieves a List of all DisplayImages from the persistence mechanism.
     * 
     * @return  The List of DisplayImages.   
     */
    public List<DisplayImage> getAllDisplayImages() {
        String sql = "SELECT * FROM displayImages ORDER BY dateCreated DESC";  
        List<DisplayImage> displayImages = getJdbcTemplate().query(sql, new DisplayImageMapper());         
        return displayImages;
    }

    /**
     * Finds and removes the DisplayImage from the persistence mechanism using the Image ID.
     * 
     * @param imageId  The ID of the Image. 
     */
    public void deleteDisplayImage(String imageId) {
        String sql = "DELETE FROM displayImages WHERE imageId = ?";
        int rowsAffected  = getJdbcTemplate().update(sql, imageId);
        if (rowsAffected <= 0) {
            throw new RecoverableDataAccessException("Unable to delete DisplayImage. No DisplayImage found with imageId: " + new Integer(imageId).toString());
        }   
    }
    
    /**
     * Creates a new DisplayImage in the persistence mechanism.
     * 
     * @param displayImage  The DisplayImage to be created. 
     */
    public void createDisplayImage(DisplayImage displayImage) {
        this.insertActor = new SimpleJdbcInsert(getDataSource()).withTableName("displayImages").usingGeneratedKeyColumns("id");
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(displayImage);
        Number newId = insertActor.executeAndReturnKey(parameters);
        displayImage.setId(newId.intValue());  
    }


    /***
     * Maps each row of the ResultSet to a DisplayImage object.
     */
    private static class DisplayImageMapper implements RowMapper<DisplayImage> {
        /**
         * Maps each row of data in the ResultSet to the DisplayImage object.
         * 
         * @param rs  The ResultSet to be mapped.
         * @param rowNum  The number of the current row.
         * @return  The populated DisplayImage object.
         * @throws SQLException  If a SQLException is encountered getting column values.
         */
        public DisplayImage mapRow(ResultSet rs, int rowNum) throws SQLException {
            DisplayImage displayImage = new DisplayImage();
            displayImage.setId(rs.getInt("id"));
            displayImage.setImageId(rs.getString("imageId"));
            return displayImage;
        }
    }

}
