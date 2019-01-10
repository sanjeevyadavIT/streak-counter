package com.betatech.alex.streakcounter;

import android.content.Context;
import android.util.Log;

import com.betatech.alex.streakcounter.db.AppDatabase;
import com.betatech.alex.streakcounter.db.dao.TasksDao;
import com.betatech.alex.streakcounter.db.entity.TaskEntity;
import com.betatech.alex.streakcounter.util.DateUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

/**
 *Test all the query written in {@link TasksDao}
 */
@RunWith(AndroidJUnit4.class)
public class RoomDatabaseTest {

    public static final String TAG= RoomDatabaseTest.class.getSimpleName();

    private AppDatabase mDb;
    private TasksDao mTaskDao;
    private String id;
    private final int NUMBER_OF_TASK = 5;

    @Before
    public void createDb(){
        Context context = InstrumentationRegistry.getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        mTaskDao = mDb.tasksDao();
        id = UUID.randomUUID().toString();
    }

    @After
    public void closeDb() throws IOException {
        mDb.close();
    }

    /**
     * Test to check whether single insert works or not
     */
    @Test
    public void insertTask(){
        TaskEntity entity = new TaskEntity("Learn spanish", DateUtils.getYesterdayDateWithoutTime(),DateUtils.getCurrentDateWithoutTime(),DateUtils.getCurrentDateWithoutTime(),id);
        long[] rowId = mTaskDao.insertTask(entity);
        assert rowId.length == 1;
        assert rowId[0] != 0;
    }

    /**
     * Test to check whether getTaskById works,
     * we pass the above inserted Task id and retrieve it
     */
    @Test
    public void getTaskById(){
        insertTask();
        LiveData<TaskEntity> entity =  mTaskDao.getTaskById(id);

        assert entity.getValue()!=null;
        assert entity.getValue().getId().equals(id);
    }

    /**
     * Inserting multiples tasks, the rowIds should be equal to the
     * number of task inserted.
     */
    @Test
    public void insertTasks(){

        List<TaskEntity> tasks = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_TASK; i++) {
            tasks.add(new TaskEntity((i+1) + " Task",DateUtils.getYesterdayDateWithoutTime(),DateUtils.getCurrentDateWithoutTime(),DateUtils.getCurrentDateWithoutTime(),UUID.randomUUID().toString()));
        }

        long[] rowIds = mTaskDao.insertTask(tasks.toArray(new TaskEntity[0])); // rowIds should look like this [1, 2, 3, 4, 5]

        assert rowIds.length >= NUMBER_OF_TASK;
    }

    /**
     * Getting all task, should be equal to {@link #NUMBER_OF_TASK} + 1
     * due to previous test insertTask and insertTasks
     */
    @Test
    public void getAllTask(){
        insertTask();
        LiveData<List<TaskEntity>> taskList = mTaskDao.getTasks();

        assert taskList.getValue() != null;
        assert taskList.getValue().size() > 0;
    }

    /**
     * Test to check whether single task can be updated
     */
    @Test
    public void updateSingleTask(){
        insertTask();
        LiveData<TaskEntity> task = mTaskDao.getTaskById(id);
        String newTaskTitle = "Learn French";

        assert task.getValue() !=null ;
        task.getValue().setTitle(newTaskTitle); //setting new title
        task.getValue().setCurrentStreak(0);   //setting new streak
        task.getValue().setLastDate(DateUtils.getPastDateWithoutTime(2));    //setting lastDate to be 2 day's before

        int numberOfTaskUpdated = mTaskDao.updateTasks(task.getValue());
        assert numberOfTaskUpdated == 1;
    }

    /**
     * Temporary test,
     *
     * TODO : Extract it to it's own Test Class
     */
    @Test
    public void testDateUtils(){
        Date dayBeforeYesterday = DateUtils.getPastDateWithoutTime(2);
        Date yesterday = DateUtils.getYesterdayDateWithoutTime();
        Date today = DateUtils.getCurrentDateWithoutTime();

        assert DateUtils.subtractDates(today,dayBeforeYesterday) == 2;
        assert DateUtils.subtractDates(today,yesterday) == 1;
        assert today.getTime() == DateUtils.getCurrentDateWithoutTime().getTime();
    }


    /**
     * Test to increment the streak of the task
     */
    @Test
    public void incrementStreak(){
        insertTask();
        mTaskDao.incrementStreak(id,DateUtils.getCurrentDateWithoutTime());

        LiveData<TaskEntity> tasks = mTaskDao.getTaskById(id);
        assert tasks.getValue() != null;
        assert tasks.getValue().getCurrentStreak() > 0 ;
        assert tasks.getValue().getLastDate().getTime() == DateUtils.getCurrentDateWithoutTime().getTime();
    }

    /**
     * Test to delete a single task
     */
    @Test
    public void deleteSingleTasks(){
        insertTask();
        LiveData<TaskEntity> tasks = mTaskDao.getTaskById(id);
        assert tasks.getValue() != null;
        int taskDeleted = mTaskDao.deleteTasks(tasks.getValue());

        assert taskDeleted == 1;
    }

    /**
     * Test to delete a single task
     */
    /*@Test
    public void deleteTasks(){
       LiveData<List<TaskEntity>> tasks = mTaskDao.getTasks();

       assert tasks.getValue() != null;
       int totalTask = tasks.getValue().size();
       int numberOfTaskDeleted = mTaskDao.deleteTasks(tasks.getValue().toArray(new TaskEntity[0]));

       assert numberOfTaskDeleted == totalTask;
    }*/


}
