package edu.ucar.unidata.cloudcontrol.service.docker;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import edu.ucar.unidata.cloudcontrol.domain.docker.NewContainer;

@Component
public class NewContainerValidator implements Validator  {
  
    protected static Logger logger = Logger.getLogger(NewContainerValidator.class);

    private String[] NAUGHTY_STRINGS = {"<script>", "../", "svg", "javascript", "::", "&quot;", "fromcharCode", "%3", "$#", "alert(", ".js", ".source", "\\", "scriptlet", ".css", "binding:", ".htc", "vbscript", "mocha:", "livescript:", "base64", "\00", "xss:", "%77", "0x", "IS NULL;", "1;", "; --", "1=1"}; 
    private String[] NAUGHTY_CHARS = {"<", ">", "`", "^", "|", "}", "{"}; 

    private Pattern pattern;
    private Matcher matcher;

    public boolean supports(Class clazz) {
		logger.info(String.valueOf(NewContainer.class.isAssignableFrom(clazz)));
        return NewContainer.class.isAssignableFrom(clazz);
    }

    /**
     * Validates the user input contained in the NewContainer object.
     * 
     * @param obj  The target object to validate.
     * @param error  Object in which to store any validation errors.
     */
    public void validate(Object obj, Errors errors) {
        NewContainer newContainer = (NewContainer) obj;
        validateName(newContainer.getName(), errors);  
        validatePortNumber(newContainer.getPortNumber(), errors); 
        validateHostName(newContainer.getHostName(), errors);
    }

    /**
     * Validates the user input for the name field.
     * 
     * @param input  The user input to validate.
     * @param error  Object in which to store any validation errors.
     */
    public void validateName(String input, Errors errors) {
    }

    /**
     * Validates the user input for the port number field.
     * 
     * @param input  The NewContainer input to validate.
     * @param error  Object in which to store any validation errors.
     */
    public void validatePortNumber(int input, Errors errors) {
    }    
   
    /**
     * Validates the user input for the host name field.
     * 
     * @param input  The NewContainer input to validate.
     * @param error  Object in which to store any validation errors.
     */
    public void validateHostName(String input, Errors errors) {
    }    
	
    /**
     * A generic utility method to validate NewContainer input against known bad characters and strings.
     * 
     * @param formField  The form field corresponding to the NewContainer input.
     * @param input  The NewContainer input to validate.
     * @param error  Object in which to store any validation errors.
     */
    public void validateInput(String formField, String input, Errors errors) {
        String badChar = checkForNaughtyChars(input);
        if (badChar != null) {
            logger.warn("Bad value submitted for " + formField + " : " + badChar);
            errors.rejectValue(formField, "newContainer.error", "Bad value submitted: " + badChar);
            return;
        }
        String badString = checkForNaughtyStrings(input);
        if (badString != null) {
            logger.warn("Bad value submitted for " + formField + " : " + badString);
            errors.rejectValue(formField, "newContainer.error", "Bad value submitted: " + badString);
            return;
        }
    }

    /**
     * Validates the NewContainer input against known bad strings.
     * 
     * @param itemToCheck  The NewContainer input to validate.
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
     * Validates the NewContainer input against known bad characters.
     * 
     * @param itemToCheck  The NewContainer input to validate.
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
