package com.example.mysocialapp.domain.validators;

//public interface Validator<T> {
//    void validate(T entity) throws ValidationException;
//}


public interface Validator<T> {
    void validate(T entity) throws ValidationException;;
}