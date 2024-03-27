package com.example.mysocialapp.utils.events;

import com.example.mysocialapp.domain.Friendship;


public class FriendshipEvent implements Event {
    private final ChangeEventType type;
    private final Friendship data;
    private Friendship oldData;

    public FriendshipEvent(ChangeEventType type, Friendship data) {
        this.type = type;
        this.data = data;
    }
    public FriendshipEvent(ChangeEventType type, Friendship data, Friendship oldData) {
        this.type = type;
        this.data = data;
        this.oldData=oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Friendship getData() {
        return data;
    }

    public Friendship getOldData() {
        return oldData;
    }
}
