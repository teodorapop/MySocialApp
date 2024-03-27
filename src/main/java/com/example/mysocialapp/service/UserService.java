package com.example.mysocialapp.service;



import com.example.mysocialapp.domain.User;
import com.example.mysocialapp.domain.validators.ValidationException;
import com.example.mysocialapp.repo.Repository;
import com.example.mysocialapp.utils.events.UserEvent;
import com.example.mysocialapp.utils.observer.Observable;
import com.example.mysocialapp.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;


public class UserService implements Observable<UserEvent> {


    private final Repository<Long, User> userRepository;
    private final List<Observer<UserEvent>> observers = new ArrayList<>();

    public UserService(Repository<Long, User> userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(String firstName, String lastName, String email, String password, String gender) throws ValidationException {
        User user = new User(firstName,lastName,email,password,gender);
        System.out.println(user.getId());
        this.userRepository.save(user);
    }

    public void deleteUser(Long id) {
        this.userRepository.delete(id);
    }

    public void updateUser(Long id, String first_name, String last_name, String email, String password, String gender) throws ValidationException {
        User updated_user = new User(first_name, last_name, email, password, gender);
        updated_user.setId(id);
        this.userRepository.update(updated_user);
    }

    public Iterable<User> getAllUsers() {
        return this.userRepository.getAll();
    }

    public User getOne(Long id) {
        return this.userRepository.getOne(id);
    }

    @Override
    public void addObserver(Observer<UserEvent> e) {
        observers.add(e);
    }


    @Override
    public void removeObserver(Observer<UserEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(UserEvent t) {
        observers.forEach(x -> x.update(t));
    }


    public void addUser(User u) {
        this.userRepository.save(u);
    }

}
