/*
 * <Count streak by daily working on a task>
 *  Copyright (C) <2019>  <Sanjeev Yadav>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.betatech.alex.streakcounter.model;

import java.util.Date;

/**
 * Structure : Task model class will be assigned to a single task
 * in which a streak is need to be maintained
 */
public interface Task {

    /**
     * Return unique id to identify a task
     *
     * @return task id
     */
    int getId();

    /**
     * Return string denoting the task title
     *
     * @return task title
     */
    String getTitle();

    /**
     * Return string denoting location of the task,
     * where user want to do it
     *
     * @return task location
     */
    String getLocation();

    /**
     * Returns the last date on which streak was maintained,
     * at the time of creation of task it will be current date - 1day
     *
     * @return last date on which task was completed
     */
    Date getLastDate();

    /**
     * Return the amount of minutes needed to be spent, to complete one streak for particular task,
     * Constraints : time >= 0 && time <= 1440
     *
     * @return minutes designated for this task
     */
    int getMinutes();

    /**
     * Returns time at which notification need to be shown, and user want to complete the task
     *
     * @return time for notification/execution of task
     */
    Date getTime();

    /**
     * Parse the Date object where time is saved, and return string in format h:mm a
     * If user hasn't set the time => return a null string
     * else returned the formatted string
     *
     * @return string containing time of execution of task
     */
    String getTimeStr();

    /**
     * Returns whether to show notification or not
     *
     * @return boolean : true=> show notification else don't show notification
     */
    boolean isShowNotification();

    /**
     * Returns the current streak user has maintained
     * int the task, 0 by default
     *
     * @return current streak count
     */
    int getCurrentStreak();

    /**
     * Return elapsed time, if user started the task, and pause it and close the task,
     * it will be only valid if lastDate will be equal to yesterday date,
     * which means the value stored in elapseTime is of today's progress
     *
     */
    long getElapsedTimeInMilliSeconds();

    /**
     * Return a date on which the progress has been made
     *
     * @return date on which progress is made
     */
    Date getProgressDate();


    /**
     * Whether to integrate with Google calender
     *
     * @return true to save task to google calender
     */
    //boolean getSaveTaskToGoogleCalender();

    /**
     * Returns the max number of days, streak has been
     * maintained in the task, 0 by default
     *
     * @return max streak maintained in the task
     */
    //int getMaxStreak();

}
