package com.example.mysocialapp.domain.validators;



import com.example.mysocialapp.domain.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator implements Validator<User> {

    private static final String EMAIL_REGEX = "^(.+)@(.+)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Override
    public void validate(User user) throws ValidationException {
        if (user == null) {
            throw new ValidationException("Userul nu poate fi null");
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new ValidationException("Adresa de e-mail a utilizatorului este null sau goală");
        }
        if (!isValidEmail(user.getEmail())) {
            throw new ValidationException("Adresa de e-mail a utilizatorului nu este într-un format valid");
        }

        if (isValidName(user.getFirstName())) {
            throw new ValidationException("Numele utilizatorului conține caractere nepermise");
        }
        if (isValidName(user.getLastName())) {
            throw new ValidationException("NPrenumele utilizatorului conține caractere nepermise");
        }
    }

    private boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    private boolean isValidName(String name) {
        for (int i = 0; i < name.length(); i++) {
            char ch = name.charAt(i);
            if (!Character.isLetter(ch)) {
                return true;
            }
        }
        return false;
    }
}
