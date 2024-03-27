package com.example.mysocialapp.service;

//import org.example.domain.Message;
//import org.example.repo.Repository;
//import org.example.utils.events.ChangeEventType;
//import org.example.utils.events.MessageEvent;
//import org.example.utils.observer.Observable;
//import org.example.utils.observer.Observer;

import com.example.mysocialapp.domain.Message;
import com.example.mysocialapp.repo.Repository;
import com.example.mysocialapp.utils.events.ChangeEventType;
import com.example.mysocialapp.utils.events.MessageEvent;
import com.example.mysocialapp.utils.observer.Observable;
import com.example.mysocialapp.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class MessageService implements Observable<MessageEvent> {
    private final Repository<Long, Message> messageRepository;

    private final List<Observer<MessageEvent>> observers = new ArrayList<>();

    public MessageService(Repository<Long, Message> messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void addMessage(Long from, Long to, String messageText, LocalDateTime date, Long idReplay) {
        Message message = new Message(from, to, messageText, date, idReplay);
        this.messageRepository.save(message);
        notifyObservers(new MessageEvent(ChangeEventType.ADD, message));
    }

    public void replyAll(Long from, List<Long> ids_to, String messageText, LocalDateTime dateTime, List<Long> idsReply) {
        int i = 0;
        for (Long to : ids_to) {
            Message message = new Message(from, to, messageText, dateTime, idsReply.get(i));
            this.messageRepository.save(message);
            notifyObservers(new MessageEvent(ChangeEventType.ADD, message));
            i++;
        }
    }

    public void deleteMessage(Long id) {
        Message message = this.messageRepository.delete(id);
        notifyObservers(new MessageEvent(ChangeEventType.ADD, message));
    }

    public Iterable<Message> getAllMessages() {
        return this.messageRepository.getAll();
    }

    public Message getOne(Long id) {
        return this.messageRepository.getOne(id);
    }

    public void showConversation(Long first_id, Long second_id) {
        Iterable<Message> messagesSet = this.messageRepository.getAll();

        for (Message message : messagesSet) {
            try {
                if ((Objects.equals(message.getTo(), first_id) && Objects.equals(message.getFrom(), second_id))
                        || (Objects.equals(message.getTo(), second_id) && Objects.equals(message.getFrom(), first_id))) {
                    System.out.println(message);
                }
            } catch (NumberFormatException ex) {
                System.out.println("Error parsing message: " + ex.getMessage());
            }
        }
    }

    public List<Message> getConversation(Long first_id, Long second_id) {
        Iterable<Message> messagesSet = this.messageRepository.getAll();
        List<Message> conversation = new ArrayList<>();

        for (Message message : messagesSet) {
            try {
                if ((Objects.equals(message.getTo(), first_id) && Objects.equals(message.getFrom(), second_id))
                        || (Objects.equals(message.getTo(), second_id) && Objects.equals(message.getFrom(), first_id))) {
                    conversation.add(message);
                }
            } catch (NumberFormatException ex) {
                // Poți să faci ceva cu această excepție, dacă este necesar
            }
        }

        conversation.sort(Comparator.comparing(Message::getData));
        return conversation;
    }

    @Override
    public void addObserver(Observer<MessageEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<MessageEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(MessageEvent t) {
        observers.forEach(x -> x.update(t));
    }


}
