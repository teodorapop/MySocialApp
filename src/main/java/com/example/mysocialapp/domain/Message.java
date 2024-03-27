package com.example.mysocialapp.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Message extends Entity<Long> {

    private Long from;
    private Long to;
    private String text;
    private LocalDateTime data;
    private Long idReplay;

    public Message(Long from, Long to, String text, LocalDateTime data, Long idReplay) {
        this.from = from;
        this.to = to;
        this.text = text;
        this.data = data;
        this.idReplay = idReplay;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public String getMessage() {
        return text;
    }

    public void setMessage(String text) {
        this.text = text;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    @Override
    public String toString() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = data.format(formatter);

        return "Message{" +
                "from= " + from +
                ", to= " + to +
                ", message= " + text +
                ", date=" + formattedDate+
                ", idReplay= " + idReplay +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message that = (Message) o;
        return Objects.equals(from, that.from) && Objects.equals(to, that.to) && Objects.equals(text, that.text) && Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, text, data);
    }

    public Long getIdReplay() {
        return idReplay;
    }

    public void setIdReplay(Long idReplay) {
        this.idReplay = idReplay;
    }
}

