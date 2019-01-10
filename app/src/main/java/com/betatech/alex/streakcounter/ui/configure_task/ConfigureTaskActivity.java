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
package com.betatech.alex.streakcounter.ui.configure_task;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.betatech.alex.streakcounter.R;
import com.betatech.alex.streakcounter.databinding.ActivityConfigureTaskBinding;
import com.betatech.alex.streakcounter.db.entity.TaskEntity;
import com.betatech.alex.streakcounter.ui.detail_task.TaskDetailFragment;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

public class ConfigureTaskActivity extends AppCompatActivity {

    private ConfigureTaskViewModel configureTaskViewModel;
    private CoordinatorLayout coordinatorLayout;
    private long lastClickTimeForPickerButton = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupLayoutWithDataBinding();
        setupToolbar();
        initView();
        configureTask();
        observeDataFromViewModel();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
            onBackPressed();
            /*if(taskId==null){
                NavUtils.navigateUpTo(this, new Intent(this, TaskListActivity.class));
            }else{
                // FIXME: 10/22/2018 If in dual pane mode, the parent activity will be TaskListActivity, fix code in dualPaneMode
                NavUtils.navigateUpTo(this, new Intent(this, TaskDetailActivity.class));
            }*/
//            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setupLayoutWithDataBinding() {
        // Obtain the ViewModel component.
        configureTaskViewModel = ViewModelProviders.of(this).get(ConfigureTaskViewModel.class);
        // Inflate view and obtain an instance of the binding class.
        ActivityConfigureTaskBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_configure_task);
        // Assign the component to a property in the binding class.
        binding.setViewModel(configureTaskViewModel);
        binding.setLifecycleOwner(this);
    }

    private void initView() {
        coordinatorLayout = findViewById(R.id.cl_add_task); //For SnackBar to work properly
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.configure_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void configureTask() {
        int taskId = getIntent().getIntExtra(TaskDetailFragment.ARG_TASK_ID, -1);
        configureTaskViewModel.setConfigurationMode(taskId);
    }

    private void observeDataFromViewModel() {
        configureTaskViewModel.getTask().observe(this, this::setViewParameters);
        configureTaskViewModel.getValidationErrorMsg().observe(this, this::showSnackBar);
        configureTaskViewModel.getStatusFinishActivity().observe(this, this::_finishActivity);
    }

    /**
     * Set time once the Task has been setup
     */
    private void setViewParameters(TaskEntity taskEntity) {
        if (taskEntity != null && taskEntity.getTime() != null) {
            configureTaskViewModel.setTime(taskEntity.getTimeStr());
            configureTaskViewModel.setMinutesForTask(String.valueOf(taskEntity.getMinutes()));
        } else {
            configureTaskViewModel.setTime(getString(R.string.time_tag));
        }
    }


    /**
     * Show SnackBar with message
     *
     * @param message need to be shown
     */
    private void showSnackBar(String message) {
        if (message != null) {
            Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
            // FIXME: 10/22/2018 Use SingleLiveEvent
            configureTaskViewModel.consumedErrorMessage();
        }
    }

    /**
     * Finish activity if status == 1
     * <p>
     * todo :: Should show SnackBar in parent activity whether task has been inserted or edited successfully depending on the context
     */
    private void _finishActivity(Integer status) {
        if (status != null && status == 1) {
            finish();
        }
    }

    /**
     * OnClick listener attached to button[@+id/mbtn_ending_date]
     * helps user to pick time at which they want to execute time
     */
    public void showTimePickerDialog(View v) {
        if (SystemClock.elapsedRealtime() - lastClickTimeForPickerButton < 1000) {
            return;
        }
        lastClickTimeForPickerButton = SystemClock.elapsedRealtime();
        hideSoftKeyboard();
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    private void hideSoftKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}
