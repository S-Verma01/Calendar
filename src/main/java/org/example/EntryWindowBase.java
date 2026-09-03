package org.example;

import com.calendarfx.view.YearMonthView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.SetChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

//Parent class for createEntryWindow & editEntryWindow

public class EntryWindowBase extends VBox {

    private boolean yearMonthCalendar1IsOpen;
    private boolean yearMonthCalendar2IsOpen;

    private String tempDate1;
    private String tempDate2;
    private String tempTime1;
    private String tempTime2;

    private YearMonthView yearMonthCalendar1;
    private YearMonthView yearMonthCalendar2;

    private GridPane entryGrid;
    protected VBox form;
    private HBox datePane1;
    private HBox datePane2;
    private FlowPane yearMonthViewPane1;
    private FlowPane yearMonthViewPane2;
    private FlowPane timeOfDayBoxes1;
    private FlowPane timeOfDayBoxes2;

    private TextField title;
    private TextField dateField1;
    private TextField dateField2;
    private TextArea descriptionArea;

    private ComboBox<String> timeBox1;
    private ComboBox<String> timeBox2;

    private Button calendarIcon1;
    private Button calendarIcon2;

    public EntryWindowBase() {
        initializeElements();
        buildLayout();
        setStyleSettings();
        configureLayout();
        setEventHandlers();
        setStyle();
    }

    //initialization methods
    private void initializeElements() {
        yearMonthCalendar1IsOpen = false;
        yearMonthCalendar2IsOpen = false;

        yearMonthCalendar1 = new YearMonthView();
        yearMonthCalendar1.setShowTodayButton(false);
        yearMonthCalendar2 = new YearMonthView();
        yearMonthCalendar2.setShowTodayButton(false);

        entryGrid = new GridPane();
        form = new VBox();
        datePane1 = new HBox();
        datePane2 = new HBox();
        yearMonthViewPane1 = new FlowPane();
        yearMonthViewPane2 = new FlowPane();
        timeOfDayBoxes1 = new FlowPane();
        timeOfDayBoxes2 = new FlowPane();

        title = new TextField();
        dateField1 = new TextField();
        tempDate1 = dateField1.getText();
        dateField2 = new TextField();
        tempDate2 = dateField2.getText();
        descriptionArea = new TextArea();

        timeBox1 = new ComboBox<>();
        timeBox2 = new ComboBox<>();

        calendarIcon1 = new Button("*");
        calendarIcon2 = new Button("*");

        initializeTimeBoxes();
    }
    private void buildLayout() {
        //Adding layout & control elements together
        this.getChildren().add(form);
        form.getChildren().addAll(title, entryGrid, descriptionArea);

        entryGrid.add(timeOfDayBoxes1, 0, 0);
        entryGrid.add(datePane1, 0, 1);
        entryGrid.add(timeOfDayBoxes2, 1, 0);
        entryGrid.add(datePane2, 1, 1);

        datePane1.getChildren().addAll(dateField1, calendarIcon1);
        datePane2.getChildren().addAll(dateField2, calendarIcon2);
        timeOfDayBoxes1.getChildren().add(timeBox1);
        timeOfDayBoxes2.getChildren().add(timeBox2);

        yearMonthViewPane1.getChildren().add(yearMonthCalendar1);
        yearMonthViewPane2.getChildren().add(yearMonthCalendar2);
    }
    private void initializeTimeBoxes() {
        ObservableList<String> time1 = FXCollections.observableList(createTimeOfDayChoices1());
        ObservableList<String> time2 = FXCollections.observableList(createTimeOfDayChoices1());
        timeBox1 = new ComboBox<>(time1);
        timeBox2 = new ComboBox<>(time2);
        timeBox1.setEditable(true);
        timeBox2.setEditable(true);

    }
    private List<String> createTimeOfDayChoices1() {
        List<String> res = new ArrayList<>();
        String hour = "00";
        String minute = "00";
        String time;
        do {
            time = hour + ":" + minute;
            res.add(time);

            Integer intHour = Integer.parseInt(hour);
            Integer intMinute = Integer.parseInt(minute);

            if(intMinute == 0) {
                minute = "30";
            } else if(intMinute == 30) {
                minute = "00";
                intHour++;
                if(intHour < 10) {
                    hour = "0" + intHour;
                } else {
                    hour = intHour + "";
                }
            }
        } while(!time.equals("23:30"));
        return res;
    }
    private void setStyleSettings() {
        this.setStyle("-fx-border-color: #5a6572; -fx-border-width: 2px; -fx-background-color: #5a6572; -fx-border-radius: 5px; -fx-background-radius: 5px;");
    }
    private void configureLayout() {
        this.setSpacing(5);
        this.setMaxWidth(400);
        this.setPadding(new Insets(10));
        entryGrid.setVgap(5);
        entryGrid.setPadding(new Insets(3));
        dateField1.setMaxWidth(125);
        dateField2.setMaxWidth(125);
        timeBox1.setMaxWidth(135);
        timeBox1.setMinWidth(135);
        timeBox1.setPrefWidth(135);
        timeBox2.setMaxWidth(135);
        timeBox2.setMinWidth(135);
        timeBox2.setPrefWidth(135);
        title.setPrefWidth(350);
        title.setMaxWidth(350);
        title.setMinWidth(350);
        yearMonthViewPane1.setAlignment(Pos.CENTER);
        yearMonthViewPane2.setAlignment(Pos.CENTER);
        descriptionArea.setMinWidth(200);
        descriptionArea.setMaxWidth(400);
        descriptionArea.setPrefWidth(400);
        descriptionArea.setPrefHeight(125);
        descriptionArea.setMinHeight(125);
        descriptionArea.setMaxHeight(125);
    }
    private void setEventHandlers() {
        //Process date input for dateField1
        dateField1.setOnAction(e -> {
            String input = dateField1.getText();
            try {
                //Check if date format is correct and set format to "MM.dd.yyyy" (standardize)
                LocalDate processedInput = getDate1(input);
                dateField1.setText(processedInput.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                tempDate1 = dateField1.getText();
            } catch(IllegalArgumentException i) {
                dateField1.setText(tempDate1);
            }
        });

        //Process date input for dateField2
        dateField2.setOnAction(e -> {
            String input = dateField2.getText();
            try {
                //Check if date format is correct and set format to "MM.dd.yyyy" (standardize) if need be
                LocalDate processedInput = getDate1(input);
                dateField2.setText(processedInput.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                tempDate2 = dateField2.getText();
            } catch(IllegalArgumentException i) {
                dateField2.setText(tempDate2);
            }
        });

        //Process time input for timeBox1
        timeBox1.setOnAction(e -> {
            String input = timeBox1.getEditor().getText();

            try {
                String time = getTimeInFormat1(input);

                Platform.runLater(() -> {
                    timeBox1.getEditor().setText(time);
                });

                tempTime1 = time;

            } catch (IllegalArgumentException ex) {
                Platform.runLater(() -> {
                    timeBox1.getEditor().setText(tempTime1);
                });
            }
        });

        //Process time input for timeBox2
        timeBox2.setOnAction(e -> {
            String input = timeBox2.getEditor().getText();

            try {
                String time = getTimeInFormat1(input);

                Platform.runLater(() -> {
                    timeBox2.getEditor().setText(time);
                });

                tempTime2 = time;

            } catch (IllegalArgumentException ex) {
                Platform.runLater(() -> {
                    timeBox2.getEditor().setText(tempTime2);
                });
            }
        });

        //Open mini calendar (yearMonthView) for easier date selection, starting date
        calendarIcon1.setOnAction(e -> {
            if(!yearMonthCalendar1IsOpen) {
                if(yearMonthCalendar2IsOpen) {
                    this.form.getChildren().remove(yearMonthViewPane2);
                    yearMonthCalendar2IsOpen = false;
                }
                this.form.getChildren().add(2, yearMonthViewPane1);
                yearMonthCalendar1IsOpen = true;
                this.form.getChildren().remove(descriptionArea);
            } else {
                this.form.getChildren().remove(yearMonthViewPane1);
                yearMonthCalendar1IsOpen = false;
                this.form.getChildren().add(2, descriptionArea);
            }
        });

        //Open mini calendar (yearMonthView) for easier date selection, ending date
        calendarIcon2.setOnAction(e -> {
            if(!yearMonthCalendar2IsOpen) {
                if(yearMonthCalendar1IsOpen) {
                    this.form.getChildren().remove(yearMonthViewPane1);
                    yearMonthCalendar1IsOpen = false;
                }
                this.form.getChildren().add(2, yearMonthViewPane2);
                yearMonthCalendar2IsOpen = true;
                this.form.getChildren().remove(descriptionArea);
            } else {
                this.form.getChildren().remove(yearMonthViewPane2);
                yearMonthCalendar2IsOpen = false;
                this.form.getChildren().add(2, descriptionArea);
            }
        });

        //extract input from yearMonthView for dateField1
        yearMonthCalendar1.getSelectedDates().addListener(
                (SetChangeListener<LocalDate>) change -> {

                    if (change.wasAdded()) {
                        LocalDate selectedDate = change.getElementAdded();
                        dateField1.setText(selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                        tempDate1 = dateField1.getText();
                    }
                }
        );

        //extract input from yearMonthView for dateField2
        yearMonthCalendar2.getSelectedDates().addListener(
                (SetChangeListener<LocalDate>) change -> {

                    if (change.wasAdded()) {
                        LocalDate selectedDate = change.getElementAdded();
                        dateField2.setText(selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                        tempDate2 = dateField2.getText();
                    }
                }
        );
    }
    private LocalDate getDate1(String input) throws IllegalArgumentException {

        DateTimeFormatter format1 = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter format2 = DateTimeFormatter.ofPattern("d.M.yyyy");
        DateTimeFormatter format3 = DateTimeFormatter.ofPattern("d.MM.yyyy");
        DateTimeFormatter format4 = DateTimeFormatter.ofPattern("dd.M.yyyy");

        try {
            LocalDate date1 = LocalDate.parse(input, format1);
            return date1;
        } catch (DateTimeParseException f1) {
            try {
                LocalDate date2 = LocalDate.parse(input, format2);
                return date2;
            } catch(DateTimeParseException f2) {
                try {
                    LocalDate date3 = LocalDate.parse(input, format3);
                    return date3;
                } catch(DateTimeParseException f3) {
                    try {
                        LocalDate date4 = LocalDate.parse(input, format4);
                        return date4;
                    } catch(DateTimeParseException f4) {
                        throw new IllegalArgumentException();
                    }
                }
            }
        }
        // Note regarding invalid day input: A DateTimeParseException does not occur when the day exceeds the number
        // of days in the selected month. Instead, the highest valid day of that month is selected.
        // A DateTimeParseException only occurs when the day is set to 32 or higher.
    }
    private String getTimeInFormat1(String input) throws IllegalArgumentException {
        //Check if time input is valid and set format to "hh:mm"
        String[] s = input.split(":");

        if(s.length != 2) {
            throw new IllegalArgumentException("Illegal time format");
        }

        int hour = 0;
        int minute = 0;

        try {
            hour = Integer.parseInt(s[0]);
            minute = Integer.parseInt(s[1]);
        } catch(NumberFormatException n) {
            throw new IllegalArgumentException("Illegal time format");
        }

        if(!(hour >= 0 && hour <= 23)) {
            throw new IllegalArgumentException("Illegal time format");
        }

        if(!(minute >= 0 && minute <= 59)) {
            throw new IllegalArgumentException("Illegal time format");
        }

        String hourString;
        String minuteString;

        if(hour < 10) {
            hourString = "0" + hour;
        } else {
            hourString = hour + "";
        }

        if(minute < 10) {
            minuteString = "0" + minute;
        } else {
            minuteString = minute + "";
        }

        return hourString + ":" + minuteString;
    }
    private void setStyle() {

        yearMonthCalendar1.getStylesheets().add(
                getClass().getResource("/YearMonthView.css").toExternalForm()
        );

        yearMonthCalendar2.getStylesheets().add(
                getClass().getResource("/YearMonthView.css").toExternalForm()
        );
    }

    //Getter & setter methods
    public TextField getTitle() {
        return title;
    }
    public ComboBox<String> getTimeBox1() {
        return timeBox1;
    }
    public ComboBox<String> getTimeBox2() {
        return timeBox2;
    }
    public TextField getDateField1() {
        return dateField1;
    }
    public TextField getDateField2() {
        return dateField2;
    }
    public TextArea getDescriptionArea() {
        return descriptionArea;
    }
    public String getTempTime1() {
        return tempTime1;
    }
    public String getTempTime2() {
        return tempTime2;
    }
    public void setTempTime1(String tempTime1) {
        this.tempTime1 = tempTime1;
    }
    public void setTempTime2(String tempTime2) {
        this.tempTime2 = tempTime2;
    }
    public String getTempDate1() {
        return tempDate1;
    }
    public String getTempDate2() {
        return tempDate2;
    }
    public void setTempDate1(String tempDate1) {
        this.tempDate1 = tempDate1;
    }
    public void setTempDate2(String tempDate2) {
        this.tempDate2 = tempDate2;
    }
}
