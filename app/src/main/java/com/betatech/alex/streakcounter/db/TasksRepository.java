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
