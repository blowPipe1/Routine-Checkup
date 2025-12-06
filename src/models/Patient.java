package models;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class Patient {
    private final String id;
    private final LocalTime time;
    private final String fullName;
    private final LocalDate dateOfBirth;
    private final String type;
    private final String symptoms;


    public Patient(LocalTime time, String fullName, LocalDate dateOfBirth, String type, String symptoms) {
        this.id = UUID.randomUUID().toString();
        this.time = time;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.type = type;
        this.symptoms = symptoms;
    }

    public String getId() {
        return id;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getFullName() {
        return fullName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String  getType() {
        return type;
    }

    public String getSymptoms() {
        return symptoms;
    }
}
