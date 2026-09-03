package org.example;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

//Rule:

public class CountdownWindow extends VBox {

    private List<Appointment> todayEvents;
    private Appointment nextEvent;
    private HBox layer1;
    private HBox layer2;
    private HBox layer3;

    private FlowPane titlePane;
    private FlowPane todayPane;
    private FlowPane nearFuturePane;

    private Region space1;
    private Region space2;
    private Region space3;

    private Label entryTitle1;
    private Label entryTitle2;
    private Label entryTitle3;
    private Label generalTitle;
    private Label todayTitle;
    private Label futureTitle;

    private Label countdown1;
    private Label countdown2;
    private Label countdown3;

    private boolean layer1IsActive;
    private boolean layer2IsActive;
    private boolean layer3IsActive;

    LocalDate currentDate;


    public CountdownWindow() {
        initializeGlobalVariables();
        initializeUpcomingEvents();
        initializeLabels();
        initializePanes();
        buildLayout();
        configureLayout();
        setStyleSettings();
    }

    private void initializeGlobalVariables() {
        space1 = new Region();
        space2 = new Region();
        space3 = new Region();
        currentDate = LocalDate.now();
    }
    private void setStyleSettings() {
        this.setStyle("-fx-border-color: #5a6572; -fx-border-width: 2px; -fx-background-color: #5a6572; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        layer1.setStyle("-fx-border-color: #00124d; -fx-border-width: 2px; -fx-background-color: #00124d; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        layer2.setStyle("-fx-border-color: #00124d; -fx-border-width: 2px; -fx-background-color: #00124d; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        layer3.setStyle("-fx-border-color: #000d33; -fx-border-width: 2px; -fx-background-color: #000d33; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        entryTitle1.setStyle("-fx-font-size: 1.35em; -fx-font-weight: bold;");
        entryTitle2.setStyle("-fx-font-size: 1.35em; -fx-font-weight: bold;");
        entryTitle3.setStyle("-fx-font-size: 1.35em; -fx-font-weight: bold;");
        countdown1.setStyle("-fx-font-size: 1.35em; -fx-font-weight: bold;");
        countdown2.setStyle("-fx-font-size: 1.35em; -fx-font-weight: bold;");
        countdown3.setStyle("-fx-font-size: 1.35em; -fx-font-weight: bold;");
        generalTitle.setStyle("-fx-font-size: 1.35em;");
        todayTitle.setStyle("-fx-font-size: 1.25em;");
        futureTitle.setStyle("-fx-font-size: 1.25em;");
    }
    private void configureLayout() {
        this.setSpacing(5);
        this.setPadding(new Insets(5));
        layer1.setPadding(new Insets(3));
        layer2.setPadding(new Insets(3));
        layer3.setPadding(new Insets(3));

    }
    private void initializeUpcomingEvents() {
        todayEvents = LocalDatabase.getTodaysUpcomingEvents(2);
        nextEvent = LocalDatabase.getNextUpcomingEvent();
    }
    private void initializeLabels() {
        int length = todayEvents.size();

        switch(length) {
            case 0: entryTitle1 = new Label("empty");
                    entryTitle2 = new Label("empty");
                    countdown1 = new Label("empty");
                    countdown2 = new Label("empty");
                    layer1IsActive = false;
                    layer2IsActive = false;
                break;
            case 1: entryTitle1 = new Label(todayEvents.get(0).getTitle());
                    entryTitle2 = new Label("empty");
                    countdown1 = new Label(getTimeDifferenceForDisplay(LocalDateTime.now(), todayEvents.get(0).getTimeStart()));
                    countdown2 = new Label("empty");
                    layer1IsActive = true;
                    layer2IsActive = false;
                break;
            case 2: entryTitle1 = new Label(todayEvents.get(0).getTitle());
                    entryTitle2 = new Label(todayEvents.get(1).getTitle());
                    countdown1 = new Label(getTimeDifferenceForDisplay(LocalDateTime.now(), todayEvents.get(0).getTimeStart()));
                    countdown2 = new Label(getTimeDifferenceForDisplay(LocalDateTime.now(), todayEvents.get(1).getTimeStart()));
                    layer1IsActive = true;
                    layer2IsActive = true;
                break;
            default: entryTitle1 = new Label(todayEvents.get(0).getTitle());
                     entryTitle2 = new Label(todayEvents.get(1).getTitle());
                     countdown1 = new Label(getTimeDifferenceForDisplay(LocalDateTime.now(), todayEvents.get(0).getTimeStart()));
                     countdown2 = new Label(getTimeDifferenceForDisplay(LocalDateTime.now(), todayEvents.get(1).getTimeStart()));
                    layer1IsActive = true;
                    layer2IsActive = true;
                break;
        }
        if(nextEvent != null) {
            entryTitle3 = new Label(nextEvent.getTitle());
            countdown3 = new Label(getTimeDifferenceForDisplay(LocalDateTime.now(), nextEvent.getTimeStart()));
            layer3IsActive = true;
        } else {
            entryTitle3 = new Label("empty");
            countdown3 = new Label("empty");
            layer3IsActive = false;
        }
        generalTitle = new Label("Upcoming Events");
        todayTitle = new Label("Today:");
        futureTitle = new Label("Near future:");
    }
    private void refreshLabels() {
        int length = todayEvents.size();

        switch(length) {
            case 0: entryTitle1.setText("empty");
                entryTitle2.setText("empty");
                countdown1.setText("empty");
                countdown2.setText("empty");
                layer1IsActive = false;
                layer2IsActive = false;
                break;
            case 1: entryTitle1.setText(todayEvents.get(0).getTitle());
                entryTitle2.setText("empty");
                countdown1.setText(getTimeDifferenceForDisplay(LocalDateTime.now(), todayEvents.get(0).getTimeStart()));
                countdown2.setText("empty");
                layer1IsActive = true;
                layer2IsActive = false;
                break;
            case 2: entryTitle1.setText(todayEvents.get(0).getTitle());
                entryTitle2.setText(todayEvents.get(1).getTitle());
                countdown1.setText(getTimeDifferenceForDisplay(LocalDateTime.now(), todayEvents.get(0).getTimeStart()));
                countdown2.setText(getTimeDifferenceForDisplay(LocalDateTime.now(), todayEvents.get(1).getTimeStart()));
                layer1IsActive = true;
                layer2IsActive = true;
                break;
            default: entryTitle1.setText(todayEvents.get(0).getTitle());
                entryTitle2.setText(todayEvents.get(1).getTitle());
                countdown1.setText(getTimeDifferenceForDisplay(LocalDateTime.now(), todayEvents.get(0).getTimeStart()));
                countdown2.setText(getTimeDifferenceForDisplay(LocalDateTime.now(), todayEvents.get(1).getTimeStart()));
                layer1IsActive = true;
                layer2IsActive = true;
                break;
        }
        if(nextEvent != null) {
            entryTitle3.setText(nextEvent.getTitle());
            countdown3.setText(getTimeDifferenceForDisplay(LocalDateTime.now(), nextEvent.getTimeStart()));
            layer3IsActive = true;
        } else {
            entryTitle3.setText("empty");
            countdown3.setText("empty");
            layer3IsActive = false;
        }
    }
    private void initializePanes() {
        layer1 = new HBox();
        layer2 = new HBox();
        layer3 = new HBox();
    }
    private void buildLayout() {
        this.getChildren().addAll(generalTitle, todayTitle, layer1, layer2, futureTitle, layer3);

        layer1.setHgrow(space1, Priority.ALWAYS);
        layer2.setHgrow(space2, Priority.ALWAYS);
        layer3.setHgrow(space3, Priority.ALWAYS);

        layer1.getChildren().addAll(entryTitle1, space1, countdown1);
        layer2.getChildren().addAll(entryTitle2, space2, countdown2);
        layer3.getChildren().addAll(entryTitle3, space3, countdown3);


    }
    private String getTimeDifferenceForDisplay(LocalDateTime start, LocalDateTime end) {

        if(start.equals(end) || end.isBefore(start)) {
            return null;
        }

        int startYear = start.getYear();
        int endYear = end.getYear();

        String res;

        if(startYear != endYear) {
            res = (endYear - startYear) + " years";
            return res;
        }

        Period bigDifference = Period.between(start.toLocalDate(), end.toLocalDate());
        Duration smallDifference = Duration.between(start, end);

            int years = bigDifference.getYears();
            if(years != 0) {
                res = years + " years";
                return res;
            }
            int months = bigDifference.getMonths();
            if(months != 0) {
                res = months + " months";
                return res;
            }
            long days = smallDifference.toDays();
            if(days != 0) {
                res = days + " days";
                return res;
            }
            long hours = smallDifference.toHours();
            if(hours != 0) {
                res = hours + " hours";
                return res;
            }
            long minutes = smallDifference.toMinutes();
            res = minutes + " min";
            return res;
    }
    public void refreshCountdown() {
        if(!currentDate.equals(LocalDate.now())) {
            refresh();
            currentDate = LocalDate.now();
        }

        if(layer1IsActive) {
            if(LocalDateTime.now().equals(todayEvents.get(0).getTimeStart()) || LocalDateTime.now().isAfter(todayEvents.get(0).getTimeStart())) {
                entryTitle1.setText("empty");
                countdown1.setText("empty");
                layer1IsActive = false;
            } else {
                countdown1.setText(getTimeDifferenceForDisplay(LocalDateTime.now(), todayEvents.get(0).getTimeStart()));
            }
        }

        if(layer2IsActive) {
            if(LocalDateTime.now().equals(todayEvents.get(1).getTimeStart()) || LocalDateTime.now().isAfter(todayEvents.get(1).getTimeStart())) {
                entryTitle2.setText("empty");
                countdown2.setText("empty");
                layer2IsActive = false;
            } else {
                countdown2.setText(getTimeDifferenceForDisplay(LocalDateTime.now(), todayEvents.get(1).getTimeStart()));
            }
        }

        if(layer3IsActive) {
            countdown3.setText(getTimeDifferenceForDisplay(LocalDateTime.now(), nextEvent.getTimeStart()));

            if(LocalDateTime.now().equals(nextEvent.getTimeStart()) || LocalDateTime.now().isAfter(nextEvent.getTimeStart())) {
                entryTitle3.setText("empty");
                countdown3.setText("empty");
                layer3IsActive = false;
            } else {
                countdown3.setText(getTimeDifferenceForDisplay(LocalDateTime.now(), nextEvent.getTimeStart()));
            }
        }
    }
    public void refresh() {
        initializeUpcomingEvents();
        refreshLabels();
        refreshCountdown();
    }
    public static void main(String[] args) {

    }
}
