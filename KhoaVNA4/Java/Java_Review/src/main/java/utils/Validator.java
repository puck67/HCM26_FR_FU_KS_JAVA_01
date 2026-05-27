package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Validator {

    public static boolean isValidBirthDate(String birthDate) {
        if (birthDate == null || birthDate.trim().isEmpty()) {
            return false;
        }
        if (!Constants.BIRTH_DATE_PATTERN.matcher(birthDate).matches()) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            Date date = sdf.parse(birthDate);
            return date.before(new Date());
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isValidPhone(CharSequence phone) {
        if (phone == null) {
            return false;
        }
        return Constants.PHONE_PATTERN.matcher(phone).matches();
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null) {
            return false;
        }
        return Constants.PHONE_PATTERN.matcher(phone).matches();
    }

    public static boolean isValidEmail(CharSequence email) {
        if (email == null) {
            return false;
        }
        return Constants.EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        return Constants.EMAIL_PATTERN.matcher(email).matches();
    }
}
