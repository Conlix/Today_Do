package com.example.today_do;

public class Task {
    int id;
    double creations_time, deadline;
    String topic, task, details;
    boolean today;

    public Task(int id, String topic, String task, String details, double creation_time, double deadline, boolean today){
        this.id = id;
        this.topic = topic;
        this.task = task;
        this.details = details;
        this.creations_time = creation_time;
        this.deadline = deadline;
        this.today = today;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCreations_time() {
        return creations_time;
    }

    public void setCreations_time(double creations_time) {
        this.creations_time = creations_time;
    }

    public double getDeadline() {
        return deadline;
    }

    public void setDeadline(double deadline) {
        this.deadline = deadline;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public boolean isToday() {
        return today;
    }

    public void setToday(boolean today) {
        this.today = today;
    }

}

