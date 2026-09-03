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

    private String timeStringStart;
    private String timeStringEnd;

    //Constructors
    public Appointment(int id, String title, String timeStartString, String timeEndString, String description)  {
        this. id = id;
        this.title = title;
        this.timeStart = LocalDateTime.parse(timeStartString);
        this.timeEnd = LocalDateTime.parse(timeEndString);
        this.description = description;

        String tempTimeStart = timeStart.toString();
        String tempTimeEnd = timeEnd.toString();

        this.timeStringStart = tempTimeStart.substring(0, 4) + tempTimeStart.substring(5, 7) + tempTimeStart.substring(8, 10) + tempTimeStart.substring(11, 13) + tempTimeStart.substring(14, 16);
        this.timeStringEnd = tempTimeEnd.substring(0, 4) + tempTimeEnd.substring(5, 7) + tempTimeEnd.substring(8, 10) + tempTimeEnd.substring(11, 13) + tempTimeEnd.substring(14, 16);
    }
    public Appointment(String title, LocalDateTime timeStartDateTime, LocalDateTime timeEndDateTime, String description) {
        this.title = title;
        this.timeStart = timeStartDateTime;
        this.timeEnd = timeEndDateTime;
        this.description = description;

        String tempTimeStart = timeStart.toString();
        String tempTimeEnd = timeEnd.toString();

        this.timeStringStart = tempTimeStart.substring(0, 4) + tempTimeStart.substring(5, 7) + tempTimeStart.substring(8, 10) + tempTimeStart.substring(11, 13) + tempTimeStart.substring(14, 16);
        this.timeStringEnd = tempTimeEnd.substring(0, 4) + tempTimeEnd.substring(5, 7) + tempTimeEnd.substring(8, 10) + tempTimeEnd.substring(11, 13) + tempTimeEnd.substring(14, 16);
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
    public String getTimeStringStart() {
        return timeStringStart;
    }
    public String getTimeStringEnd() {
        return timeStringEnd;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setTimeEnd(LocalDateTime timeEnd) {
        this.timeEnd = timeEnd;

        String tempTimeEnd = timeEnd.toString();
        this.timeStringEnd = tempTimeEnd.substring(0, 4) + tempTimeEnd.substring(5, 7) + tempTimeEnd.substring(8, 10) + tempTimeEnd.substring(11, 13) + tempTimeEnd.substring(14, 16);
    }
    public void setTimeStart(LocalDateTime timeStart) {
        this.timeStart = timeStart;

        String tempTimeStart = timeStart.toString();
        this.timeStringStart = tempTimeStart.substring(0, 4) + tempTimeStart.substring(5, 7) + tempTimeStart.substring(8, 10) + tempTimeStart.substring(11, 13) + tempTimeStart.substring(14, 16);
    }
    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }
    public void setTimeStringStart(String timeStringStart) {
        this.timeStringStart = timeStringStart;
    }
    public void setTimeStringEnd(String timeStringEnd) {
        this.timeStringEnd = timeStringEnd;
    }
}
