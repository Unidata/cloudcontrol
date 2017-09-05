package edu.ucar.unidata.cloudcontrol.service.user.validators;

import edu.ucar.unidata.cloudcontrol.domain.User;
import edu.ucar.unidata.cloudcontrol.service.user.UserManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validator class for the form-backing object for modifying a User (sans password).
 */
@Component
public class EditUserValidator implements Validator  {

    protected static Logger logger = Logger.getLogger(EditUserValidator.class);

    private String[] NAUGHTY_STRINGS = {"<script>", "../", "svg", "javascript", "::", "&quot;", "fromcharCode", "%3", "$#", "alert(", ".js", ".source", "\\", "scriptlet", ".css", "binding:", ".htc", "vbscript", "mocha:", "livescript:", "base64", "\00", "xss:", "%77", "0x", "IS NULL;", "1;", "; --", "1=1"};
    private String[] NAUGHTY_CHARS = {"<", ">", "`", "^", "|", "}", "{"};

    private Pattern pattern;
    private Matcher matcher;

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final String USER_NAME_PATTERN = "^[a-zA-Z0-9_-]{2,}$";

    @Resource(name="userManager")
    private UserManager userManager;

    /**
     * Checks to see if Object class can be validated.
     *
     * @param clazz  The Object class to validate
     * @return true if class can be validated
     */
    public boolean supports(Class clazz) {
        return User.class.equals(clazz);
    }

    /**
     * Validates the user input contained in the User object.
     *
     * @param obj  The target object to validate.
     * @param error  Object in which to store any validation errors.
     */
    public void validate(Object obj, Errors errors) {
        User user = (User) obj;
        validateFullName(user.getFullName(), errors);
        validateAccessLevel(user.getAccessLevel(), errors);
        validateAccountStatus(user.getAccountStatus(), errors);
        validateEmailAddress(user.getEmailAddress(),  user.getUserName(), errors);
    }

    /**
     * Validates the user input for the fullName field.
     *
     * @param input  The user input to validate.
     * @param error  Object in which to store any validation errors.
     */
    public void validateFullName(String input, Errors errors) {
        if (StringUtils.isBlank(input)) {
            errors.rejectValue("fullName", "fullName.required");
            return;
        }
        if ((StringUtils.length(input) < 2) || (StringUtils.length(input) > 75)) {
            errors.rejectValue("fullName", "fullName.length");
            return;
        }
        validateInput("fullName", input, errors);
    }

    /**
     * Validates the user input for the emailAddress field.
     *
     * @param input  The user input to validate.
     * @param userName  The user name needed to validate.
     * @param error  Object in which to store any validation errors.
     */
     public void validateEmailAddress(String input, String userName, Errors errors) {
        if (StringUtils.isBlank(input)) {
            errors.rejectValue("emailAddress", "emailAddress.required");
            return;
        }
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            errors.rejectValue("emailAddress", "emailAddress.wellFormed");
            return;
        }
        try {
            User dbUser = userManager.lookupUserByEmailAddress(input);
            if (!userName.equals(dbUser.getUserName())) {
                errors.rejectValue("emailAddress", "emailAddress.alreadyInUse");
            }
            return;
        } catch (RecoverableDataAccessException e) {
            return;
        }
    }

    /**
     * Validates the admin input for the User's access level.
     *
     * @param input  The user input to validate.
     * @param error  Object in which to store any validation errors.
     */
    public void validateAccessLevel(int input, Errors errors) {
        if ((input > 2) || (input < 1)) {
            errors.rejectValue("accessLevel", "accessLevel.options");
            return;
        }
    }

    /**
     * Validates the admin input for the User's account status.
     *
     * @param input  The user input to validate.
     * @param error  Object in which to store any validation errors.
     */
    public void validateAccountStatus(int input, Errors errors) {
        if (input > 1) {
            errors.rejectValue("accountStatus", "accountStatus.options");
            return;
        }
    }

    /**
     * A generic utility method to validate user input against known bad characters and strings.
     *
     * @param formField  The form field corresponding to the user input.
     * @param input  The user input to validate.
     * @param error  Object in which to store any validation errors.
     */
    public void validateInput(String formField, String input, Errors errors) {
        String badChar = checkForNaughtyChars(input);
        if (Objects.nonNull(badChar)) {
            logger.warn("Bad value submitted for " + formField + " : " + badChar);
            errors.rejectValue(formField, "user.error", "This character is not allowed: " + badChar);
            return;
        }
        String badString = checkForNaughtyStrings(input);
        if (Objects.nonNull(badString)) {
            logger.warn("Bad value submitted for " + formField + " : " + badString);
            errors.rejectValue(formField, "user.error", "This string is not allowed: " + badString);
            return;
        }
    }

    /**
     * Validates the user input against known bad strings.
     *
     * @param itemToCheck  The user input to validate.
     * @return  The bad user input string, or null if input passes validation.
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
     * Validates the user input against known bad characters.
     *
     * @param itemToCheck  The user input to validate.
     * @return  The bad user input char, or null if input passes validation.
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
