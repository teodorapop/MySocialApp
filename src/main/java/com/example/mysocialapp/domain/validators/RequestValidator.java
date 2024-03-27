package com.example.mysocialapp.domain.validators;



import com.example.mysocialapp.domain.Request;

import java.util.Objects;

public class RequestValidator implements Validator<Request> {
    @Override
    public void validate(Request entity) throws ValidationException {
        if(entity.getId() == null) {
            throw new ValidationException("ID-ul nu poate sa fie null!");
        }
        if(entity.getTo() == null || entity.getFrom() == null ){
            throw new ValidationException("To sau From ID nu poate sa fie null");
        }

        if (!Objects.equals(entity.getStatus(), "approved") &&
                !Objects.equals(entity.getStatus(), "rejected") &&
                !Objects.equals(entity.getStatus(), "pending")){
            throw new ValidationException("Status poate sa fie approved, rejected sau pending");
        }


        // all good
    }
}
