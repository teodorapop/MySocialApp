package com.example.mysocialapp.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

public class Friendship extends Entity<Long> {
    private final Long id1;
    private final Long id2;
    private final LocalDateTime date;

    public Friendship(Long id1, Long id2, LocalDateTime date) {
        this.id1 = Objects.requireNonNull(id1);
        this.id2 = Objects.requireNonNull(id2);
        this.date = date;
        setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
    }

    public Long getId1() {
        return id1;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = date.format(formatter);
        return "Friendship{" +
                "id1=" + id1 +
                ", id2=" + id2 +
                ", date=" +formattedDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(id1, that.id1) && Objects.equals(id2, that.id2) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id1, id2, date);
    }

    public Long getId2() {
        return id2;
    }
}

