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
package com.betatech.alex.streakcounter.db;

import android.content.Context;
import android.os.AsyncTask;

import com.betatech.alex.streakcounter.db.dao.TasksDao;
import com.betatech.alex.streakcounter.db.entity.TaskEntity;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * Repository handling the operation with tasks
 * This class act as in intermediate between ViewModel
 * and network/database layer
 * 
 * TODO: 9/30/2018 Find how to correctly do database operation off the ui thread, and implement it
 * TODO: 9/30/2018 Implement other database operation, mentioned in {@link TasksDao}
 * FIXME: 16/10/2018 Remove all async task with a single entity(could be Executor)
 */
public class TasksRepository {

    private static TasksRepository sInstance = null;

    private TasksDao mTasksDao;
    //To execute operation off the UI thread
    private Executor executor;

    // Prevent direct instantiation.
    private TasksRepository(Context context) {
        mTasksDao = AppDatabase.getInstance(context).tasksDao();
    }

    /**
     * Helper method to initiate a singleton of TasksRepository
     *
     * @param context application or activity context
     */
    public static TasksRepository getInstance(Context context) {
        if (sInstance == null) {
            synchronized (TasksRepository.class) {
                if (sInstance == null) {
                    sInstance = new TasksRepository(context);
                }
            }
        }
        return sInstance;
    }

    /**
     * Gets tasks from local data source (SQLite)
     */
    public LiveData<List<TaskEntity>> getTasks(){
        return mTasksDao.getTasks();
    }

    /**
     * get single task, provided it's id
     *
     * @param taskId id of the task
     * @return single task wrapped in LiveData
     */
    public LiveData<TaskEntity> getTaskById(int taskId){
        return mTasksDao.getTaskById(taskId);
    }

    /**
     * Insert task into database, in a separate background thread
     */
    public void insertTask(TaskEntity task) {
        new InsertAsyncTask(mTasksDao).execute(task);
    }

    private static class InsertAsyncTask extends AsyncTask<TaskEntity, Void, Void> {

        private TasksDao mAsyncTasksDao;

        InsertAsyncTask(TasksDao dao) {
            mAsyncTasksDao = dao;
        }


        @Override
        protected Void doInBackground(TaskEntity... task) {
            mAsyncTasksDao.insertTask(task[0]);
            return null;
        }
    }

    /**
     * Update the list of task
     */
    public void updateTasks(TaskEntity... tasks) {
        new UpdateAllAsyncTask(mTasksDao).execute(tasks);
    }

    private static class UpdateAllAsyncTask extends AsyncTask<TaskEntity, Void, Void> {

        private TasksDao mAsyncTasksDao;

        UpdateAllAsyncTask(TasksDao dao) {
            mAsyncTasksDao = dao;
        }


        @Override
        protected Void doInBackground(TaskEntity... tasks) {
            mAsyncTasksDao.updateTasks(tasks);
            return null;
        }
    }

    public void incrementStreak(int taskId, Date currentDate) {
        new IncrementStreakAsyncTask(mTasksDao).execute(new TaskEntity(taskId,"","",0,false,null,currentDate));

    }
    private static class IncrementStreakAsyncTask extends AsyncTask<TaskEntity, Void, Void> {

        private TasksDao mAsyncTasksDao;

        IncrementStreakAsyncTask(TasksDao dao) {
            mAsyncTasksDao = dao;
        }


        @Override
        protected Void doInBackground(TaskEntity... tasks) {
            mAsyncTasksDao.incrementStreak(tasks[0].getId(),tasks[0].getLastDate());
            return null;
        }
    }

    /**
     * Delete a task given task id
     */
    public void deleteTask(TaskEntity task){
        new DeleteTaskAsyncTask(mTasksDao).execute(task);
    }

    private static class DeleteTaskAsyncTask extends AsyncTask<TaskEntity, Void, Void> {

        private TasksDao mAsyncTasksDao;

        DeleteTaskAsyncTask(TasksDao dao) {
            mAsyncTasksDao = dao;
        }


        @Override
        protected Void doInBackground(TaskEntity... tasks) {
            mAsyncTasksDao.deleteTasks(tasks);
            return null;
        }
    }


}
