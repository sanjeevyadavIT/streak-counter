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
package com.betatech.alex.streakcounter.db.dao;

import com.betatech.alex.streakcounter.db.entity.TaskEntity;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

/**
 * Data Access Object for the tasks table.
 */
@Dao
public interface TasksDao {

    /**
     * Select all tasks from the tasks table.
     *
     * @return all tasks.
     */
    @Query("SELECT * FROM tasks")
    LiveData<List<TaskEntity>> getTasks();

    /**
     * Select a task by id.
     *
     * @param taskId the task id.
     * @return the task with taskId.
     */
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    LiveData<TaskEntity> getTaskById(int taskId);

    /**
     * Insert a task in the database. If the task already exists, replace it.
     *
     * @param task the task to be inserted.
     * @return an array containing the rowId of tasks inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertTask(TaskEntity... task);

    /**
     * Update a list of tasks. Can be a single task also
     *
     * @param tasks all the tasks that need to be updated
     * @return an integer indicating the number of rows updated in the database
     */
    @Update
    int updateTasks(TaskEntity... tasks);

    /**
     * Increment the streak by 1 and set lastDate to be current date, and reset elapsed time to be zero for next day
     *
     * @param taskId    id of the task
     * @param currentDate today's date
     */
    @Query("UPDATE tasks SET currentStreak = currentStreak + 1, lastDate = :currentDate, elapsedTimeInMilliSeconds = 0 WHERE id = :taskId")
    void incrementStreak(int taskId, Date currentDate);

    /**
     * Delete a list of tasks, could be single too
     *
     * @return an integer indicating the number of rows removed from the database
     */
    @Delete
    int deleteTasks(TaskEntity... tasks);


}
