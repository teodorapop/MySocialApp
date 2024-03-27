package com.example.mysocialapp.utils.events;

import com.example.mysocialapp.domain.User;


public class UserEvent implements Event {

    private final ChangeEventType type;
    private final User data;
    private User oldData;

    public UserEvent(ChangeEventType type, User data) {
        this.type = type;
        this.data = data;
    }
    public UserEvent(ChangeEventType type, User data, User oldData) {
        this.type = type;
        this.data = data;
        this.oldData=oldData;
    }

    public UserEvent(ChangeEventType type) {
        this.type = type;
        data = null;
    }

    public ChangeEventType getType() {
        return type;
    }

    public User getData() {
        return data;
    }

    public User getOldData() {
        return oldData;
    }
}
