package edu.ucar.unidata.cloudcontrol.service.docker.validators;

import edu.ucar.unidata.cloudcontrol.domain.docker.ClientConfig;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validator class for the form-backing object for a ClientConfig creation.
 */
@Component
public class ClientConfigValidator implements Validator  {

    protected static Logger logger = Logger.getLogger(ClientConfigValidator.class);

    private String[] NAUGHTY_STRINGS = {"<script>", "../", "svg", "javascript", "::", "&quot;", "fromcharCode", "%3", "$#", "alert(", ".js", ".source", "\\", "scriptlet", ".css", "binding:", ".htc", "vbscript", "mocha:", "livescript:", "base64", "\00", "xss:", "%77", "0x", "IS NULL;", "1;", "; --", "1=1"};
    private String[] NAUGHTY_CHARS = {"<", ">", "`", "^", "|", "}", "{"};

    /**
     * Checks to see if Object class can be validated.
     *
     * @param clazz  The Object class to validate
     * @return true if class can be validated
     */
    public boolean supports(Class clazz) {
        return ClientConfig.class.equals(clazz);
    }

    /**
     * Validates the clientConfig input contained in the ClientConfig object.
     *
     * @param obj  The target object to validate.
     * @param error  Object in which to store any validation errors.
     */
    public void validate(Object obj, Errors errors) {
        ClientConfig clientConfig = (ClientConfig) obj;
        validateDockerHost(clientConfig.getDockerHost(), errors);
        validateDockerCertPath(clientConfig.getDockerCertPath(), errors);
        validateDockerTlsVerify(clientConfig.getDockerTlsVerify(), errors);
    }

    /**
     * Validates the user input for the dockerHost field.
     *
     * @param input  The clientConfig input to validate.
     * @param error  Object in which to store any validation errors.
     */
    public void validateDockerHost(String input, Errors errors) {
        logger.debug("Validating docker host client configuration.");
        if (StringUtils.isBlank(input)) {
            errors.rejectValue("dockerHost", "dockerHost.required");
            return;
        }
        if ((StringUtils.length(input) < 10) || (StringUtils.length(input) > 100)) {
            errors.rejectValue("dockerHost", "dockerHost.length");
            return;
        }
        validateInput("dockerHost", input, errors);
    }

    /**
     * Validates the user input for the dockerCertPath field.
     *
     * @param input  The clientConfig input to validate.
     * @param error  Object in which to store any validation errors.
     */
    public void validateDockerCertPath(String input, Errors errors) {
        logger.debug("Validating docker certificate path client configuration.");
        if (StringUtils.isBlank(input)) {
            errors.rejectValue("dockerCertPath", "dockerCertPath.required");
            return;
        }
        if ((StringUtils.length(input) < 8) || (StringUtils.length(input) > 120)) {
            errors.rejectValue("dockerCertPath", "dockerCertPath.length");
            return;
        }
        validateInput("dockerCertPath", input, errors);
    }

    /**
     * Validates the user input for the dockerTlsVerify field.
     *
     * @param input  The clientConfig input to validate.
     * @param error  Object in which to store any validation errors.
     */
     public void validateDockerTlsVerify(int input, Errors errors) {
        logger.debug("Validating docker TLS verify client configuration.");
        if (input != 1){
            errors.rejectValue("dockerTlsVerify", "dockerTlsVerify.wellFormed");
            return;
        }
    }

    /**
     * A generic utility method to validate clientConfig input against known bad characters and strings.
     *
     * @param formField  The form field corresponding to the clientConfig input.
     * @param input  The clientConfig input to validate.
     * @param error  Object in which to store any validation errors.
     */
    public void validateInput(String formField, String input, Errors errors) {
        String badChar = checkForNaughtyChars(input);
        if (Objects.nonNull(badChar)) {
            logger.warn("Bad value submitted for " + formField + " : " + badChar);
            errors.rejectValue(formField, "clientConfig.error", "This character is not allowed: " + badChar);
            return;
        }
        String badString = checkForNaughtyStrings(input);
        if (Objects.nonNull(badString)) {
            logger.warn("Bad value submitted for " + formField + " : " + badString);
            errors.rejectValue(formField, "clientConfig.error", "This string is not allowed: " + badString);
            return;
        }
    }

    /**
     * Validates the clientConfig input against known bad strings.
     *
     * @param itemToCheck  The clientConfig input to validate.
     * @return  The bad clientConfig input string, or null if input passes validation.
     */
    public String checkForNaughtyStrings(String itemToCheck) {
        for (String item : NAUGHTY_STRINGS) {
            if (StringUtils.contains(StringUtils.lowerCase(itemToCheck), item)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Validates the clientConfig input against known bad characters.
     *
     * @param itemToCheck  The clientConfig input to validate.
     * @return  The bad clientConfig input char, or null if input passes validation.
     */
    public String checkForNaughtyChars(String itemToCheck) {
        for (String item : NAUGHTY_CHARS) {
            if (StringUtils.contains(itemToCheck, item)) {
                return item;
            }
        }
        return null;
    }
}
