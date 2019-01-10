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
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.betatech.alex.streakcounter.R;
import com.betatech.alex.streakcounter.databinding.TaskDetailBinding;
import com.betatech.alex.streakcounter.db.entity.TaskEntity;
import com.betatech.alex.streakcounter.ui.configure_task.ConfigureTaskActivity;
import com.betatech.alex.streakcounter.util.AlertDialogHelper;
import com.betatech.alex.streakcounter.util.DateUtils;
import com.betatech.alex.streakcounter.util.TaskStateAnnotation;
import com.gelitenight.waveview.library.WaveView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import static com.betatech.alex.streakcounter.util.TaskStateAnnotation.TASK_COMPLETED;
import static com.betatech.alex.streakcounter.util.TaskStateAnnotation.TASK_PAUSED;
import static com.betatech.alex.streakcounter.util.TaskStateAnnotation.TASK_RESUMED;
import static com.betatech.alex.streakcounter.util.TaskStateAnnotation.TASK_STARTED;
import static com.betatech.alex.streakcounter.util.TaskStateAnnotation.TASK_STOPPED;

/**
 * A fragment representing a single Item detail screen.
 * TODO : Current layout design (use of ConstraintLayout) will create problem with multi-window mode
 */
public class TaskDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_TASK_ID = "item_id";

    private TaskDetailViewModel taskDetailViewModel;

    private WaveView waveView;
    private WaveHelper waveHelper;
    private boolean beforeOnPauseTaskWasRunning = false;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TaskDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        TaskDetailBinding binding = DataBindingUtil.inflate(inflater, R.layout.task_detail, container, false);
        taskDetailViewModel = ViewModelProviders.of(getActivity()).get(TaskDetailViewModel.class);
        binding.setViewmodel(taskDetailViewModel);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupWaveView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        taskDetailViewModel.getTask().observe(this, this::onChanged);
        taskDetailViewModel.getTaskRunningStatus().observe(this, this::taskStatusChanged);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (taskDetailViewModel.getTaskRunningStatus().getValue()!=null && (taskDetailViewModel.getTaskRunningStatus().getValue() ==TASK_STARTED || taskDetailViewModel.getTaskRunningStatus().getValue() == TASK_RESUMED)) {
            beforeOnPauseTaskWasRunning = true;
            manuallyPauseTask();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(beforeOnPauseTaskWasRunning){
            taskDetailViewModel.setResumeTaskAfterScreenOrientation(true);
        }
    }

    private void onChanged(TaskEntity task) {
        if (task != null) {
            if(task.getElapsedTimeInMilliSeconds() > 0){
                if(DateUtils.subtractDates(DateUtils.getCurrentDateWithoutTime(), task.getProgressDate()) == 0){
                    new Handler().postDelayed(()->{
                        if(waveHelper!=null){
                            //hack
                            taskDetailViewModel.startTask();
                            if(!taskDetailViewModel.isResumeTaskAfterScreenOrientation()){
                                taskDetailViewModel.pauseTask();
                            }else{
                                taskDetailViewModel.setResumeTaskAfterScreenOrientation(false);
                            }

                        }
                    },100);
                }else{
                    task.setElapsedTimeInMilliSeconds(0);
                }
            }
            if (waveHelper == null)
                waveHelper = new WaveHelper(taskDetailViewModel, waveView);
            waveView.setWaterLevelRatio((taskDetailViewModel.getElapsedTime() * 1.0f) / taskDetailViewModel.getMinutesInMilliSeconds());
            waveView.setShowWave(true);
            taskDetailViewModel.calculateIsTodayStreakCompleted();

            /*if (taskDetailViewModel.isScreenRotation()) {
                startAnimation();
                keepScreenOn();
                taskDetailViewModel.setProgressMade(true);
                taskDetailViewModel.setScreenRotation(false);
            }*/


        }
    }

    private void taskStatusChanged(@TaskStateAnnotation.TaskState Integer taskStatus) {
        switch (taskStatus) {
            case TASK_PAUSED:
                waveHelper.pauseAnimation();
                removeKeepScreenOnFlag();
                break;
            case TASK_STARTED:
                waveHelper.startAnimation();
                keepScreenOn();
                break;
            case TASK_RESUMED:
                waveHelper.resumeAnimation();
                keepScreenOn();
                break;
        }
    }


    private void initViews(View view) {
        waveView = view.findViewById(R.id.wave);
    }

    private void setupWaveView() {
        waveView.setAmplitudeRatio(0f);
        waveView.setShowWave(true);
        waveView.setWaveColor(Color.parseColor("#44A201"), Color.parseColor("#5AD701"));
        waveView.setBorder(4, Color.parseColor("#5AD701"));
        waveView.setShapeType(WaveView.ShapeType.CIRCLE);
    }

    /**
     * Handle delete and edit action from menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                editTask();
                return true;
            case R.id.action_delete:
                showConfirmationDialogDeleteTask();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void editTask() {
        if(taskDetailViewModel.getTask()==null || taskDetailViewModel.getTask().getValue() == null) return;
        
        Intent i = new Intent(getActivity(), ConfigureTaskActivity.class);
        i.putExtra(TaskDetailFragment.ARG_TASK_ID, taskDetailViewModel.getTask().getValue().getId());
        startActivity(i);
    }


    /**
     * show a dialog to ask confirmation before deleting a task
     */
    private void showConfirmationDialogDeleteTask() {

        AlertDialogHelper.showAlertDialog(getActivity(),
                R.drawable.ic_delete,
                getString(R.string.dialog_delete_title),
                getString(R.string.dialog_delete_message),
                getString(R.string.ok),
                getString(R.string.cancel),
                true,
                () -> {
                    //positive button callback
                    taskDetailViewModel.deleteTask();
                    if (getActivity() instanceof TaskDetailActivity) {
                        //open in one pane
                        getActivity().finish();
                    } else {
                        //open in two pane, update the ui appropriately, currently app doesn't implement two pane mode
                        throw new IllegalStateException("Implement two pane logic");
                    }
                });

    }

    /**
     * If task is running and user hit back button, show dialog for confirmation
     */
    void showConfirmGoBackDialog() {

        AlertDialogHelper.showAlertDialog(getActivity(),
                R.drawable.ic_delete,
                getString(R.string.dialog_quit_title),
                getString(R.string.dialog_quit_message),
                getString(R.string.ok),
                getString(R.string.cancel),
                true,
                () -> {
                    //positive button callback
                    manuallyPauseTask();
                    if (getActivity() != null)
                        getActivity().onBackPressed();
                });
    }

    private void manuallyPauseTask() {
        taskDetailViewModel.pauseTask();
        removeKeepScreenOnFlag();
    }

    private void keepScreenOn() {
        if (getActivity() == null || getActivity().getWindow() == null) return;
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void removeKeepScreenOnFlag() {
        if (getActivity() == null || getActivity().getWindow() == null) return;
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
