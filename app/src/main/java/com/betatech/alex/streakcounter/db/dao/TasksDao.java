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
