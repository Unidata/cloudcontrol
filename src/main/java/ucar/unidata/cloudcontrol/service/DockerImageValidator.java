package edu.ucar.unidata.cloudcontrol.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import edu.ucar.unidata.cloudcontrol.domain.DockerImage;

@Component
public class DockerImageValidator implements Validator  {
  
    protected static Logger logger = Logger.getLogger(DockerImageValidator.class);

    private String[] NAUGHTY_STRINGS = {"<script>", "../", "svg", "javascript", "::", "&quot;", "fromcharCode", "%3", "$#", "alert(", ".js", ".source", "\\", "scriptlet", ".css", "binding:", ".htc", "vbscript", "mocha:", "livescript:", "base64", "\00", "xss:", "%77", "0x", "IS NULL;", "1;", "; --", "1=1"}; 
    private String[] NAUGHTY_CHARS = {"<", ">", "`", "^", "|", "}", "{"}; 

    private Pattern pattern;
    private Matcher matcher;

    public boolean supports(Class clazz) {
        return DockerImage.class.equals(clazz);
    }

    /**
     * Validates the DockerImage input contained in the DockerImage object.
     * 
     * @param obj  The target object to validate.
     * @param error  Object in which to store any validation errors.
     */
    public void validate(Object obj, Errors errors) {
        DockerImage DockerImage = (DockerImage) obj;
        validateItem("repository", DockerImage.getRepository(), errors); 
        validateItem("tag",  DockerImage.getTag(), errors); 
		validateItem("imageId",  DockerImage.getImageId(), errors); 
		validateItem("created",  DockerImage.getCreated(), errors); 
		validateItem("virtualSize",  DockerImage.getVirtualSize(), errors); 

    }

    /**
     * Validates the DockerImage input for the firstName and lastName fields.
     * 
     * @param formField  The form field corresponding to the DockerImage input.
     * @param input  The DockerImage input to validate.
     * @param error  Object in which to store any validation errors.
     */
    public void validateItem(String formField, String input, Errors errors) {
        if (StringUtils.isBlank(input)) {
            errors.rejectValue(formField, "DockerImage.error", formField + " is required!");
            return;
        }
        if ((StringUtils.length(input) < 6) || (StringUtils.length(input) > 75)) {
            errors.rejectValue(formField, "DockerImage.error", formField + " must be between 6 and 75 characters in length.");
            return;
        }  
        validateInput(formField, input, errors); 
    }    
     
    /**
     * A generic utility method to validate DockerImage input against known bad characters and strings.
     * 
     * @param formField  The form field corresponding to the DockerImage input.
     * @param input  The DockerImage input to validate.
     * @param error  Object in which to store any validation errors.
     */
    public void validateInput(String formField, String input, Errors errors) {
        String badChar = checkForNaughtyChars(input);
        if (badChar != null) {
            logger.warn("Bad value submitted for " + formField + " : " + badChar);
            errors.rejectValue(formField, "DockerImage.error", "Bad value submitted: " + badChar);
            return;
        }
        String badString = checkForNaughtyStrings(input);
        if (badString != null) {
            logger.warn("Bad value submitted for " + formField + " : " + badString);
            errors.rejectValue(formField, "DockerImage.error", "Bad value submitted: " + badString);
            return;
        }
    }

    /**
     * Validates the DockerImage input against known bad strings.
     * 
     * @param itemToCheck  The DockerImage input to validate.
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
     * Validates the DockerImage input against known bad characters.
     * 
     * @param itemToCheck  The DockerImage input to validate.
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
