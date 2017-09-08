package edu.ucar.unidata.cloudcontrol.repository.docker;

import edu.ucar.unidata.cloudcontrol.domain.docker.ImageMapping;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import org.apache.log4j.Logger;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * The ImageMappingDao implementation.  Persistence mechanism is a database.
 */
public class JdbcImageMappingDao extends JdbcDaoSupport implements ImageMappingDao {

    protected static Logger logger = Logger.getLogger(JdbcImageMappingDao.class);

    private SimpleJdbcInsert insertActor;

    /**
     * Looks up and retrieves the ImageMapping from the persistence mechanism using the Image ID.
     *
     * @param imageId   The ID of the Image (will be unique for each ImageMapping).
     * @return  The ImageMapping.
     * @throws DataRetrievalFailureException  If unable to locate ImageMapping by Image ID.
     */
    public ImageMapping lookupImageMapping(String imageId) {
        logger.debug("Querying database for image mapping entry for image with id " + imageId);
        String sql = "SELECT * FROM imageMapping WHERE imageId = ?";
        logger.debug("SELECT * FROM imageMapping WHERE imageId = " + imageId);
        List<ImageMapping> imageMappings = getJdbcTemplate().query(sql, new ImageMappingMapper(), imageId);
        if (imageMappings.isEmpty()) {
            String message = "Unable to find image mapping for image with id " + imageId;
            logger.error(message);
            throw new DataRetrievalFailureException(message);
        }
        logger.debug("Image mapping entry found for image with id " + imageId);
        return imageMappings.get(0);
    }

    /**
     * Looks up and retrieves a List of all ImageMappings from the persistence mechanism.
     *
     * @return  The List of ImageMappings.
     */
    public List<ImageMapping> getAllImageMappings() {
        logger.debug("Querying database for all image mapping entries.");
        String sql = "SELECT * FROM imageMapping";
        logger.debug("SELECT * FROM imageMapping");
        List<ImageMapping> imageMappings = getJdbcTemplate().query(sql, new ImageMappingMapper());
        if (imageMappings.isEmpty()) {
            logger.info("No ImageMappings persisted yet.");
        }
        return imageMappings;
    }

    /**
     * Finds and removes the ImageMapping from the persistence mechanism using the Image ID.
     *
     * @param imageId  The ID of the Image.
     * @throws DataRetrievalFailureException  If unable to locate and delete ImageMapping using the Image ID.
     */
    public void deleteImageMapping(String imageId) {
        logger.debug("Deleting image mapping entry in database for image with id " + imageId);
        String sql = "DELETE FROM imageMapping WHERE imageId = ?";
        logger.debug("DELETE FROM imageMapping WHERE imageId = " + imageId);
        int rowsAffected  = getJdbcTemplate().update(sql, imageId);
        if (rowsAffected <= 0) {
            String message = "Unable to delete ImageMapping. No ImageMapping found with imageId: " + imageId;
            logger.error(message);
            throw new DataRetrievalFailureException(message);
        } else {
            logger.info("Deleted image mapping entry for image with id " + imageId);
        }
    }

    /**
     * Creates a new ImageMapping in the persistence mechanism.
     *
     * @param imageMapping  The ImageMapping to be created.
     * @throws DataRetrievalFailureException  If unable to create and persist ImageMapping by Image ID.
     */
    public void createImageMapping(ImageMapping imageMapping) {
        logger.debug("Creating entry in database for image mapping " + imageMapping.toString());
        logger.debug("Checking to see if image mapping already exists in the database... ");
        String sql = "SELECT * FROM imageMapping WHERE imageId = ?";
        logger.debug("SELECT * FROM imageMapping WHERE imageId = " + imageMapping.getImageId());
        List<ImageMapping> imageMappings = getJdbcTemplate().query(sql, new ImageMappingMapper(), imageMapping.getImageId());
        if (!imageMappings.isEmpty()) {
            String message = "Did not persist image mapping: " + imageMapping.toString() + ". Image mapping for image with id " + imageMapping.getImageId() + " already exists.";
            logger.error(message);
            throw new DataRetrievalFailureException(message);
        } else {
            this.insertActor = new SimpleJdbcInsert(getDataSource()).withTableName("imageMapping").usingGeneratedKeyColumns("id");
            SqlParameterSource parameters = new BeanPropertySqlParameterSource(imageMapping);
            Number newId = insertActor.executeAndReturnKey(parameters);
            if (Objects.nonNull(newId)) {
                imageMapping.setId(newId.intValue());
                logger.info("Image mapping entry persisted in database for " + imageMapping.toString());
            } else {
                String message = "Unable to create and persist ImageMapping: " + imageMapping.toString();
                logger.error(message);
                throw new DataRetrievalFailureException(message);
            }
        }
    }


    /***
     * Maps each row of the ResultSet to a ImageMapping object.
     */
    private static class ImageMappingMapper implements RowMapper<ImageMapping> {
        /**
         * Maps each row of data in the ResultSet to the ImageMapping object.
         *
         * @param rs  The ResultSet to be mapped.
         * @param rowNum  The number of the current row.
         * @return  The populated ImageMapping object.
         * @throws SQLException  If a SQLException is encountered getting column values.
         */
        public ImageMapping mapRow(ResultSet rs, int rowNum) throws SQLException {
            ImageMapping imageMapping = new ImageMapping();
            imageMapping.setId(rs.getInt("id"));
            imageMapping.setImageId(rs.getString("imageId"));
            return imageMapping;
        }
    }

}
