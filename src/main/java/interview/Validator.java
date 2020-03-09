package interview;

import org.apache.commons.validator.routines.UrlValidator;

public class Validator{
    public void ValidateUrl(String url, String errorMsg) throws Exception {
        UrlValidator defaultValidator = new UrlValidator();
        if (!defaultValidator.isValid(url)) {
            throw new Exception(errorMsg);
        }
    }

    public void ValidateInt(String number, String errorMsg) throws Exception {
        try {
            Integer.parseInt(number);
        } catch (NumberFormatException nfe) {
            throw new Exception(errorMsg);
        }
    }
}
