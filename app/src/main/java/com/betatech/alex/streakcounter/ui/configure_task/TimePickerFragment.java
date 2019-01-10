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

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.betatech.alex.streakcounter.R;
import com.betatech.alex.streakcounter.util.DateUtils;

import java.util.Calendar;
import java.util.Date;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

/**
 * TimePickerFragment to allow user to set time at which they want to do the task,
 * also a notification will be shown
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private ConfigureTaskViewModel configureTaskVM;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();

        if (getActivity() != null && getActivity() instanceof ConfigureTaskActivity) {
            configureTaskVM = ViewModelProviders.of(getActivity()).get(ConfigureTaskViewModel.class);
            setInitialTime(c);
        } else {
            //problem, won't be able to set value to viewmodel
            throw new IllegalStateException();
        }

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(getActivity()));
    }

    private void setInitialTime(Calendar c) {
        if (!getString(R.string.time_tag).equals(configureTaskVM.getTime().getValue())) {
            Date date = configureTaskVM.getTask().getValue().getTime();
            c.setTime(date);
        }
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        int hour = hourOfDay;
        int minutes = minute;
        String timeSet = "";
        if (hour > 12) {
            hour -= 12;
            timeSet = "PM";
        } else if (hour == 0) {
            hour += 12;
            timeSet = "AM";
        } else if (hour == 12) {
            timeSet = "PM";
        } else {
            timeSet = "AM";
        }
        // TODO: 10/20/2018 This code could be improved, use DateUtils function
        //set this to reflect changes back in xml button
        configureTaskVM.setTime(new StringBuilder().append(hour).append(":").append(minutes < 10 ? "0" + minutes : minutes).append(" ").append(timeSet.toLowerCase()).toString());
        configureTaskVM.getTask().getValue().setTime(DateUtils.getFormattedTime(hourOfDay, minute, 0));
    }
}
