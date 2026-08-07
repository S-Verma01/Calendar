package org.example;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.Styles;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class Main extends Application {

    private CalendarView calendar;
    private boolean sidebarIsOpen;
    private boolean createWindowIsOpen;
    private TextField title;
    private ChoiceBox<Integer> dayBox1;
    private ChoiceBox<Integer> dayBox2;
    private ChoiceBox<String> monthBox1;
    private ChoiceBox<String> monthBox2;
    private ChoiceBox<Integer> yearBox1;
    private ChoiceBox<Integer> yearBox2;
    private ChoiceBox<Integer> hourBox1;
    private ChoiceBox<Integer> hourBox2;
    private ChoiceBox<Integer> minuteBox1;
    private ChoiceBox<Integer> minuteBox2;
    private ObservableList<Integer> monthWith31Days;
    private ObservableList<Integer> monthWith30Days;
    private ObservableList<Integer> monthWith28Days;
    private Calendar general;
    private Group calendarGroup;
    private Region space;

    //Panes
    private HBox mainPane;
    private VBox home;
    private HBox top;
    private BorderPane promptBar;
    private VBox sidebar;
    private VBox dashboard;
    private VBox entryWindow;
    private Button closeButton;
    private VBox closeArea;
    private FlowPane timeOfDayBoxes1;
    private GridPane entryGrid;
    private FlowPane timeOfDayBoxes2;
    private HBox center;
    private VBox entryArea;

    //Labels
    private Label sidebarIcon;
    private Label settingsIcon;
    private Label designIcon;
    private Label currentDate;

    //Buttons
    private Button createEvent;
    private Button create;

    //Helper methods
    public ObservableList<Integer> createMonth (int days) {
        List<Integer> d = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            d.add(i + 1);
        }
        ObservableList<Integer> daysList = FXCollections.observableList(d);
        return FXCollections.observableList(daysList);
    }
    public void updateTodayTimeLabels() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        currentDate.setText("Today, " + today.format(format));
    }
    public void fixCalendarWindow() {
        //Used for keeping a consistent window size of calendarView
        calendar.setMinWidth(1025);
        calendar.setMaxWidth(1025);
        calendar.setMinHeight(900);
        calendar.setMaxHeight(900);
    }
    public void refreshDayBox(ChoiceBox<String> monthBox, ChoiceBox<Integer> dayBox) {
        //Used for setting numbers of days in dayBoxes according to the selected month
        String currentMonth = monthBox.getSelectionModel().getSelectedItem();
        Integer currentDay = dayBox.getSelectionModel().getSelectedItem();

        if (currentMonth.equals("January") ||
                currentMonth.equals("March") ||
                currentMonth.equals("May") ||
                currentMonth.equals("July") ||
                currentMonth.equals("August") ||
                currentMonth.equals("October") ||
                currentMonth.equals("December")) {
            dayBox.setItems(monthWith31Days);
        } else if (currentMonth.equals("April") ||
                currentMonth.equals("June") ||
                currentMonth.equals("September") ||
                currentMonth.equals("November")) {
            dayBox.setItems(monthWith30Days);
        } else if (currentMonth.equals("February")) {
            dayBox.setItems(monthWith28Days);
        }

        if (!(currentDay == null)) {
            if (currentDay >= 29 && monthBox.getSelectionModel().getSelectedItem().equals("February")) {
                dayBox.getSelectionModel().select(null);
            } else if (currentDay == 31 && (monthBox.getSelectionModel().getSelectedItem().equals("April")
                    || monthBox.getSelectionModel().getSelectedItem().equals("June")
                    || monthBox.getSelectionModel().getSelectedItem().equals("September")
                    || monthBox.getSelectionModel().getSelectedItem().equals("November"))) {
                dayBox.getSelectionModel().select(null);
            } else {
                dayBox.getSelectionModel().select(currentDay);
            }
        }
    }

    public void initializeGlobalVariables() {
        //Global Variables
        calendar = new CalendarView();
        sidebarIsOpen = false;
        createWindowIsOpen = false;
        title = new TextField();
        dayBox1 = new ChoiceBox<>();
        dayBox2 = new ChoiceBox<>();
        general = new Calendar("general");
        calendarGroup = new Group(calendar);
        space = new Region();

        //Global Variables monthWith31Days, monthWith30Days, monthWith28days
        monthWith31Days = createMonth(31);
        monthWith30Days = createMonth(30);
        monthWith28Days = createMonth(28);

        //Global Variables yearBox1, yearBox2
        List<Integer> yearsList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            yearsList.add(2026 + i);
        }
        ObservableList<Integer> years = FXCollections.observableList(yearsList);

        yearBox1 = new ChoiceBox<>(years);
        yearBox2 = new ChoiceBox<>(years);

        //Global Variables monthBox1, monthBox2
        String[] monthStrings = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        List<String> monthsList = Arrays.asList(monthStrings);
        ObservableList<String> months = FXCollections.observableList(monthsList);

        monthBox1 = new ChoiceBox<>(months);
        monthBox1.getSelectionModel().select(0);
        refreshDayBox(monthBox1, dayBox1);
        monthBox2 = new ChoiceBox<>(months);
        monthBox2.getSelectionModel().select(0);
        refreshDayBox(monthBox2, dayBox2);

        //Global Variables hourBox1, hourBox2
        List<Integer> hoursList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hoursList.add(i + 1);
        }
        ObservableList<Integer> hours = FXCollections.observableList(hoursList);

        hourBox1 = new ChoiceBox<>(hours);
        hourBox2 = new ChoiceBox<>(hours);

        //Global Variables minuteBox1, minuteBox2
        List<Integer> minutesList = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            minutesList.add(i + 1);
        }
        ObservableList<Integer> minutes = FXCollections.observableList(minutesList);

        minuteBox1 = new ChoiceBox<>(minutes);
        minuteBox2 = new ChoiceBox<>(minutes);
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
        closeArea = new VBox(closeButton);
        timeOfDayBoxes1 = new FlowPane();
        entryGrid = new GridPane();
        timeOfDayBoxes2 = new FlowPane();
        center = new HBox();
        entryArea = new VBox();
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
        createEvent = new Button("Create");
        create = new Button("+");
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
        closeArea.setAlignment(Pos.TOP_RIGHT);
        hourBox1.setPrefWidth(50);
        minuteBox1.setPrefWidth(50);
        hourBox2.setPrefWidth(50);
        minuteBox2.setPrefWidth(50);
        entryGrid.setVgap(5);
        entryGrid.setPadding(new Insets(3));
        dashboard.setPadding(new Insets(10));
        center.setSpacing(15);
        dashboard.setMinWidth(400);
        dashboard.setMaxWidth(400);
        dashboard.setMinHeight(715);
        dashboard.setMaxHeight(715);
        dashboard.setVgrow(space, Priority.ALWAYS);
        currentDate.setMaxWidth(Double.MAX_VALUE);
        currentDate.setAlignment(Pos.CENTER);
        sidebar.setMinWidth(0);
        sidebar.setMaxWidth(0);
        calendar.setScaleX(0.8);
        calendar.setScaleY(0.8);
    }
    public void buildLayout() {
        //Adding layout elements together
        top.getChildren().addAll(sidebarIcon, create);
        home.getChildren().add(top);
        timeOfDayBoxes1.getChildren().addAll(hourBox1, minuteBox1);
        timeOfDayBoxes2.getChildren().addAll(hourBox2, minuteBox2);
        entryWindow.getChildren().addAll(closeArea, title, entryGrid, createEvent);
        dashboard.getChildren().addLast(entryArea);
        dashboard.getChildren().addLast(space);
        dashboard.getChildren().addLast(promptBar);
        mainPane.getChildren().addAll(sidebar, home);
        center.getChildren().addAll(calendarGroup, dashboard);
        home.getChildren().add(center);
        dashboard.getChildren().addFirst(currentDate);
        entryGrid.add(timeOfDayBoxes1, 0, 0);
        entryGrid.add(monthBox1, 0, 1);
        entryGrid.add(dayBox1, 0, 2);
        entryGrid.add(yearBox1, 0, 3);
        entryGrid.add(timeOfDayBoxes2, 1, 0);
        entryGrid.add(monthBox2, 1, 1);
        entryGrid.add(dayBox2, 1, 2);
        entryGrid.add(yearBox2, 1, 3);
    }
    public void setCalendarView() {
        //Setting up calendarView
        calendar.showMonthPage();
        fixCalendarWindow();

        CalendarSource myCalendarSource = new CalendarSource("My Calendars");
        myCalendarSource.getCalendars().add(general);

        //Resize calendarView window accordingly after page selection
        calendar.selectedPageProperty().addListener((obs, oldPage, newPage) -> {
            if (newPage == CalendarView.Page.DAY) {
                fixCalendarWindow();
            } else if (newPage == CalendarView.Page.WEEK) {
                fixCalendarWindow();
            } else if (newPage == CalendarView.Page.MONTH) {
                fixCalendarWindow();
            } else if (newPage == CalendarView.Page.YEAR) {
                fixCalendarWindow();
            }
        });
    }
    public void setEventHandlers() {
        //adjust monthBoxes and dayBoxes
        monthBox1.setOnAction(e -> {
            refreshDayBox(monthBox1, dayBox1);
        });

        monthBox2.setOnAction(e -> {
            refreshDayBox(monthBox2, dayBox2);
        });

        //create entry
        createEvent.setOnAction(e -> {
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
               entryArea.getChildren().add(entryWindow);
               createWindowIsOpen = true;
           }
        });

        //Close entryWindow
        closeButton.setOnAction(e -> {
                entryArea.getChildren().remove(entryWindow);
                createWindowIsOpen = false;

        });
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
        //Extract all the input form entryWindow
        String titleText = title.getText();

        int dayStart = dayBox1.getSelectionModel().getSelectedItem();
        String monthStart = monthBox1.getSelectionModel().getSelectedItem();
        int yearStart = yearBox1.getSelectionModel().getSelectedItem();
        int hourStart = hourBox1.getSelectionModel().getSelectedItem();
        int minuteStart = minuteBox1.getSelectionModel().getSelectedItem();

        int dayEnd = dayBox2.getSelectionModel().getSelectedItem();
        String monthEnd = monthBox2.getSelectionModel().getSelectedItem();
        int yearEnd = yearBox2.getSelectionModel().getSelectedItem();
        int hourEnd = hourBox2.getSelectionModel().getSelectedItem();
        int minuteEnd = minuteBox2.getSelectionModel().getSelectedItem();

        months temp1 = months.valueOf(monthStart);
        int monthStartIndex = temp1.ordinal() + 1;
        months temp2 = months.valueOf(monthEnd);
        int monthEndIndex = temp2.ordinal() + 1;

        //Create entry with extracted input
        Entry<String> entry = new Entry<>(titleText);
        entry.changeStartDate(LocalDate.of(yearStart, monthStartIndex, dayStart));
        entry.changeStartTime(LocalTime.of(hourStart, minuteStart));
        entry.changeEndDate(LocalDate.of(yearEnd, monthEndIndex, dayEnd));
        entry.changeEndTime(LocalTime.of(hourEnd, minuteEnd));

        general.addEntry(entry);
    }

    @Override
    public void start(Stage stage) {

        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        Locale.setDefault(Locale.ENGLISH);
        
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

        Scene scene = new Scene(mainPane);

            scene.getStylesheets().add(
                    getClass().getResource("/CalendarFX.css").toExternalForm()
            );

        stage.setTitle("Calendar");
        stage.setScene(scene);
        stage.show();
    }

        public static void main (String[] args){
            launch(args);
        }

    }