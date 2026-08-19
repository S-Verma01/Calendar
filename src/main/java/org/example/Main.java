package org.example;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.Styles;
import com.calendarfx.model.*;
import com.calendarfx.model.Calendar;
import com.calendarfx.view.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class Main extends Application {

    //Static variables
    private final static Calendar<?> general = new Calendar("general");
    private final static Map<String, Appointment> entryToAppointment = new HashMap<>();

    //General variables
    private CalendarView calendar;
    private boolean sidebarIsOpen;
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
    private HBox center;

    //Labels
    private Label sidebarIcon;
    private Label settingsIcon;
    private Label designIcon;
    private Label currentDate;

    //Buttons
    private Button create;

    //Menu elements
    private MenuItem edit;
    private MenuItem delete;

    //entryBases
    private CreateEntryWindow createEntryWindow;
    private EditEntryWindow editEntryWindow;

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
        space = new Region();
        edit = new MenuItem("Edit");
        delete = new MenuItem("Delete");
        createEntryWindow = new CreateEntryWindow(general, entryToAppointment);
        editEntryWindow = new EditEntryWindow();
        createEntryWindow.setIsOpen(false);
        sidebarIsOpen = false;
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
        center = new HBox();
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
        create = new Button("+");
    }
    public void setStyleSettings() {
        //Style Settings (colors, font etc...)
        mainPane.setStyle("-fx-background-color: #101010");
        top.setStyle("-fx-border-color: #30363d; -fx-border-width: 2px; -fx-background-color: #30363d; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        sidebarIcon.getStyleClass().add(Styles.TITLE_1);
        sidebarIcon.setStyle("-fx-text-fill: -color-fg-default;");
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
    }
    public void buildLayout() {
        //Adding layout elements together
        mainPane.getChildren().addAll(sidebar, home);
        home.getChildren().addAll(top, center);
        top.getChildren().addAll(sidebarIcon, create);
        center.getChildren().addAll(calendarGroup, dashboard);
        dashboard.getChildren().addAll(currentDate, space, promptBar);
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

        //Right click on entry, contextmenu elements, event handling
        calendar.setEntryContextMenuCallback(param -> {

            EntryViewBase<?> entryView = param.getEntryView();
            Entry<?> selectedEntry = entryView.getEntry();

            ContextMenu menu = new ContextMenu();

            edit = new MenuItem("Edit");
            delete = new MenuItem("Delete");

            delete.setOnAction(e -> {
                Appointment appointment = entryToAppointment.get(selectedEntry.getId());
                appointment.deleteFromLocalDatabase();
                selectedEntry.removeFromCalendar();
            });

            edit.setOnAction(e -> {
                if(!editEntryWindow.getIsOpen()) {
                    if(createEntryWindow.getIsOpen()) {
                        dashboard.getChildren().remove(createEntryWindow);
                        createEntryWindow.setIsOpen(false);
                    }
                    dashboard.getChildren().add(1, editEntryWindow);
                    editEntryWindow.setIsOpen(true);
                }
                Appointment appointment = entryToAppointment.get(selectedEntry.getId());
                editEntryWindow.displayEntry(appointment, selectedEntry);
            });

            //Highlight selected entry
            calendar.clearSelection();
            calendar.getSelections().add(selectedEntry);

            menu.getItems().addAll(edit, delete);

            return menu;
        });

        //Disable all other right click events
        calendar.setContextMenuCallback(null);

        //Link the entry, made by mouse drag/click in calendarView, to appointment
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

            //Show editEntryWindow
            if(!editEntryWindow.getIsOpen()) {
                if(createEntryWindow.getIsOpen()) {
                    dashboard.getChildren().remove(createEntryWindow);
                    createEntryWindow.setIsOpen(false);
                }
                dashboard.getChildren().add(1, editEntryWindow);
                editEntryWindow.setIsOpen(true);
            }

            //Update appointment data (changes made by calendarView)
            entry.intervalProperty().addListener((obs, oldInterval, newInterval) -> {
                appointment.setTimeStart(newInterval.getStartDateTime());
                appointment.setTimeEnd(newInterval.getEndDateTime());
                appointment.update();
                editEntryWindow.displayEntry(appointment, entry);

            });
            //Refresh editEntryWindow
            editEntryWindow.displayEntry(appointment, entry);

            return entry;
        });
    }
    public void setEventHandlers() {
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

        //Show createEntryWindow
        create.setOnAction(e -> {
           if(!createEntryWindow.getIsOpen()) {
               if(editEntryWindow.getIsOpen()) {
                   dashboard.getChildren().remove(editEntryWindow);
                   editEntryWindow.setIsOpen(false);
               }
               dashboard.getChildren().add(1, createEntryWindow);
               createEntryWindow.setIsOpen(true);
           }
        });

        //Close createEntryWindow
        createEntryWindow.getCloseButton().setOnAction(e -> {
            dashboard.getChildren().remove(createEntryWindow);
            createEntryWindow.setIsOpen(false);
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

        //Event on selecting entry
        calendar.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if(event.getButton().equals(MouseButton.PRIMARY)) {
                if(event.getTarget() instanceof EntryViewBase<?>) {
                    EntryViewBase<?> entryView  = ((EntryViewBase<?>)event.getTarget());
                    Entry<?> entry = entryView.getEntry();
                    if(editEntryWindow.getIsOpen()) {
                        Appointment appointment = entryToAppointment.get(entry.getId());
                        editEntryWindow.displayEntry(appointment, entry);
                    }
                }
            }
        });

        //Close editEntryWindow
        editEntryWindow.getCloseButton().setOnAction(e -> {
            if(editEntryWindow.getIsOpen()) {
                dashboard.getChildren().remove(editEntryWindow);
                editEntryWindow.setIsOpen(false);
            }
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
    public void loadAllAppointments() {
       List<Appointment> appointmentList = LocalDatabase.appointmentList();
       if(appointmentList != null) {
           for (Appointment appointment : appointmentList) {
               Entry<String> entry = new Entry<>(appointment.getTitle());
               entry.setInterval(appointment.getTimeStart(), appointment.getTimeEnd());
               appointment.setCalendarId(entry.getId());
               entryToAppointment.put(entry.getId(), appointment);
               general.addEntry(entry);
               //Add listener to every initialized entry to display changes made by calendarView on the editEntryWindow
               entry.intervalProperty().addListener((obs, oldInterval, newInterval) -> {
                   appointment.setTimeStart(newInterval.getStartDateTime());
                   appointment.setTimeEnd(newInterval.getEndDateTime());
                   appointment.update();
                   editEntryWindow.displayEntry(appointment, entry);
               });
           }
       }
    }
    public void setStyle() {
        calendar.getStylesheets().add(
                getClass().getResource("/CalendarFX.css").toExternalForm()
        );
    }
    public static Map<String, Appointment> getEntryToAppointment() {
        return entryToAppointment;
    }
    public static Calendar<?> getGeneral() {
        return general;
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
        setStyle();
        loadAllAppointments();

        Scene scene = new Scene(mainPane);
        stage.setTitle("Calendar");
        stage.setScene(scene);
        stage.show();
    }

        public static void main (String[] args){
            launch(args);

        }

    }