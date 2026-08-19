package org.example;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class CreateEntryWindow extends EntryWindowBase {

    private boolean isOpen;

    private FlowPane closePane;
    private FlowPane createPane;

    private Button closeButton;
    private Button createButton;

    public CreateEntryWindow(Calendar<?> general, Map<String, Appointment> entryToAppointment) {
        super();
        initializeElements();
        buildLayout();
        configureLayout();
        initializeTimeDateFields();
        setEventHandlers();
    }

    //Initialization methods
    private void initializeElements() {
        isOpen = false;
        closePane = new FlowPane();
        createPane = new FlowPane();
        closeButton = new Button("x");
        createButton = new Button("create");

        //Set dateFields to current LocalDateTime
        this.getDateField1().setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        this.getDateField2().setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }
    private void setEventHandlers() {
        //create entry (for calendarView & local database)
        createButton.setOnAction(e-> {
            try {
                setEvent();
            } catch (NullPointerException n) {
                System.out.println(n.getMessage());
            }
        });
    }
    private void setEvent() {
        //Extract all the input from TextFields and TextAreas
        String titleText = this.getTitle().getText();
        String timeOfDay1 = this.getTimeBox1().getEditor().getText();
        String timeOfDay2 = this.getTimeBox2().getEditor().getText();
        String dateTime1 = this.getDateField1().getText();
        String dateTime2 = this.getDateField2().getText();
        String[] timeOfDayArray1 = timeOfDay1.split(":");
        String[] timeOfDayArray2 = timeOfDay2.split(":");
        String[] dateTimeArray1 = dateTime1.split("\\.");
        String[] dateTimeArray2 = dateTime2.split("\\.");
        String description = this.getDescriptionArea().getText();

        int startYear = Integer.parseInt(dateTimeArray1[2]);
        int startMonth = Integer.parseInt(dateTimeArray1[1]);
        int startDay = Integer.parseInt(dateTimeArray1[0]);
        int startHour = Integer.parseInt(timeOfDayArray1[0]);
        int startMinute = Integer.parseInt(timeOfDayArray1[1]);

        int endYear = Integer.parseInt(dateTimeArray2[2]);
        int endMonth = Integer.parseInt(dateTimeArray2[1]);
        int endDay = Integer.parseInt(dateTimeArray2[0]);
        int endHour = Integer.parseInt(timeOfDayArray2[0]);
        int endMinute = Integer.parseInt(timeOfDayArray2[1]);

        LocalDateTime start = LocalDateTime.of(startYear, startMonth, startDay, startHour, startMinute);
        LocalDateTime end = LocalDateTime.of(endYear, endMonth, endDay, endHour, endMinute);

        //create entry for calendarView
        Entry<String> entry = new Entry<>(titleText);
        entry.setInterval(start, end);

        //create entry for the local database
        Appointment appointment = new Appointment(titleText, start, end, description);
        appointment.addToLocalDatabase();
        appointment.setCalendarId(entry.getId());

        Main.getEntryToAppointment().put(entry.getId(),appointment);
        Main.getGeneral().addEntry(entry);

    }
    private void buildLayout() {
        this.getChildren().addFirst(closePane);
        this.getChildren().addLast(createPane);
        closePane.getChildren().add(closeButton);
        createPane.getChildren().add(createButton);
    }
    private void configureLayout() {
        closePane.setAlignment(Pos.TOP_RIGHT);
        createPane.setAlignment(Pos.TOP_RIGHT);
    }
    private void initializeTimeDateFields() {
        //Outlook calendar inspired time and date field initialization (1 hour time gap instead of 30 min)
        LocalTime time = LocalTime.now();
        String[] timeArray = time.toString().split(":");
        int currentHour =  Integer.parseInt(timeArray[0]);
        int currentMinute = Integer.parseInt(timeArray[1]);
        String inputMinute;
        String inputHour;
        boolean nextDay = false;

        if(currentMinute < 30) {
            inputMinute = "30";
        } else {
            inputMinute = "00";
            currentHour++;
            if(currentHour == 24) {
                currentHour = 0;
                nextDay = true;
            }
        }

        if(currentHour < 10) {
            inputHour = "0" + currentHour;
        } else {
            inputHour = currentHour + "";
        }

        String input1 = inputHour + ":" + inputMinute;

        this.getTimeBox1().getSelectionModel().select(input1);
        this.setTempTime1(input1);

        if(input1.equals("00:00")) {
            this.getDateField1().setText(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        }

        currentHour++;
        if(currentHour == 24) {
            currentHour = 0;
            this.getDateField2().setText(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        }

        if(currentHour == 1 && nextDay) {
            this.getDateField2().setText(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        }

        inputHour = currentHour + "";
        String input2 = inputHour + ":" + inputMinute;

        this.getTimeBox2().getSelectionModel().select(input2);
        this.setTempTime2(input2);
    }

    //Getter & setter methods
    public Button getCloseButton() {
        return closeButton;
    }
    public Button getCreateButton() {
        return createButton;
    }
    public boolean getIsOpen() {
        return isOpen;
    }
    public void setIsOpen(boolean value) {
        isOpen = value;
    }

}
