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
