package edu.ucar.unidata.cloudcontrol.service.user;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import edu.ucar.unidata.cloudcontrol.domain.User;

@Component
public class UserValidator implements Validator  {
  
    protected static Logger logger = Logger.getLogger(UserValidator.class);

    private String[] NAUGHTY_STRINGS = {"<script>", "../", "svg", "javascript", "::", "&quot;", "fromcharCode", "%3", "$#", "alert(", ".js", ".source", "\\", "scriptlet", ".css", "binding:", ".htc", "vbscript", "mocha:", "livescript:", "base64", "\00", "xss:", "%77", "0x", "IS NULL;", "1;", "; --", "1=1"}; 
    private String[] NAUGHTY_CHARS = {"<", ">", "`", "^", "|", "}", "{"}; 

    private Pattern pattern;
    private Matcher matcher;
 
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final String USER_NAME_PATTERN = "^[a-zA-Z0-9_-]{2,}$";


    public boolean supports(Class clazz) {
        return User.class.equals(clazz);
    }

    /**
     * Validates the User input contained in the User object.
     * 
     * @param obj  The target object to validate.
     * @param error  Object in which to store any validation errors.
     */
    public void validate(Object obj, Errors errors) {
        User user = (User) obj;
        validateUserName(user.getUserName(), errors);  
        validateName("firstName", user.getFirstName(), errors); 
        validateName("lastName",  user.getLastName(), errors); 
        validateEmailAddress(user.getEmailAddress(), errors);
    }

    /**
     * Validates the User input for the userName field.
     * 
     * @param input  The User input to validate.
     * @param error  Object in which to store any validation errors.
     */
    public void validateUserName(String input, Errors errors) {
        if (StringUtils.isBlank(input)) {
            errors.rejectValue("userName", "user.error", "User name is required!");
            return;
        }
        if ((StringUtils.length(input) < 2) || (StringUtils.length(input) > 50)) {
            errors.rejectValue("userName", "user.error", "The userName must be between 2 and 50 characters in length.");
            return;
        }        
        pattern = Pattern.compile(USER_NAME_PATTERN);
        matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            errors.rejectValue("userName", "user.error", "Only alphanumeric characters, dashed and underscores are allowed. (Spaces are NOT allowed.)");
            return;
        }
    }

    /**
     * Validates the User input for the firstName and lastName fields.
     * 
     * @param formField  The form field corresponding to the User input.
     * @param input  The User input to validate.
     * @param error  Object in which to store any validation errors.
     */
    public void validateName(String formField, String input, Errors errors) {
        if (StringUtils.isBlank(input)) {
            errors.rejectValue(formField, "user.error", formField + " is required!");
            return;
        }
        if ((StringUtils.length(input) < 2) || (StringUtils.length(input) > 75)) {
            errors.rejectValue(formField, "user.error", formField + " must be between 2 and 75 characters in length.");
            return;
        }  
        validateInput(formField, input, errors); 
    }    
     
   
    /**
     * Validates the User input for the emailAddress field.
     * 
     * @param input  The User input to validate.
     * @param error  Object in which to store any validation errors.
     */    
     public void validateEmailAddress(String input, Errors errors) {
        if (StringUtils.isBlank(input)) {
            errors.rejectValue("emailAddress", "user.error", "User email address is required!");
            return;
        }
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            errors.rejectValue("emailAddress", "user.error", "This is not a well-formed email address.");
            return;
        }  
    }

    /**
     * A generic utility method to validate User input against known bad characters and strings.
     * 
     * @param formField  The form field corresponding to the User input.
     * @param input  The User input to validate.
     * @param error  Object in which to store any validation errors.
     */
    public void validateInput(String formField, String input, Errors errors) {
        String badChar = checkForNaughtyChars(input);
        if (badChar != null) {
            logger.warn("Bad value submitted for " + formField + " : " + badChar);
            errors.rejectValue(formField, "user.error", "Bad value submitted: " + badChar);
            return;
        }
        String badString = checkForNaughtyStrings(input);
        if (badString != null) {
            logger.warn("Bad value submitted for " + formField + " : " + badString);
            errors.rejectValue(formField, "user.error", "Bad value submitted: " + badString);
            return;
        }
    }

    /**
     * Validates the User input against known bad strings.
     * 
     * @param itemToCheck  The User input to validate.
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
     * Validates the User input against known bad characters.
     * 
     * @param itemToCheck  The User input to validate.
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
