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

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import com.gelitenight.waveview.library.WaveView;

import java.util.ArrayList;
import java.util.List;

// FIXME: 10/29/2018 Will create problem at the time of screen rotation
public class WaveHelper {
    private static final int DEFAULT_ANIMATION_DURATION = 1000;

    private WaveView mWaveView;

    private AnimatorSet mAnimatorSet;
    private ObjectAnimator waterLevelAnim;

    private TaskDetailViewModel viewModel;

    private Handler hanlder;
    private Runnable runnable;


    public WaveHelper(TaskDetailViewModel viewModel, WaveView waveView) {
        this.viewModel = viewModel;
        mWaveView = waveView;
        initAnimation();
        hanlder = new Handler();
        runnable = viewModel::taskCompleted;
    }

    private void initAnimation() {
        List<Animator> animators = new ArrayList<>();

        // horizontal animation.
        // wave waves infinitely.
        ObjectAnimator waveShiftAnim = ObjectAnimator.ofFloat(
                mWaveView, "waveShiftRatio", 0f, 1f);
        waveShiftAnim.setRepeatCount(ValueAnimator.INFINITE);
        waveShiftAnim.setDuration(1000);
        waveShiftAnim.setInterpolator(new LinearInterpolator());
        animators.add(waveShiftAnim);

        // vertical animation.
        // water level increases from 0 to center of WaveView
        float waterLevel = (viewModel.getElapsedTime() * 1.0f) / viewModel.getMinutesInMilliSeconds();
        // FIXME: 11/23/2018 Will create problem if half of the work is done today and the app is openes on other day
        // FIXME: 11/23/2018 if time duration become negative
        long timeDuration = viewModel.getMinutesInMilliSeconds() != 0 ? viewModel.getMinutesInMilliSeconds() - viewModel.getElapsedTime() : DEFAULT_ANIMATION_DURATION;
        waterLevelAnim = ObjectAnimator.ofFloat(
                mWaveView, "waterLevelRatio", waterLevel, 1f);
        waterLevelAnim.setDuration(timeDuration);
        waterLevelAnim.setInterpolator(new LinearInterpolator());
        animators.add(waterLevelAnim);

        // amplitude animation.
        // wave grows big then grows small, repeatedly
        ObjectAnimator amplitudeAnim = ObjectAnimator.ofFloat(
                mWaveView, "amplitudeRatio", 0.0001f, 0.05f);
        amplitudeAnim.setRepeatCount(ValueAnimator.INFINITE);
        amplitudeAnim.setRepeatMode(ValueAnimator.REVERSE);
        amplitudeAnim.setDuration(5000);
        amplitudeAnim.setInterpolator(new LinearInterpolator());
        animators.add(amplitudeAnim);

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(animators);
    }

    public void startAnimation() {
        Log.d("STREAKY",""+mAnimatorSet.getDuration());
        if (mAnimatorSet != null) {
            mAnimatorSet.start();
            startHandler();
        }
    }

    public void pauseAnimation() {
        stopHandler();
        Log.d("STREAKY",""+mAnimatorSet.getDuration());
        if (mAnimatorSet != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mAnimatorSet.pause();
            } else {
                mAnimatorSet.cancel();
            }
        }
    }

    public void resumeAnimation() {
        Log.d("STREAKY",""+mAnimatorSet.getDuration());
        if (mAnimatorSet != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mAnimatorSet.resume();
            } else {
                if (viewModel.getElapsedTime() != 0 && viewModel.getElapsedTime()  <= viewModel.getMinutesInMilliSeconds()) {
                    setWaterLevelHeight((viewModel.getElapsedTime()  * 1.0f) / viewModel.getMinutesInMilliSeconds());
                } else {
                    setWaterLevelHeight(0);
                }
                mAnimatorSet.start();
            }
            startHandler();
        }
    }

    public void endAnimation() {
        if (mAnimatorSet != null) {
            mAnimatorSet.end();
        }
    }

    /**
     * Save current task running status
     */
    private void startHandler() {
        hanlder.postDelayed(runnable, viewModel.getMinutesInMilliSeconds() - viewModel.getElapsedTime());
    }

    /**
     * task has been stopped
     */
    private void stopHandler() {
        hanlder.removeCallbacks(runnable);
    }


    private void setWaterLevelHeight(float waterLevelHeight) {
        waterLevelAnim.setFloatValues(waterLevelHeight, 1f);
    }
}

