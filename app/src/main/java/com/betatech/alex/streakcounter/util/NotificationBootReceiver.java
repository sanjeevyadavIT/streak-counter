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
package com.betatech.alex.streakcounter.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.betatech.alex.streakcounter.db.TasksRepository;
import com.betatech.alex.streakcounter.db.entity.TaskEntity;

import java.util.List;

public class NotificationBootReceiver extends BroadcastReceiver {

    private final String BOOT_ACTION_INTENT = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (BOOT_ACTION_INTENT.equals(intent.getAction())) {
            TasksRepository repository = TasksRepository.getInstance(context);

            List<TaskEntity> tasks = repository.getTasks().getValue();

            if(tasks == null) return;

            for (TaskEntity task : tasks) {
                if(task.isShowNotification()){
                    NotificationUtils.scheduleAlarmToTriggerNotification(context,task);
                }
            }
        }
    }
}
