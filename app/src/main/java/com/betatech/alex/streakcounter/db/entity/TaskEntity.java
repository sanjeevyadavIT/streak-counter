package com.betatech.alex.streakcounter.db.entity;

import com.betatech.alex.streakcounter.model.Task;
import com.betatech.alex.streakcounter.util.DateUtils;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Model class for a Task. Read {@link Task} for documentation
 */
@Entity(tableName = "tasks")
public class TaskEntity implements Task {

    @PrimaryKey
    @NonNull
    private int id;
    private String title;
    private String location;
    private int minutes;
    private boolean showNotification;
    private Date time;
    private Date lastDate;
    private int currentStreak;
    private long elapsedTimeInMilliSeconds;
    private Date progressDate;

    @Ignore
    public TaskEntity() {/*Empty constructor*/}

    public TaskEntity(int id, String title, String location, int minutes, boolean showNotification, Date time, Date lastDate) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.minutes = minutes;
        this.showNotification = showNotification;
        this.time = time;
        this.lastDate = lastDate;
        currentStreak = 0; //initially it is 0
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public int getMinutes() {
        return minutes;
    }

    public long getMinutesInMilliSeconds(){
        return minutes*60*1000;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    @Override
    public boolean isShowNotification() {
        return showNotification;
    }

    public void setShowNotification(boolean showNotification) {
        this.showNotification = showNotification;
    }

    @Override
    public Date getTime() {
        return time;
    }

    @Override
    public String getTimeStr() {
        return DateUtils.getFormattedTime(this.time);
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    @Override
    public Date getLastDate() {
        return lastDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    @Override
    public long getElapsedTimeInMilliSeconds() {
        return elapsedTimeInMilliSeconds;
    }

    public void setElapsedTimeInMilliSeconds(long elapsedTimeInMilliSeconds) {
        this.elapsedTimeInMilliSeconds = elapsedTimeInMilliSeconds;
    }

    @Override
    public Date getProgressDate() {
        return progressDate;
    }

    public void setProgressDate(Date progressDate) {
        this.progressDate = progressDate;
    }
}
