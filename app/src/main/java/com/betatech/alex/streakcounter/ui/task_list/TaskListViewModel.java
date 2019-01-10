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
package com.betatech.alex.streakcounter.ui.task_list;

import android.app.Application;

import com.betatech.alex.streakcounter.db.TasksRepository;
import com.betatech.alex.streakcounter.db.entity.TaskEntity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * ViewModel class used in {@link TaskListActivity}
 */
public class TaskListViewModel extends AndroidViewModel {

    private TasksRepository tasksRepository;

    private LiveData<List<TaskEntity>> taskList;

    private int numberOfTask;



    public TaskListViewModel(@NonNull Application application) {
        super(application);
        tasksRepository = TasksRepository.getInstance(application);
        numberOfTask = -1;
        loadData();
    }

    private void loadData() {
        taskList = new MutableLiveData<>(); //used for showing loader
        taskList = tasksRepository.getTasks();
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Getters and setters ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~**/

    public LiveData<List<TaskEntity>> getTaskList() {
        return taskList;
    }

    public void updateTasks(List<TaskEntity> updatedList) {
        tasksRepository.updateTasks(updatedList.toArray(new TaskEntity[0]));
    }

    public int getNumberOfTask() {
        return numberOfTask;
    }

    public void setNumberOfTask(int numberOfTask) {
        this.numberOfTask = numberOfTask;
    }
}
