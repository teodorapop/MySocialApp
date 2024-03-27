package com.example.mysocialapp.domain;



import com.example.mysocialapp.utils.events.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class User extends Entity<Long> implements Event {
    private String firstName;
    private String lastName;
    private final List<User> friends = new ArrayList<>();
    private String email;
    private String password;
    private String gender;


    public User(String firstName, String lastName, String email, String password, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.gender = gender;
        setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String information() {
        return "Utilizator{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public String toString() {

        StringBuilder result = new StringBuilder();
        result.append(this.information());
        result.append(" FRIENDS: ");
        for(User user : friends){
            result.append(user).append(", ");
        }
        return result.toString();


        /*
        StringBuilder result = new StringBuilder();
        result.append(this.information());
        result.append(Constants.ANSI_RED);
        result.append(" FRIENDS: ");
        result.append(Constants.ANSI_RESET);
        for (User user : friends) {
            result.append(user.information());
            result.append(", ");
        }
        return result.toString();*/

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User that)) return false;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getFriends().equals(that.getFriends()) &&
                getEmail().equals(that.getEmail()) &&
                getPassword().equals(that.getPassword())
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getFriends(), getEmail(), getPassword());
    }

    public List<User> getFriends() {
        return friends;
    }

    public void addFriend(User u) {
        this.friends.add(u);
    }

    public void deleteFriend(User u) {
        this.friends.remove(u);
    }

}
