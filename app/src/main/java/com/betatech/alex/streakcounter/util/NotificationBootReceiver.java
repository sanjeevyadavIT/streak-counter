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
