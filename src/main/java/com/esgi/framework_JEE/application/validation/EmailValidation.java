package com.esgi.framework_JEE.application.validation;

import java.util.regex.Pattern;

public class EmailValidation {


    public EmailValidation(){
    }

    public boolean isValid(String email) {

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);

        return  pat.matcher(email).matches();

    }


}
