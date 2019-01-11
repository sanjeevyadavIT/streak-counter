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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

public class TaskStateAnnotation {

    public static final int TASK_STOPPED = 671; //task hasn't been started yet and is not complete
    public static final int TASK_STARTED = 603; //task has been started and is currently running
    public static final int TASK_PAUSED = 20; //task has been paused
    public static final int TASK_RESUMED = 704; //task was paused earlier and now resumed and running
    public static final int TASK_COMPLETED = 735; //task has been completed for this day

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TASK_STOPPED,TASK_STARTED,TASK_PAUSED,TASK_RESUMED,TASK_COMPLETED})
    public @interface TaskState {
    }
}
