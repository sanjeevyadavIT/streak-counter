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
