package com.example.mysocialapp.service;

import com.example.mysocialapp.domain.Friendship;
import com.example.mysocialapp.domain.Request;
import com.example.mysocialapp.domain.User;
import com.example.mysocialapp.repo.Repository;
import com.example.mysocialapp.utils.events.ChangeEventType;
import com.example.mysocialapp.utils.events.RequestEvent;
import com.example.mysocialapp.utils.observer.Observable;
import com.example.mysocialapp.utils.observer.Observer;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class RequestsService implements Observable<RequestEvent> {
    private final Repository<Long, Request> requestRepository;
    private final Repository<Long, User> userRepository;
    private final Repository<Long, Friendship> friendshipRepository;

    private final List<Observer<RequestEvent>> observers = new ArrayList<>();


    public RequestsService(Repository<Long, Request> repo,
                           Repository<Long, User> userRepository, Repository<Long, Friendship> friendshipRepository) {
        this.requestRepository = repo;
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
    }

    public void addRequest(Long id_from, Long id_to) {
        HashSet<Friendship> all_friendships = (HashSet<Friendship>) this.friendshipRepository.getAll();
        if (all_friendships.stream().noneMatch(f -> (
                Objects.equals(f.getId1(), id_from) && Objects.equals(f.getId2(), id_to)) ||
                (Objects.equals(f.getId2(), id_from) && Objects.equals(f.getId1(), id_to)))) {
            Request u = new Request(id_from, id_to, "pending", LocalDateTime.now());
            this.requestRepository.save(u);
            notifyObservers(new RequestEvent(ChangeEventType.ADD, u));
        } else {
            throw new IllegalArgumentException("This friendship already exists!");
        }
    }


//    public void deleteRequest(Long id) {
//        // Verificați dacă cererea există
//        if (this.requestRepository.getOne(id)!=null) {
//            // Ștergeți cererea de prietenie
//            this.requestRepository.delete(id);
//        } else {
//            throw new IllegalArgumentException("Request with id " + id + " does not exist.");
//        }
//    }
    public void deleteRequest(Long id) {
        Request request = this.requestRepository.getOne(id);
        Request r = this.requestRepository.delete(id);
        if (r == null) {
            notifyObservers(new RequestEvent(ChangeEventType.DELETE, request));
        }
    }

//    public void deleteARequest(Long id_from, Long id_to) {
//        Long requestId = findID(id_from, id_to); // Găsește ID-ul cererii de prietenie
//
//        // Verifică dacă cererea există
//        if (this.requestRepository.getOne(requestId)!=null) {
//            // Șterge cererea de prietenie
//            this.requestRepository.delete(requestId);
//        } else {
//            throw new IllegalArgumentException("Request between id_from " + id_from + " and id_to " + id_to + " does not exist.");
//        }
//    }

    public void deleteARequest(Long id_from, Long id_to) {
        Request request = this.requestRepository.getOne(findID(id_from, id_to));
        Request r = this.requestRepository.delete(findID(id_from, id_to));
        if (r == null) {
            notifyObservers(new RequestEvent(ChangeEventType.DELETE, request));
        }
    }


    public void confirmRequest(Long id_from, Long id_to) {
        Request request = this.requestRepository.getOne(findID(id_from, id_to));
        request.setStatus("approved");
        request.setId(findID(id_from, id_to));

        Friendship f = new Friendship(id_from, id_to, LocalDateTime.now());
        this.friendshipRepository.save(f);

        this.deleteRequest(findID(id_from, id_to));
    }

    public void updateRequest(Long id, String status) {
        Request u = this.requestRepository.getOne(id);
        u.setStatus(status);
        u.setId(id);

        if (Objects.equals(status, "approved")) {
            Friendship f = new Friendship(u.getFrom(), u.getTo(), LocalDateTime.now());
            this.friendshipRepository.save(f);
        }

        if (Objects.equals(status, "approved") || Objects.equals(status, "rejected")) {
            this.deleteRequest(id);
        } else if (!Objects.equals(status, "pending")) {
            throw new IllegalArgumentException("Wrong status!");
        }
    }

    public Iterable<Request> getAllRequests() {
        return this.requestRepository.getAll();
    }

    public Map<User, LocalDateTime> getAllRequestsForAnUser(Long id) {
        Collection<Request> allRequestsCollection = (Collection<Request>) this.requestRepository.getAll();
        Set<Request> allRequests = new HashSet<>(allRequestsCollection);

        return allRequests.stream()
                .filter(request -> Objects.equals(request.getTo(), id))
                .collect(Collectors.toMap(
                        request -> userRepository.getOne(request.getFrom()),
                        Request::getDateTime
                ));
    }



    public Request getOne(Long id) {
        return this.requestRepository.getOne(id);
    }

    public Long findID(Long id_from, Long id_to) {
        HashSet<Request> requests = (HashSet<Request>) this.requestRepository.getAll();
        return requests.stream().
                filter(x -> Objects.equals(x.getFrom(), id_from) && Objects.equals(x.getTo(), id_to))
                .toList().getFirst().getId();
    }

    public int existRequest(Long id1, Long id2) {//id1 a trimis cerere spre id2
        Map<User, LocalDateTime> idRequest = getAllRequestsForAnUser(id2);

        for (Map.Entry<User, LocalDateTime> request : idRequest.entrySet()) {
            if (request.getKey().getId().equals(id1))
                return 0;
        }
        return 1;
    }

    @Override
    public void addObserver(Observer<RequestEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<RequestEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(RequestEvent t) {
        observers.forEach(x -> x.update(t));
    }




}
