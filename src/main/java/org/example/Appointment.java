package org.example;

import java.time.LocalDateTime;

//Class for saving and processing entries (features where this class is used are planned)

public class Appointment {

    private int id;
    private LocalDateTime timeStart;
    private LocalDateTime timeEnd;
    private String calendarId;

    private String title;
    private String description;

    //Constructors
    public Appointment(int id, String title, String timeStartString, String timeEndString, String description)  {
        this. id = id;
        this.title = title;
        this.timeStart = LocalDateTime.parse(timeStartString);
        this.timeEnd = LocalDateTime.parse(timeEndString);
        this.description = description;
    }
    public Appointment(String title, LocalDateTime timeStartDateTime, LocalDateTime timeEndDateTime, String description) {
        this.title = title;
        this.timeStart = timeStartDateTime;
        this.timeEnd = timeEndDateTime;
        this.description = description;
    }

    //Methods for adding changes to local database
    public void addToLocalDatabase() {
        this.id = LocalDatabase.add(this);
    }
    public void deleteFromLocalDatabase() {
        LocalDatabase.delete(this);
    }
    public void update() {
        LocalDatabase.update(this);
    }

    //Getter & setter methods
    public String getDescription() {
        return description;
    }
    public String getTitle() {
        return title;
    }
    public int getId() {
        return id;
    }
    public LocalDateTime getTimeStart() {
        return timeStart;
    }
    public LocalDateTime getTimeEnd() {
        return timeEnd;
    }
    public String getCalendarId() {
        return calendarId;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setTimeEnd(LocalDateTime timeEnd) {
        this.timeEnd = timeEnd;
    }
    public void setTimeStart(LocalDateTime timeStart) {
        this.timeStart = timeStart;
    }
    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }
}
