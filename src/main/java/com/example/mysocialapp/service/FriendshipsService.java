package com.example.mysocialapp.service;
import com.example.mysocialapp.domain.Friendship;
import com.example.mysocialapp.domain.User;
import com.example.mysocialapp.repo.Repository;
import com.example.mysocialapp.utils.events.ChangeEventType;
import com.example.mysocialapp.utils.events.FriendshipEvent;
import com.example.mysocialapp.utils.observer.Observable;
import com.example.mysocialapp.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class FriendshipsService implements Observable<FriendshipEvent> {
    private final Repository<Long, Friendship> friendshipRepository;
    private final Repository<Long, User> userRepository;
    private final List<Observer<FriendshipEvent>> observers = new ArrayList<>();

    public FriendshipsService(Repository<Long, Friendship> repo_friend, Repository<Long, User> repo_user) {
        this.friendshipRepository = repo_friend;
        this.userRepository = repo_user;
    }

    public void addFriendship(Long id1, Long id2, LocalDateTime dateTime){
        verifyFriendship(id1, id2);
        Friendship friendship = new Friendship(id1, id2, dateTime);
        this.friendshipRepository.save(friendship);
        notifyObservers(new FriendshipEvent(ChangeEventType.ADD, friendship));
    }

    public void verifyFriendship(Long id1, Long id2) {
        User u1 = userRepository.getOne(id1);
        User u2 = userRepository.getOne(id2);
        if (u1 == null || u2 == null) {
            throw new IllegalArgumentException("One of the users does not exist!");
        }
    }

    public void deleteFriendship(Long id) {
        Friendship f = this.friendshipRepository.delete(id);
        notifyObservers(new FriendshipEvent(ChangeEventType.DELETE, f));
    }

    public Iterable<Friendship> getAllFriendships() {
        return this.friendshipRepository.getAll();
    }

    public void deleteFriendshipsWithThisUser(Long id) {
        List<Friendship> friendships = (List<Friendship>) this.friendshipRepository.getAll(); // Obțineți lista de prietenii

        // Inițializăm Iteratorul pentru lista de prietenii
        Iterator<Friendship> iterator = friendships.iterator();

        // Parcurgem lista de prietenii
        while (iterator.hasNext()) {
            // Obținem următorul element din listă
            Friendship friendship = iterator.next();

            // Verificăm dacă id-ul prieteniei corespunde cu id-ul dorit
            if (Objects.equals(friendship.getId1(), id) || Objects.equals(friendship.getId2(), id)) {
                // Dacă da, ștergem prietenia și actualizăm lista
                deleteFriendship(friendship.getId()); // Ștergeți prietenia
                iterator.remove(); // Eliminați prietenia din lista curentă
            }
        }
    }

    public void deleteFriendshipBetweenTwoUsers(Long id1, Long id2) {
        List<Friendship> friendships = (List<Friendship>) this.friendshipRepository.getAll(); // Obțineți lista de prietenii

        for (Iterator<Friendship> iterator = friendships.iterator(); iterator.hasNext();) {
            Friendship friendship = iterator.next();

            if ((Objects.equals(friendship.getId1(), id1) && Objects.equals(friendship.getId2(), id2))
                    || (Objects.equals(friendship.getId2(), id1) && Objects.equals(friendship.getId1(), id2))) {
                deleteFriendship(friendship.getId());
                iterator.remove();
            }
        }
    }



    @Override
    public void addObserver(Observer<FriendshipEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<FriendshipEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(FriendshipEvent t) {
        observers.forEach(x -> x.update(t));
    }


}
