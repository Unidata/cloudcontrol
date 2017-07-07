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

import edu.ucar.unidata.cloudcontrol.domain.docker.ClientConfig;

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
     * @throws RecoverableDataAccessException  If unable to lookup ClientConfig with the given id.
     */
    public ClientConfig lookupById(int id) {
        String sql = "SELECT * FROM clientConfig WHERE id = ?";
        List<ClientConfig> clientConfigs = getJdbcTemplate().query(sql, new ClientConfigMapper(), id);
        if (clientConfigs.isEmpty()) {
            throw new RecoverableDataAccessException("Unable to find ClientConfig with id: " + new Integer(id).toString());
        }
        return clientConfigs.get(0);
    }

    /**
     * Looks up and retrieves a List of ClientConfigs from the persistence mechanism using the DOCKER_HOST value.
     *
     * @param dockerHost  The DOCKER_HOST value of the ClientConfig.
     * @return  The List of ClientConfigs.
     * @throws RecoverableDataAccessException  If unable to lookup any ClientConfigs with the given DOCKER_HOST value.
     */
    public List<ClientConfig> lookupByDockerHost(String dockerHost) {
        String sql = "SELECT * FROM clientConfig WHERE dockerHost = ?";
        List<ClientConfig> clientConfigs = getJdbcTemplate().query(sql, new ClientConfigMapper(), dockerHost);
        if (clientConfigs.isEmpty()) {
            throw new RecoverableDataAccessException("Unable to find any ClientConfigs with a DOCKER_HOST value of: " + dockerHost);
        }
        return clientConfigs;
    }

    /**
     * Looks up and retrieves a List of ClientConfigs from the persistence mechanism using the DOCKER_CERT_PATH value.
     *
     * @param dockerCertPath  The DOCKER_CERT_PATH value of the ClientConfig.
     * @return  The List of ClientConfigs.
     * @throws RecoverableDataAccessException  If unable to lookup any ClientConfigs with the given DOCKER_CERT_PATH value.
     */
   public List<ClientConfig> lookupByDockerCertPath(String dockerCertPath) {
        String sql = "SELECT * FROM clientConfig WHERE dockerCertPath = ?";
        List<ClientConfig> clientConfigs = getJdbcTemplate().query(sql, new ClientConfigMapper(), dockerCertPath);
        if (clientConfigs.isEmpty()) {
            throw new RecoverableDataAccessException("Unable to find any ClientConfigs with a DOCKER_CERT_PATH value of: " + dockerCertPath);
        }
        return clientConfigs;
    }

    /**
     * Looks up and retrieves a List of ClientConfigs from the persistence mechanism using the DOCKER_TLS_VERIFY value.
     *
     * @param dockerTlsVerify  The DOCKER_TLS_VERIFY value of the ClientConfig.
     * @return  The List of ClientConfigs.
     * @throws RecoverableDataAccessException  If unable to lookup any ClientConfigs with the given DOCKER_TLS_VERIFY value.
     */
    public List<ClientConfig> lookupByDockerTlsVerify(int dockerTlsVerify) {
        String sql = "SELECT * FROM clientConfig WHERE dockerTlsVerify = ?";
        List<ClientConfig> clientConfigs = getJdbcTemplate().query(sql, new ClientConfigMapper(), dockerTlsVerify);
        if (clientConfigs.isEmpty()) {
            throw new RecoverableDataAccessException("Unable to find any ClientConfigs with a DOCKER_TLS_VERIFY value of: " + new Integer(dockerTlsVerify).toString());
        }
        return clientConfigs;
    }

    /**
     * Looks up and retrieves a List of ClientConfigs from the persistence mechanism using the user who created them.
     *
     * @param createdBy  The user who created the ClientConfigs.
     * @return  The List of ClientConfigs.
     * @throws RecoverableDataAccessException  If unable to lookup any ClientConfigs created by the user.
     */
    public List<ClientConfig> lookupByCreatedBy(String createdBy) {
        String sql = "SELECT * FROM clientConfig WHERE createdBy = ?";
        List<ClientConfig> clientConfigs = getJdbcTemplate().query(sql, new ClientConfigMapper(), createdBy);
        if (clientConfigs.isEmpty()) {
            throw new RecoverableDataAccessException("Unable to find any ClientConfigs created by user: " + createdBy);
        }
        return clientConfigs;
    }

    /**
     * Looks up and retrieves a List of ClientConfigs from the persistence mechanism using the user who last updated them.
     *
     * @param lastUpdatedBy  The user to last update the ClientConfigs.
     * @return  The List of ClientConfigs.
     * @throws RecoverableDataAccessException  If unable to lookup any ClientConfigs last updated by the user.
     */
    public List<ClientConfig> lookupByLastUpdatedBy(String lastUpdatedBy) {
        String sql = "SELECT * FROM clientConfig WHERE lastUpdatedBy = ?";
        List<ClientConfig> clientConfigs = getJdbcTemplate().query(sql, new ClientConfigMapper(), lastUpdatedBy);
        if (clientConfigs.isEmpty()) {
            throw new RecoverableDataAccessException("Unable to find any ClientConfigs last updated by user: " + lastUpdatedBy);
        }
        return clientConfigs;
    }

    /**
     * Looks up and retrieves a List of all ClientConfigs from the persistence mechanism.
     *
     * @return  The List of ClientConfigs.
     * @throws RecoverableDataAccessException  If unable to lookup any ClientConfigs.
     */
    public List<ClientConfig> getAllClientConfigs() {
        String sql = "SELECT * FROM clientConfig ORDER BY dateCreated DESC";
        List<ClientConfig> clientConfigs = getJdbcTemplate().query(sql, new ClientConfigMapper());
        return clientConfigs;
    }

    /**
     * Finds and removes the ClientConfig from the persistence mechanism using the id.
     *
     * @param id  The id of the ClientConfig to delete.
     */
    public void deleteClientConfig(int id) {
        String sql = "DELETE FROM clientConfig WHERE id = ?";
        int rowsAffected  = getJdbcTemplate().update(sql, id);
        if (rowsAffected <= 0) {
            throw new RecoverableDataAccessException("Unable to delete ClientConfig. No ClientConfig found with id: " + new Integer(id).toString());
        }
    }

    /**
     * Creates a new ClientConfig in the persistence mechanism.
     *
     * @param clientConfig  The ClientConfig to be created.
     * @return  The id of the newly created ClientConfig object.
     * @throws RecoverableDataAccessException  If unable to create the ClientConfig.
     */
    public int createClientConfig(ClientConfig clientConfig) {
        this.insertActor = new SimpleJdbcInsert(getDataSource()).withTableName("clientConfig").usingGeneratedKeyColumns("id");
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(clientConfig);
        Number newId = insertActor.executeAndReturnKey(parameters);
        if (newId != null) {
            clientConfig.setId(newId.intValue());
            return newId.intValue();
        } else {
            throw new RecoverableDataAccessException("Unable to create ClientConfig: " + clientConfig.toString());
        }
    }

    /**
     * Saves changes made to an existing ClientConfig in the persistence mechanism.
     *
     * @param clientConfig  The existing ClientConfig with changes that needs to be saved.
     * @throws RecoverableDataAccessException  If unable to find the ClientConfig to update.
     */
    public void updateClientConfig(ClientConfig clientConfig) {
        String sql = "UPDATE clientConfig SET dockerHost = ?, dockerCertPath = ?, dockerTlsVerify = ?, createdBy = ?, lastUpdatedBy = ?, dateModified = ? WHERE id = ?";
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
            throw new RecoverableDataAccessException("Unable to update ClientConfig.  No ClientConfig with id: " + clientConfig.getId() + " found.");
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
