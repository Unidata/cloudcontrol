package edu.ucar.unidata.cloudcontrol.service.user.validators;

import edu.ucar.unidata.cloudcontrol.domain.User;

import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validator class for the form-backing object for resetting a User's password.
 */
@Component
public class PasswordValidator implements Validator  {

    protected static Logger logger = Logger.getLogger(PasswordValidator.class);

    private String[] NAUGHTY_STRINGS = {"<script>", "../", "svg", "javascript", "::", "&quot;", "fromcharCode", "%3", "$#", "alert(", ".js", ".source", "\\", "scriptlet", ".css", "binding:", ".htc", "vbscript", "mocha:", "livescript:", "base64", "\00", "xss:", "%77", "0x", "IS NULL;", "1;", "; --", "1=1"};
    private String[] NAUGHTY_CHARS = {"<", ">", "`", "^", "|", "}", "{"};

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
        validatePassword("password",  user.getPassword(), errors);
        validatePassword("confirmPassword", user.getConfirmPassword(), errors);
        comparePasswords(user.getPassword(), user.getConfirmPassword(), errors);
    }

    /**
     * Validates the user input for the various password fields.
     *
     * @param formField  The form field corresponding to the user input.
     * @param input  The user input to validate.
     * @param error  Object in which to store any validation errors.
     */
     public void validatePassword(String formField, String input, Errors errors) {
        logger.debug("Validating user password.");
        if (StringUtils.isBlank(input)) {
            errors.rejectValue(formField, "password.required");
            return;
        }
        if ((StringUtils.length(input) < 8) || (StringUtils.length(input) > 25)) {
            errors.rejectValue(formField, "password.length");
            return;
        }
        validateInput(formField, input, errors);
    }

    /**
     * Validates password and confirmation passwords to make sure they are the same.
     *
     * @param password  The password specified by the user.
     * @param confirmPassword  The confirmation of the password.
     * @param error  Object in which to store any validation errors.
     */
    public void comparePasswords(String password, String confirmPassword, Errors errors) {
        logger.debug("Comparing user password and confirm password entries.");
        if (!StringUtils.equals(password, confirmPassword)) {
            errors.rejectValue("confirmPassword", "password.match");
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
