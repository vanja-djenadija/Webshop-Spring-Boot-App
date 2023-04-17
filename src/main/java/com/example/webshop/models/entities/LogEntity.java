package com.example.webshop.models.entities;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "log", schema = "webshop")
public class LogEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "message", nullable = false, length = 1000)
    private String message;
    @Basic
    @Column(name = "level", nullable = false, length = 50)
    private String level;
    @Basic
    @Column(name = "date_time", nullable = false)
    private Timestamp dateTime;
    @Basic
    @Column(name = "logger", nullable = false, length = 500)
    private String logger;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public String getLogger() {
        return logger;
    }

    public void setLogger(String logger) {
        this.logger = logger;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogEntity logEntity = (LogEntity) o;
        return Objects.equals(id, logEntity.id) && Objects.equals(message, logEntity.message) && Objects.equals(level, logEntity.level) && Objects.equals(dateTime, logEntity.dateTime) && Objects.equals(logger, logEntity.logger);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, message, level, dateTime, logger);
    }
}