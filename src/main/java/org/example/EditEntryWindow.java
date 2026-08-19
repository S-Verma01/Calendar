package org.example;

import com.calendarfx.model.Entry;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EditEntryWindow extends EntryWindowBase {

    private boolean isOpen;

    private FlowPane closePane;
    private FlowPane confirmPane;

    private Button closeButton;
    private Button confirmButton;

    private Entry<?> currentEntry;

    //initialization methods
    public EditEntryWindow() {
        super();
        initializeElements();
        buildLayout();
        configureLayout();
        setEventHandlers();
    }
    private void initializeElements() {
        isOpen = false;
        closePane = new FlowPane();
        confirmPane = new FlowPane();
        closeButton = new Button("x");
        confirmButton = new Button("confirm");
    }
    private void setEventHandlers() {
        //Save changes made to entry by editEntryWindow (calendarView & local database)
        confirmButton.setOnAction(e -> {
           updateEntry();
        });
    }
    private void buildLayout() {
        this.getChildren().addFirst(closePane);
        this.getChildren().addLast(confirmPane);
        closePane.getChildren().add(closeButton);
        confirmPane.getChildren().add(confirmButton);
    }
    private void configureLayout() {
        closePane.setAlignment(Pos.TOP_RIGHT);
        confirmPane.setAlignment(Pos.TOP_RIGHT);
    }

    //Getter & setter methods
    public Button getCloseButton() {
        return closeButton;
    }
    public Button getConfirmButton() {
        return confirmButton;
    }
    public boolean getIsOpen() {
        return isOpen;
    }
    public void setIsOpen(boolean value) {
        isOpen = value;
    }
    public void displayEntry(Appointment appointment, Entry<?> entry) {

        currentEntry = entry;

        String title = appointment.getTitle();
        LocalDateTime startDateTime = appointment.getTimeStart();
        LocalDateTime endDateTime = appointment.getTimeEnd();
        String description = appointment.getDescription();

        DateTimeFormatter format1 = DateTimeFormatter.ofPattern("HH:mm-dd-MM-yyyy");

        String[] startDateTimeComponents = startDateTime.format(format1).toString().split("-");
        String[] endDateTimeComponents = endDateTime.format(format1).toString().split("-");

        String startTime = startDateTimeComponents[0];
        String endTime = endDateTimeComponents[0];

        this.getTitle().setText(title);

        Platform.runLater(() -> {
            String tempMinute = startTime.split(":")[1];
            if(tempMinute.equals("00") || tempMinute.equals("30")) {
                this.getTimeBox1().getSelectionModel().select(startTime);
                this.getTimeBox1().getEditor().setText(startTime);
            } else {
                this.getTimeBox1().getEditor().setText(startTime);
            }

            tempMinute = endTime.split(":")[1];
            if(tempMinute.equals("00") || tempMinute.equals("30")) {
                this.getTimeBox2().getSelectionModel().select(endTime);
                this.getTimeBox2().getEditor().setText(endTime);
            } else {
                this.getTimeBox2().getEditor().setText(endTime);
            }
        });

        String startDate = startDateTimeComponents[1] + "." + startDateTimeComponents[2] + "." + startDateTimeComponents[3];
        this.getDateField1().setText(startDate);
        this.setTempDate1(startDate);

        String endDate = endDateTimeComponents[1] + "." + endDateTimeComponents[2] + "." + endDateTimeComponents[3];
        this.getDateField2().setText(endDate);
        this.setTempDate2(endDate);

        this.getDescriptionArea().setText(description);
    }
    public void updateEntry() {
        Appointment appointment = Main.getEntryToAppointment().get(currentEntry.getId());

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

        currentEntry.setInterval(start, end);
        currentEntry.setTitle(titleText);

        appointment.setTitle(titleText);
        appointment.setTimeStart(start);
        appointment.setTimeEnd(end);
        appointment.setDescription(description);
        appointment.update();

    }
}
