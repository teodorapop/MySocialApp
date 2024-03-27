package com.example.mysocialapp.domain.validators;


import com.example.mysocialapp.domain.Message;

public class MessageValidator implements Validator<Message> {

    @Override
    public void validate(Message entity) throws ValidationException {
        if (entity.getId() == null) throw new ValidationException("ID nu poate sa fie null");

        if (entity.getTo() == null) throw new ValidationException("To: nu poate sa fie null");

        if (entity.getFrom() == null) throw new ValidationException("From: nu poate sa fie null");


    }
}
