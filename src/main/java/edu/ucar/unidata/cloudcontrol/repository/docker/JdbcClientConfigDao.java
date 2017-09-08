package edu.ucar.unidata.cloudcontrol.repository.docker;

import edu.ucar.unidata.cloudcontrol.domain.docker.ClientConfig;

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
 * The ClientConfigDao implementation.  Persistence mechanism is a database.
 */
public class JdbcClientConfigDao extends JdbcDaoSupport implements ClientConfigDao {

    protected static Logger logger = Logger.getLogger(JdbcClientConfigDao.class);

    private SimpleJdbcInsert insertActor;

    /**
     * Looks up and retrieves the ClientConfig from the persistence mechanism using the id.
     *
     * @param id   The id of the ClientConfig (will be unique for each ClientConfig).
     * @return  The ClientConfig.
     * @throws DataRetrievalFailureException  If unable to lookup ClientConfig with the given id.
     */
    public ClientConfig lookupById(int id) {
        logger.debug("Querying database for client config entry with id " + new Integer(id).toString());
        String sql = "SELECT * FROM clientConfig WHERE id = ?";
        logger.debug("SELECT * FROM clientConfig WHERE id = " + new Integer(id).toString());
        List<ClientConfig> clientConfigs = getJdbcTemplate().query(sql, new ClientConfigMapper(), id);
        if (clientConfigs.isEmpty()) {
            String message = "Unable to find ClientConfig with id " + new Integer(id).toString();
            logger.error(message);
            throw new DataRetrievalFailureException(message);
        }
        logger.debug("Client config entry found with id " + new Integer(id).toString());
        return clientConfigs.get(0);
    }

    /**
     * Looks up and retrieves a List of ClientConfigs from the persistence mechanism using the DOCKER_HOST value.
     *
     * @param dockerHost  The DOCKER_HOST value of the ClientConfig.
     * @return  The List of ClientConfigs.
     * @throws DataRetrievalFailureException  If unable to lookup any ClientConfigs with the given DOCKER_HOST value.
     */
    public List<ClientConfig> lookupByDockerHost(String dockerHost) {
        logger.debug("Querying database for client config entry with docker host " + dockerHost);
        String sql = "SELECT * FROM clientConfig WHERE dockerHost = ?";
        List<ClientConfig> clientConfigs = getJdbcTemplate().query(sql, new ClientConfigMapper(), dockerHost);
        logger.debug("SELECT * FROM clientConfig WHERE dockerHost = " + dockerHost);
        if (clientConfigs.isEmpty()) {
            logger.info("Unable to find any ClientConfigs with a DOCKER_HOST value of " + dockerHost);
        }
        return clientConfigs;
    }

    /**
     * Looks up and retrieves a List of ClientConfigs from the persistence mechanism using the DOCKER_CERT_PATH value.
     *
     * @param dockerCertPath  The DOCKER_CERT_PATH value of the ClientConfig.
     * @return  The List of ClientConfigs.
     * @throws DataRetrievalFailureException  If unable to lookup any ClientConfigs with the given DOCKER_CERT_PATH value.
     */
   public List<ClientConfig> lookupByDockerCertPath(String dockerCertPath) {
        logger.debug("Querying database for client config entry with docker cert path " + dockerCertPath);
        String sql = "SELECT * FROM clientConfig WHERE dockerCertPath = ?";
        logger.debug("SELECT * FROM clientConfig WHERE dockerCertPath = " + dockerCertPath);
        List<ClientConfig> clientConfigs = getJdbcTemplate().query(sql, new ClientConfigMapper(), dockerCertPath);
        if (clientConfigs.isEmpty()) {
            logger.info("Unable to find any ClientConfigs with a DOCKER_CERT_PATH value of " + dockerCertPath);
        }
        return clientConfigs;
    }

    /**
     * Looks up and retrieves a List of ClientConfigs from the persistence mechanism using the DOCKER_TLS_VERIFY value.
     *
     * @param dockerTlsVerify  The DOCKER_TLS_VERIFY value of the ClientConfig.
     * @return  The List of ClientConfigs.
     * @throws DataRetrievalFailureException  If unable to lookup any ClientConfigs with the given DOCKER_TLS_VERIFY value.
     */
    public List<ClientConfig> lookupByDockerTlsVerify(int dockerTlsVerify) {
        logger.debug("Querying database for client config entry with docker TLS verify " + new Integer(dockerTlsVerify).toString());
        String sql = "SELECT * FROM clientConfig WHERE dockerTlsVerify = ?";
        logger.debug("SELECT * FROM clientConfig WHERE dockerTlsVerify = " + new Integer(dockerTlsVerify).toString());
        List<ClientConfig> clientConfigs = getJdbcTemplate().query(sql, new ClientConfigMapper(), dockerTlsVerify);
        if (clientConfigs.isEmpty()) {
            logger.info("Unable to find any ClientConfigs with a DOCKER_TLS_VERIFY value of " + new Integer(dockerTlsVerify).toString());
        }
        return clientConfigs;
    }

    /**
     * Looks up and retrieves a List of ClientConfigs from the persistence mechanism using the user who created them.
     *
     * @param createdBy  The user who created the ClientConfigs.
     * @return  The List of ClientConfigs.
     * @throws DataRetrievalFailureException  If unable to lookup any ClientConfigs created by the user.
     */
    public List<ClientConfig> lookupByCreatedBy(String createdBy) {
        logger.debug("Querying database for client config entry created by " + createdBy);
        String sql = "SELECT * FROM clientConfig WHERE createdBy = ?";
        logger.debug("SELECT * FROM clientConfig WHERE createdBy = " + createdBy);
        List<ClientConfig> clientConfigs = getJdbcTemplate().query(sql, new ClientConfigMapper(), createdBy);
        if (clientConfigs.isEmpty()) {
            logger.info("Unable to find any ClientConfigs created by user " + createdBy);
        }
        return clientConfigs;
    }

    /**
     * Looks up and retrieves a List of ClientConfigs from the persistence mechanism using the user who last updated them.
     *
     * @param lastUpdatedBy  The user to last update the ClientConfigs.
     * @return  The List of ClientConfigs.
     * @throws DataRetrievalFailureException  If unable to lookup any ClientConfigs last updated by the user.
     */
    public List<ClientConfig> lookupByLastUpdatedBy(String lastUpdatedBy) {
        logger.debug("Querying database for client config entry last updated by " + lastUpdatedBy);
        String sql = "SELECT * FROM clientConfig WHERE lastUpdatedBy = ?";
        logger.debug("QSELECT * FROM clientConfig WHERE lastUpdatedBy = " + lastUpdatedBy);
        List<ClientConfig> clientConfigs = getJdbcTemplate().query(sql, new ClientConfigMapper(), lastUpdatedBy);
        if (clientConfigs.isEmpty()) {
            logger.info("Unable to find any ClientConfigs last updated by user " + lastUpdatedBy);
        }
        return clientConfigs;
    }

    /**
     * Looks up and retrieves a List of all ClientConfigs from the persistence mechanism.
     *
     * @return  The List of ClientConfigs.
     */
    public List<ClientConfig> getAllClientConfigs() {
        logger.debug("Querying database for all client config entries.");
        String sql = "SELECT * FROM clientConfig ORDER BY dateCreated DESC";
        logger.debug("SELECT * FROM clientConfig ORDER BY dateCreated DESC");
        List<ClientConfig> clientConfigs = getJdbcTemplate().query(sql, new ClientConfigMapper());
        if (clientConfigs.isEmpty()) {
            logger.info("No ClientConfigs persisted yet.");
        }
        return clientConfigs;
    }

    /**
     * Finds and removes the ClientConfig from the persistence mechanism using the id.
     *
     * @param id  The id of the ClientConfig to delete.
     * @throws DataRetrievalFailureException  If unable to locate and delete ClientConfig by id.
     */
    public void deleteClientConfig(int id) {
        logger.debug("Deleting client config entry in database with id " + new Integer(id).toString());
        String sql = "DELETE FROM clientConfig WHERE id = ?";
        logger.debug("DELETE FROM clientConfig WHERE id = " + new Integer(id).toString());
        int rowsAffected  = getJdbcTemplate().update(sql, id);
        if (rowsAffected <= 0) {
            String message = "Unable to delete ClientConfig. No ClientConfig found with id " + new Integer(id).toString();
            logger.error(message);
            throw new DataRetrievalFailureException(message);
        } else {
            logger.info("Deleted client config entry with id " + new Integer(id).toString());
        }
    }

    /**
     * Creates a new ClientConfig in the persistence mechanism.
     *
     * @param clientConfig  The ClientConfig to be created.
     * @return  The id of the newly created ClientConfig object.
     * @throws DataRetrievalFailureException  If unable to create the ClientConfig.
     */
    public int createClientConfig(ClientConfig clientConfig) {
        logger.debug("Creating entry in database for client config " + clientConfig.toString());
        logger.debug("Checking to see if client config already exists in the database... ");
        String sql = "SELECT * FROM clientConfig WHERE dockerHost = ? AND dockerCertPath = ?";
        logger.debug("SELECT * FROM clientConfig WHERE dockerHost = " + clientConfig.getDockerHost() + " AND dockerCertPath = " + clientConfig.getDockerCertPath());
        List<ClientConfig> clientConfigs = getJdbcTemplate().query(sql, new ClientConfigMapper(), new Object[] {clientConfig.getDockerHost(), clientConfig.getDockerCertPath()});
        if (!clientConfigs.isEmpty()) {
            String message = "Did not persist client config: " + clientConfig.toString() + ". Client config with docker host" + clientConfig.getDockerHost() + " and docker cert path " + clientConfig.getDockerCertPath() + " already exists.";
            logger.error(message);
            throw new DataRetrievalFailureException(message);
        } else {
              logger.info("time to persist");

            this.insertActor = new SimpleJdbcInsert(getDataSource()).withTableName("clientConfig").usingGeneratedKeyColumns("id");
            SqlParameterSource parameters = new BeanPropertySqlParameterSource(clientConfig);
            Number newId = insertActor.executeAndReturnKey(parameters);
            if (Objects.nonNull(newId)) {
                clientConfig.setId(newId.intValue());
                return newId.intValue();
            } else {
                String message = "Unable to create and persist ClientConfig " + clientConfig.toString();
                logger.error(message);
                throw new DataRetrievalFailureException(message);
            }
        }
    }

    /**
     * Saves changes made to an existing ClientConfig in the persistence mechanism.
     *
     * @param clientConfig  The existing ClientConfig with changes that needs to be saved.
     * @throws DataRetrievalFailureException  If unable to find the ClientConfig to update.
     */
    public void updateClientConfig(ClientConfig clientConfig) {
        logger.debug("Updating entry in database for client config " + clientConfig.toString());
        String sql = "UPDATE clientConfig SET dockerHost = ?, dockerCertPath = ?, dockerTlsVerify = ?, createdBy = ?, lastUpdatedBy = ?, dateModified = ? WHERE id = ?";
        logger.debug("UPDATE clientConfig SET dockerHost = " + clientConfig.getDockerHost() + ", dockerCertPath = " + clientConfig.getDockerCertPath() + ", dockerTlsVerify = " + new Integer(clientConfig.getDockerTlsVerify()).toString() + ", createdBy = " + clientConfig.getCreatedBy() + ", lastUpdatedBy = " + clientConfig.getLastUpdatedBy() + ", dateModified = " + clientConfig.getDateModified().toString() + " WHERE id = " + new Integer(clientConfig.getId()).toString());
        int rowsAffected  = getJdbcTemplate().update(sql, new Object[] {
            // order matters here
            clientConfig.getDockerHost(),
            clientConfig.getDockerCertPath(),
            clientConfig.getDockerTlsVerify(),
            clientConfig.getCreatedBy(),
            clientConfig.getLastUpdatedBy(),
            clientConfig.getDateModified(),
            clientConfig.getId()
        });
        if (rowsAffected  <= 0) {
            String message = "Unable to update ClientConfig.  No ClientConfig with id " + clientConfig.getId() + " found.";
            logger.error(message);
            throw new DataRetrievalFailureException(message);
        } else {
            logger.info("Updated client config " + clientConfig.toString());
        }
    }

    /***
     * Maps each row of the ResultSet to a ClientConfig object.
     */
    private static class ClientConfigMapper implements RowMapper<ClientConfig> {
        /**
         * Maps each row of data in the ResultSet to the ClientConfig object.
         *
         * @param rs  The ResultSet to be mapped.
         * @param rowNum  The number of the current row.
         * @return  The populated ClientConfig object.
         * @throws SQLException  If a SQLException is encountered getting column values.
         */
        public ClientConfig mapRow(ResultSet rs, int rowNum) throws SQLException {
            ClientConfig clientConfig = new ClientConfig();
            clientConfig.setId(rs.getInt("id"));
            clientConfig.setDockerHost(rs.getString("dockerHost"));
            clientConfig.setDockerCertPath(rs.getString("dockerCertPath"));
            clientConfig.setDockerTlsVerify(rs.getInt("dockerTlsVerify"));
            clientConfig.setCreatedBy(rs.getString("createdBy"));
            clientConfig.setLastUpdatedBy(rs.getString("lastUpdatedBy"));
            clientConfig.setDateCreated(rs.getTimestamp("dateCreated"));
            clientConfig.setDateModified(rs.getTimestamp("dateModified"));
            return clientConfig;
        }
    }
}
