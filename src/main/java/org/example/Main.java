package org.example;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.Styles;
import com.calendarfx.model.*;
import com.calendarfx.model.Calendar;
import com.calendarfx.view.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.SetChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class Main extends Application {

    private CalendarView calendar;
    private boolean sidebarIsOpen;
    private boolean createWindowIsOpen;
    private boolean yearMonthCalendar1IsOpen;
    private boolean yearMonthCalendar2IsOpen;
    private TextField title;
    private Calendar general;
    private Group calendarGroup;
    private Region space;
    private Map<String, Appointment> entryToAppointment;
    private YearMonthView yearMonthCalendar1;
    private YearMonthView yearMonthCalendar2;
    private TextField dateField1;
    private TextField dateField2;
    private TextArea descriptionArea;
    private ComboBox<String> timeBox1;
    private ComboBox<String> timeBox2;

    //Panes
    private HBox mainPane;
    private VBox home;
    private HBox top;
    private BorderPane promptBar;
    private VBox sidebar;
    private VBox dashboard;
    private VBox entryWindow;
    private VBox closeEntryWindowPane;
    private FlowPane timeOfDayBoxes1;
    private GridPane entryGrid;
    private FlowPane timeOfDayBoxes2;
    private HBox center;
    private HBox datePane1;
    private HBox datePane2;
    private FlowPane yearMonthViewPane1;
    private FlowPane yearMonthViewPane2;
    private HBox createEntryPane;

    //Labels
    private Label sidebarIcon;
    private Label settingsIcon;
    private Label designIcon;
    private Label currentDate;

    //Buttons
    private Button closeButton;
    private Button createEntry;
    private Button create;
    private Button calendarIcon1;
    private Button calendarIcon2;

    //Menu elements
    private MenuItem edit;
    private MenuItem delete;

    //Temporary variables
    private Entry<?> tempEntry;
    private String tempDate1;
    private String tempDate2;
    private String tempTime1;
    private String tempTime2;

    //Methods
    public void updateTodayTimeLabels() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        currentDate.setText("Today, " + today.format(format));
    }
    public void resizeCalendarWindow() {
        //Used for keeping a consistent window size of calendarView
        calendar.setMinWidth(1025);
        calendar.setMaxWidth(1025);
        calendar.setMinHeight(900);
        calendar.setMaxHeight(900);
    }
    public void initializeGlobalVariables() {
        //Global Variables
        calendar = new CalendarView();
        calendarGroup = new Group(calendar);
        sidebarIsOpen = false;
        createWindowIsOpen = false;
        title = new TextField();
        general = new Calendar("general");
        space = new Region();
        edit = new MenuItem("Edit");
        delete = new MenuItem("Delete");
        entryToAppointment = new HashMap<>();
        yearMonthCalendar1 = new YearMonthView();
        yearMonthCalendar1.setShowTodayButton(false);
        yearMonthCalendar2 = new YearMonthView();
        yearMonthCalendar2.setShowTodayButton(false);
        dateField1 = new TextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        tempDate1 = dateField1.getText();
        dateField2 = new TextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        tempDate2 = dateField2.getText();
        yearMonthCalendar1IsOpen = false;
        yearMonthCalendar2IsOpen = false;
        descriptionArea = new TextArea();
        initializeTimeBoxes();
    }
    public List<String> createTimeOfDayChoices () {
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
    public void createPanes() {
        //Panes
        mainPane = new HBox();
        home = new VBox();
        top = new HBox();
        promptBar = new BorderPane();
        sidebar = new VBox();
        dashboard = new VBox();
        entryWindow = new VBox();
        closeButton = new Button("x");
        closeEntryWindowPane = new VBox();
        timeOfDayBoxes1 = new FlowPane();
        entryGrid = new GridPane();
        timeOfDayBoxes2 = new FlowPane();
        center = new HBox();
        datePane1 = new HBox();
        datePane2 = new HBox();
        yearMonthViewPane1 = new FlowPane();
        yearMonthViewPane2 = new FlowPane();
        createEntryPane = new HBox();
    }
    public void createLabels() {
        //Labels
        sidebarIcon = new Label("= ");
        settingsIcon = new Label("Settings");
        designIcon = new Label("Design");
        currentDate = new Label("Today, ");
    }
    public void createButtons() {
        //Buttons
        createEntry = new Button("Create");
        create = new Button("+");
        calendarIcon1 = new Button("*");
        calendarIcon2 = new Button("*");
    }
    public void setStyleSettings() {
        //Style Settings (colors, font etc...)
        mainPane.setStyle("-fx-background-color: #101010");
        top.setStyle("-fx-border-color: #30363d; -fx-border-width: 2px; -fx-background-color: #30363d; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        sidebarIcon.getStyleClass().add(Styles.TITLE_1);
        sidebarIcon.setStyle("-fx-text-fill: -color-fg-default;");
        entryWindow.setStyle("-fx-border-color: #5a6572; -fx-border-width: 2px; -fx-background-color: #5a6572; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        currentDate.getStyleClass().add(Styles.TITLE_4);
        currentDate.setStyle("-fx-text-fill: -color-fg-default;");
        currentDate.setFont(new Font(30));
        currentDate.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");
        dashboard.setStyle("-fx-border-color: #30363d; -fx-border-width: 2px; -fx-background-color: #30363d; -fx-border-radius: 5px; -fx-background-radius: 5px;");
    }
    public void configureLayout() {
        //Layout Settings
        home.setPadding(new Insets(25));
        home.setSpacing(15);
        promptBar.setCenter(new TextField());
        promptBar.setPadding(new Insets(20));
        entryWindow.setPadding(new Insets(10));
        entryWindow.setSpacing(5);
        entryWindow.setMaxWidth(400);
        closeEntryWindowPane.setAlignment(Pos.TOP_RIGHT);
        entryGrid.setVgap(5);
        entryGrid.setPadding(new Insets(3));
        dashboard.setPadding(new Insets(10));
        dashboard.setMinWidth(400);
        dashboard.setMaxWidth(400);
        dashboard.setMinHeight(715);
        dashboard.setMaxHeight(715);
        dashboard.setVgrow(space, Priority.ALWAYS);
        dashboard.setSpacing(10);
        dashboard.setAlignment(Pos.CENTER);
        center.setSpacing(15);
        currentDate.setMaxWidth(Double.MAX_VALUE);
        currentDate.setAlignment(Pos.CENTER);
        sidebar.setMinWidth(0);
        sidebar.setMaxWidth(0);
        calendar.setScaleX(0.8);
        calendar.setScaleY(0.8);
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
        createEntryPane.setAlignment(Pos.TOP_RIGHT);
    }
    public void buildLayout() {
        //Adding layout elements together
        mainPane.getChildren().addAll(sidebar, home);
        home.getChildren().addAll(top, center);
        top.getChildren().addAll(sidebarIcon, create);
        center.getChildren().addAll(calendarGroup, dashboard);
        dashboard.getChildren().addAll(currentDate, space, promptBar);

        entryWindow.getChildren().addAll(closeEntryWindowPane, title, entryGrid, descriptionArea, createEntryPane);
        closeEntryWindowPane.getChildren().add(closeButton);
        createEntryPane.getChildren().addAll(createEntry);
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
    public void setCalendarView() {
        //Setting up calendarView
        calendar.showMonthPage();
        resizeCalendarWindow();

        CalendarSource myCalendarSource = new CalendarSource("My Calendars");
        myCalendarSource.getCalendars().add(general);

        calendar.getCalendarSources().addAll(myCalendarSource);

        //Resize calendarView window accordingly after page selection
        calendar.selectedPageProperty().addListener((obs, oldPage, newPage) -> {
            if (newPage == CalendarView.Page.DAY) {
                resizeCalendarWindow();
            } else if (newPage == CalendarView.Page.WEEK) {
                resizeCalendarWindow();
            } else if (newPage == CalendarView.Page.MONTH) {
                resizeCalendarWindow();
            } else if (newPage == CalendarView.Page.YEAR) {
                resizeCalendarWindow();
            }
        });

        //Right click on entry, contextmenu
        calendar.setEntryContextMenuCallback(param -> {

            EntryViewBase<?> entryView = param.getEntryView();
            tempEntry = entryView.getEntry();

            ContextMenu menu = new ContextMenu();

            edit = new MenuItem("Edit");
            delete = new MenuItem("Delete");

            delete.setOnAction(e -> {
                Appointment appointment = entryToAppointment.get(tempEntry.getId());
                appointment.deleteFromLocalDatabase();
                tempEntry.removeFromCalendar();
            });

            menu.getItems().addAll(edit, delete);

            return menu;
        });

        //Disable all other right click events
        calendar.setContextMenuCallback(null);

        //Link entry made by mouse drag/click in calendarView to appointment
        calendar.setEntryFactory(param -> {
            DateControl dateControl = param.getDateControl();

            ZonedDateTime time = param.getZonedDateTime();
            DayOfWeek firstDayOfWeek = dateControl.getFirstDayOfWeek();

            VirtualGrid grid = dateControl.getVirtualGrid();
            ZonedDateTime lowerTime = grid.adjustTime(time, false, firstDayOfWeek);
            ZonedDateTime upperTime = grid.adjustTime(time, true, firstDayOfWeek);

            if (Duration.between(time, lowerTime).abs()
                    .minus(Duration.between(time, upperTime).abs())
                    .isNegative()) {
                time = lowerTime;
            } else {
                time = upperTime;
            }

            Entry<Object> entry =
                    new Entry<>(Messages.getString("DateControl.DEFAULT_ENTRY_TITLE"));

            Interval interval = new Interval(
                    time.toLocalDateTime(),
                    time.toLocalDateTime().plusHours(1),
                    time.getZone()
            );

            entry.setInterval(interval);

            if (dateControl instanceof AllDayView) {
                entry.setFullDay(true);
            }
            //Default entry factory code up until here

            LocalDateTime start = LocalDateTime.of(entry.getStartDate(), entry.getStartTime());
            LocalDateTime end = LocalDateTime.of(entry.getEndDate(), entry.getEndTime());
            String title = entry.getTitle();

            Appointment appointment = new Appointment(title, start, end, "");
            appointment.setCalendarId(entry.getId());
            entryToAppointment.put(entry.getId(), appointment);
            appointment.addToLocalDatabase();

            //Update appointment data (changes made by calendarView)
            entry.intervalProperty().addListener((obs, oldInterval, newInterval) -> {
                appointment.setTimeStart(newInterval.getStartDateTime());
                appointment.setTimeEnd(newInterval.getEndDateTime());
                appointment.update();

            });
                return entry;
        });

    }
    public void setEventHandlers() {
        //create entry (for calendarView & local database)
        createEntry.setOnAction(e -> {
            try {
                setEvent();
            } catch (NullPointerException n) {
                System.out.println(n.getMessage());
            }
        });

        //Show sidebar
        sidebarIcon.setOnMouseClicked(e -> {
            if (!sidebarIsOpen) {
                sidebar.setMinWidth(200);
                sidebar.getChildren().addAll(settingsIcon, designIcon);
                sidebarIsOpen = true;
            } else {
                sidebar.setMinWidth(0);
                sidebar.setMaxWidth(0);
                sidebar.getChildren().removeAll(settingsIcon, designIcon);
                sidebarIsOpen = false;
            }
        });

        //Show entryWindow
        create.setOnAction(e -> {
           if(!createWindowIsOpen) {
               dashboard.getChildren().add(1, entryWindow);
               createWindowIsOpen = true;
           }
        });

        //Close entryWindow
        closeButton.setOnAction(e -> {
                dashboard.getChildren().remove(entryWindow);
                createWindowIsOpen = false;
        });

        //Disable automatic entry on double click
        calendar.getMonthPage().getMonthView().addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getClickCount() == 2) {
                event.consume();
            }
        });

        //Block popup window
        calendar.setEntryDetailsCallback(param -> {
            return false;
        });

        edit.setOnAction(e -> {

        });

        //Process date input for dateField1
        dateField1.setOnAction(e -> {
           String input = dateField1.getText();
           try {
               //Check if date format is correct and set format to "MM.dd.yyyy" (standardize)
               LocalDate processedInput = getDate(input);
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
                LocalDate processedInput = getDate(input);
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
                String time = getTimeInFormat(input);

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
                String time = getTimeInFormat(input);

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
                    entryWindow.getChildren().remove(yearMonthViewPane2);
                    yearMonthCalendar2IsOpen = false;
                }
                entryWindow.getChildren().add(3, yearMonthViewPane1);
                yearMonthCalendar1IsOpen = true;
                entryWindow.getChildren().removeAll(descriptionArea, createEntryPane);
            } else {
                entryWindow.getChildren().remove(yearMonthViewPane1);
                yearMonthCalendar1IsOpen = false;
                entryWindow.getChildren().add(3, descriptionArea);
                entryWindow.getChildren().add(4, createEntryPane);
            }
        });

        //Open mini calendar (yearMonthView) for easier date selection, ending date
        calendarIcon2.setOnAction(e -> {
            if(!yearMonthCalendar2IsOpen) {
                if(yearMonthCalendar1IsOpen) {
                    entryWindow.getChildren().remove(yearMonthViewPane1);
                    yearMonthCalendar1IsOpen = false;
                }
                entryWindow.getChildren().add(3, yearMonthViewPane2);
                yearMonthCalendar2IsOpen = true;
                entryWindow.getChildren().removeAll(descriptionArea, createEntryPane);
            } else {
                entryWindow.getChildren().remove(yearMonthViewPane2);
                yearMonthCalendar2IsOpen = false;
                entryWindow.getChildren().add(3, descriptionArea);
                entryWindow.getChildren().add(4, createEntryPane);
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
    public void setThreads() {
        //Thread for adjusting current time
        Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
            @Override
            public void run() {
                while (true) {
                    Platform.runLater(() -> {
                        calendar.setToday(LocalDate.now());
                        calendar.setTime(LocalTime.now());
                        updateTodayTimeLabels();
                    });

                    try {
                        // update every 10 seconds
                        sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();
    }
    public void setEvent() {
        //Extract all the input from TextFields and TextAreas
        String titleText = title.getText();
        String timeOfDay1 = timeBox1.getEditor().getText();
        String timeOfDay2 = timeBox2.getEditor().getText();
        String dateTime1 = dateField1.getText();
        String dateTime2 = dateField2.getText();
        String[] timeOfDayArray1 = timeOfDay1.split(":");
        String[] timeOfDayArray2 = timeOfDay2.split(":");
        String[] dateTimeArray1 = dateTime1.split("\\.");
        String[] dateTimeArray2 = dateTime2.split("\\.");
        String description = descriptionArea.getText();

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

        //link calendarView entry and database entry together
        entryToAppointment.put(entry.getId(),appointment);

        general.addEntry(entry);
    }
    public void loadAppointments() {
       List<Appointment> appointmentList = LocalDatabase.appointmentList();
       if(appointmentList != null) {
           for (Appointment appointment : appointmentList) {
               Entry<String> entry = new Entry<>(appointment.getTitle());
               entry.setInterval(appointment.getTimeStart(), appointment.getTimeEnd());
               appointment.setCalendarId(entry.getId());
               entryToAppointment.put(entry.getId(), appointment);
               general.addEntry(entry);
           }
       }
    }
    public LocalDate getDate(String input) throws IllegalArgumentException {

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
        //Note regarding invalid day input: A DateTimeParseException does not occur when the day exceeds the number
        // of days in the selected month. Instead, the highest valid day of that month is selected.
        // A DateTimeParseException only occurs when the day is set to 32 or higher.
    }
    public String getTimeInFormat(String input) throws IllegalArgumentException {
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
    public void initializeTimeBoxes() {
        ObservableList<String> time1 = FXCollections.observableList(createTimeOfDayChoices());
        ObservableList<String> time2 = FXCollections.observableList(createTimeOfDayChoices());
        timeBox1 = new ComboBox<>(time1);
        timeBox2 = new ComboBox<>(time2);
        timeBox1.setEditable(true);
        timeBox2.setEditable(true);

        LocalTime time = LocalTime.now();
        String[] timeArray = time.toString().split(":");
        int currentHour =  Integer.parseInt(timeArray[0]);
        int currentMinute = Integer.parseInt(timeArray[1]);
        String inputMinute;
        String inputHour;

        if(currentMinute < 30) {
            inputMinute = "30";
        } else {
            inputMinute = "00";
            currentHour++;
            if(currentHour == 24) {
                currentHour = 0;
            }
        }

        if(currentHour < 10) {
            inputHour = "0" + currentHour;
        } else {
            inputHour = currentHour + "";
        }

        String input1 = inputHour + ":" + inputMinute;

        timeBox1.getSelectionModel().select(input1);
        tempTime1 = input1;

        currentHour++;
        if(currentHour == 24) {
            currentHour = 0;
            dateField2.setText(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        }

        inputHour = currentHour + "";
        String input2 = inputHour + ":" + inputMinute;

        timeBox2.getSelectionModel().select(input2);
        tempTime2 = input2;
    }
    public void setStylesheets() {
        calendar.getStylesheets().add(
                getClass().getResource("/CalendarFX.css").toExternalForm()
        );

        yearMonthCalendar1.getStylesheets().add(
                getClass().getResource("/YearMonthView.css").toExternalForm()
        );

        yearMonthCalendar2.getStylesheets().add(
                getClass().getResource("/YearMonthView.css").toExternalForm()
        );
    }

    @Override
    public void start(Stage stage) {

        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        Locale.setDefault(Locale.ENGLISH);

        LocalDatabase.initializeAppointmentTable();
        initializeGlobalVariables();
        createPanes();
        createLabels();
        createButtons();
        setStyleSettings();
        configureLayout();
        buildLayout();
        setCalendarView();
        setEventHandlers();
        setThreads();
        setStylesheets();
        loadAppointments();

        Scene scene = new Scene(mainPane);
        stage.setTitle("Calendar");
        stage.setScene(scene);
        stage.show();
    }

        public static void main (String[] args){
            launch(args);

        }

    }