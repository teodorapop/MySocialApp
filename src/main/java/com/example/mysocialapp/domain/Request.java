package com.example.mysocialapp.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Request extends Entity<Long> {
    private Long from;
    private Long to;
    private String status;
    private LocalDateTime dateTime;

    public Request(Long from, Long to, String status, LocalDateTime dateTime) {
        this.from = from;
        this.to = to;
        this.status = status;
        this.dateTime = dateTime;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Request{" +
                "from=" + from +
                ", to=" + to +
                ", status='" + status + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return from.equals(request.from) && to.equals(request.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getFrom() {
        return from;
    }

    public Long getTo() {
        return to;
    }

    public String getStatus() {
        return status;
    }
}
