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
package com.betatech.alex.streakcounter.ui.detail_task;

import android.content.Intent;
import android.os.Bundle;

import com.betatech.alex.streakcounter.R;
import com.betatech.alex.streakcounter.databinding.ActivityTaskDetailBinding;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.MenuItem;

import static com.betatech.alex.streakcounter.util.TaskStateAnnotation.TASK_COMPLETED;
import static com.betatech.alex.streakcounter.util.TaskStateAnnotation.TASK_RESUMED;
import static com.betatech.alex.streakcounter.util.TaskStateAnnotation.TASK_STARTED;
import static com.betatech.alex.streakcounter.util.TaskStateAnnotation.TASK_STOPPED;

/**
 * Show Detail screen for task
 */
public class TaskDetailActivity extends AppCompatActivity {

    private TaskDetailViewModel taskDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        setupLayoutWithDataBinding();
        setupToolbar();

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // FIXME: 11/23/2018 Show a dialog if task is already running and user clicks a notification to start another task
            // FIXME: 11/23/2018 Build a correct TaskBuilder show that when user comes from notification back button redirects to TaskListActivity
            // FIXME: 11/23/2018 Check the condition : DOn't show notification if user has completed today's streak
            loadTaskFromDb();
            setupFragment();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!taskDetailViewModel.isResumeTaskAfterScreenOrientation() && taskDetailViewModel.getTaskRunningStatus().getValue() != null && (taskDetailViewModel.getTaskRunningStatus().getValue() != TASK_STOPPED || taskDetailViewModel.getTaskRunningStatus().getValue() != TASK_COMPLETED)) {
            taskDetailViewModel.saveTaskProgress();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // FIXME: 1/9/2019 OnNewIntent is not getting called, everytime a new Instance of Activity is being initiated
        setIntent(intent);
        Log.d("SANJEEV","Task id = "+intent.getIntExtra(TaskDetailFragment.ARG_TASK_ID, -1));
        if (taskDetailViewModel.getTaskRunningStatus() !=null && taskDetailViewModel.getTaskRunningStatus().getValue() !=null && taskDetailViewModel.getTaskRunningStatus().getValue() != TASK_RESUMED || taskDetailViewModel.getTaskRunningStatus().getValue() != TASK_STARTED) {
            Log.d("SANJEEV","Task is not running");
            loadTaskFromDb();
            setupFragment();
        }else{
            Log.d("SANJEEV","Task is running");
            // FIXME: 11/23/2018 Show a dialog to start another task
        }

    }

    /**
     * inflate R.menu.detail_menu to show delete icon on toolbar,
     * to allow user to delete current task
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
//            NavUtils.navigateUpTo(this, new Intent(this, TaskListActivity.class));
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        taskDetailViewModel = ViewModelProviders.of(this).get(TaskDetailViewModel.class); // Obtain the ViewModel component.
    }

    private void setupLayoutWithDataBinding() {
        // data binding
        ActivityTaskDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_task_detail);
        // Specify the current activity as the lifecycle owner.
        binding.setLifecycleOwner(this);
        // connect ViewModel to data binding
        binding.setViewmodel(taskDetailViewModel);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void loadTaskFromDb() {
        int mTaskId = getIntent().getIntExtra(TaskDetailFragment.ARG_TASK_ID, -1);
        taskDetailViewModel.setmTaskId(mTaskId);
    }

    private void setupFragment() {
        int mTaskId = getIntent().getIntExtra(TaskDetailFragment.ARG_TASK_ID, -1);
        Bundle arguments = new Bundle();
        arguments.putInt(TaskDetailFragment.ARG_TASK_ID,
                mTaskId);
        TaskDetailFragment fragment = new TaskDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.item_detail_container, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.item_detail_container);

        if (taskDetailViewModel.getTaskRunningStatus().getValue() != null && (taskDetailViewModel.getTaskRunningStatus().getValue() == TASK_STARTED || taskDetailViewModel.getTaskRunningStatus().getValue() == TASK_RESUMED) && fragment instanceof TaskDetailFragment) {
            ((TaskDetailFragment) fragment).showConfirmGoBackDialog();
        } else {
            super.onBackPressed();
        }
    }

}
