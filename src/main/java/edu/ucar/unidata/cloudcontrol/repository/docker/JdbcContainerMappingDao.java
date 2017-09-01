package edu.ucar.unidata.cloudcontrol.repository.docker;

import edu.ucar.unidata.cloudcontrol.domain.docker.ContainerMapping;
import edu.ucar.unidata.cloudcontrol.domain.docker._Container;
import edu.ucar.unidata.cloudcontrol.domain.docker._Image;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
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
 * The ContainerMappingDao implementation.  Persistence mechanism is a database.
 */
public class JdbcContainerMappingDao extends JdbcDaoSupport implements ContainerMappingDao {

    protected static Logger logger = Logger.getLogger(JdbcContainerMappingDao.class);

    private SimpleJdbcInsert insertActor;

    /**
     * Looks up and retrieves the ContainerMapping from the persistence mechanism using the container mapping ID.
     *
     * @param containerMappingId   The ID of the ContainerMapping object (will be unique for each ContainerMapping).
     * @return  The ContainerMapping.
     * @throws DataRetrievalFailureException  If unable to locate ContainerMapping by container mapping ID.
     */
    public ContainerMapping lookupContainerMappingById(int containerMappingId) {
        String sql = "SELECT * FROM containerMapping WHERE id = ?";
        List<ContainerMapping> containerMappings = getJdbcTemplate().query(sql, new ContainerMappingMapper(), containerMappingId);
        if (containerMappings.isEmpty()) {
            String message = "Unable to find ContainerMapping with id " + new Integer(containerMappingId).toString();
            logger.error(message);
            throw new DataRetrievalFailureException(message);
        }
        return containerMappings.get(0);
    }

    /**
     * Looks up and retrieves a List of all ContainerMappings by user name.
     *
     * @param userName  The user who created the ContainerMapping.
     * @return  The List of ContainerMappings.
     */
    public  List<ContainerMapping> lookupContainerMappingsByUserName(String userName){
        String sql = "SELECT * FROM containerMapping WHERE userName = ? ORDER BY datePerformed DESC";
        List<ContainerMapping> containerMappings = getJdbcTemplate().query(sql, new ContainerMappingMapper(), userName);
        if (containerMappings.isEmpty()) {
            logger.info("Unable to find any ContainerMappings made by user name " + userName);
        }
        return containerMappings;
    }

    /**
     * Looks up and retrieves the ContainerMapping from the persistence mechanism using the _Container.
     *
     * @param _container  The _container corresponding to the ContainerMapping object.
     * @return  The ContainerMapping.
     */
    public ContainerMapping lookupContainerMappingbyContainer(_Container _container){
        String sql = "SELECT * FROM containerMapping WHERE containerId = ? ORDER BY datePerformed DESC";
        List<ContainerMapping> containerMappings = getJdbcTemplate().query(sql, new ContainerMappingMapper(), _container.getId());
        if (containerMappings.isEmpty()) {
            logger.info("Unable to find any ContainerMappings for container" + _container.toString());
            return null;
        } else {
            return containerMappings.get(0);
        }
    }

    /**
     * Looks up and retrieves a List of all ContainerMappings by _Image.
     *
     * @param _Image  The _image corresponding to the ContainerMapping object.
     * @return  The List of ContainerMappings.
     */
    public  List<ContainerMapping> lookupContainerMappingsByImage(_Image _image){
        String sql = "SELECT * FROM containerMapping WHERE imageId = ? ORDER BY datePerformed DESC";
        List<ContainerMapping> containerMappings = getJdbcTemplate().query(sql, new ContainerMappingMapper(), _image.getId());
        if (containerMappings.isEmpty()) {
            logger.info("Unable to find any ContainerMappings for image " + _image.toString());
        }
        return containerMappings;
    }

    /**
     * Looks up and retrieves a List of all ContainerMappings by Date Performed.
     *
     * @param datePerformed  The date perfomed corresponding to the ContainerMapping object.
     * @return  The List of ContainerMappings.
     */
    public  List<ContainerMapping> lookupContainerMappingsByDatePerfomed(Date datePerformed){
        String sql = "SELECT * FROM containerMapping WHERE datePerformed = ? ORDER BY datePerformed DESC";
        List<ContainerMapping> containerMappings = getJdbcTemplate().query(sql, new ContainerMappingMapper(), datePerformed);
        if (containerMappings.isEmpty()) {
            logger.info("Unable to find any ContainerMappings for date " + datePerformed.toString());
        }
        return containerMappings;
    }

    /**
     * Looks up and retrieves a List of all ContainerMappings from the persistence mechanism.
     *
     * @return  The List of ContainerMappings.
     */
    public List<ContainerMapping> getAllContainerMappings() {
        String sql = "SELECT * FROM containerMapping ORDER BY datePerformed DESC";
        List<ContainerMapping> containerMappings = getJdbcTemplate().query(sql, new ContainerMappingMapper());
        if (containerMappings.isEmpty()) {
            logger.info("No ContainerMappings persisted yet.");
        }
        return containerMappings;
    }

    /**
     * Finds and removes the ContainerMapping from the persistence mechanism using the ContainerMapping ID.
     *
     * @param containerMappingId  The ID of the ContainerMapping object.
     * @throws DataRetrievalFailureException  If unable to locate and delete ContainerMapping by ID.
     */
    public void deleteContainerMapping(String containerMappingId) {
        String sql = "DELETE FROM containerMapping WHERE id = ?";
        int rowsAffected  = getJdbcTemplate().update(sql, containerMappingId);
        if (rowsAffected <= 0) {
            String message = "Unable to delete ContainerMapping. No ContainerMapping found with id: " + containerMappingId;
            logger.error(message);
            throw new DataRetrievalFailureException(message);
        }
    }

    /**
     * Creates a new ContainerMapping in the persistence mechanism.
     *
     * @param containerMapping  The ContainerMapping to be created.
     * @throws DataRetrievalFailureException  If unable to create and persist ContainerMapping.
     */
    public void createContainerMapping(ContainerMapping containerMapping) {
        this.insertActor = new SimpleJdbcInsert(getDataSource()).withTableName("containerMapping").usingGeneratedKeyColumns("id");
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(containerMapping);
        Number newId = insertActor.executeAndReturnKey(parameters);
        if (Objects.nonNull(newId)) {
            containerMapping.setId(newId.intValue());
        } else {
            String message = "Unable to create ContainerMapping: " + containerMapping.toString();
            logger.error(message);
            throw new DataRetrievalFailureException(message);
        }
    }

    /***
     * Maps each row of the ResultSet to a ContainerMapping object.
     */
    private static class ContainerMappingMapper implements RowMapper<ContainerMapping> {
        /**
         * Maps each row of data in the ResultSet to the ContainerMapping object.
         *
         * @param rs  The ResultSet to be mapped.
         * @param rowNum  The number of the current row.
         * @return  The populated ContainerMapping object.
         * @throws SQLException  If a SQLException is encountered getting column values.
         */
        public ContainerMapping mapRow(ResultSet rs, int rowNum) throws SQLException {
            ContainerMapping containerMapping = new ContainerMapping();
            containerMapping.setId(rs.getInt("id"));
            containerMapping.setContainerId(rs.getString("containerId"));
            containerMapping.setImageId(rs.getString("imageId"));
            containerMapping.setUserName(rs.getString("userName"));
            containerMapping.setDatePerformed(rs.getTimestamp("datePerformed"));
            return containerMapping;
        }
    }
}
