package com.example.mysocialapp.domain.validators;


import com.example.mysocialapp.domain.Friendship;

public class FriendshipValidator implements Validator<Friendship> {

    @Override
    public void validate(Friendship entity) throws ValidationException {

        if(entity.getId() == null){
            throw new ValidationException("ID-ul entitatii nu poate sa fie null!");
        }

        if (entity.getId1() == null || entity.getId2() == null ){
            throw new ValidationException("ID-ul nu poate sa fie null!");
        }


    }
}
