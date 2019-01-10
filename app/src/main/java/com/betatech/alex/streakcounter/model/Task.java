/*
 * MIT License
 *
 * Copyright (c) [2019] [sanjeev yadav]
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
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
