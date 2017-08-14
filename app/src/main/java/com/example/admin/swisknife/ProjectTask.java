package com.example.admin.swisknife;

/**
 * Created by Admin on 8/12/2017.
 */

public class ProjectTask {

    String taskName, taskDescription;
    int taskNumber;

    public ProjectTask(String taskName, String taskDescription, int taskNumber) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskNumber = taskNumber;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public int getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(int taskNumber) {
        this.taskNumber = taskNumber;
    }
}
