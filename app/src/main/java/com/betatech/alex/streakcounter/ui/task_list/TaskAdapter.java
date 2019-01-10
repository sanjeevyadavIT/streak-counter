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
package com.betatech.alex.streakcounter.ui.task_list;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.betatech.alex.streakcounter.R;
import com.betatech.alex.streakcounter.db.entity.TaskEntity;
import com.betatech.alex.streakcounter.model.Task;
import com.betatech.alex.streakcounter.util.DateUtils;
import com.betatech.alex.streakcounter.ui.detail_task.TaskDetailActivity;
import com.betatech.alex.streakcounter.ui.detail_task.TaskDetailFragment;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private final TaskListActivity mParentActivity;
    private List<TaskEntity> mValues;
    private int[] backgrounds;
    private final View.OnClickListener mOnClickListener = view -> {
        Task item = (TaskEntity) view.getTag();

        Context context = view.getContext();
        Intent intent = new Intent(context, TaskDetailActivity.class);
        intent.putExtra(TaskDetailFragment.ARG_TASK_ID, item.getId());

        context.startActivity(intent);

    };

    public TaskAdapter(TaskListActivity parent,
                       List<TaskEntity> items) {
        mValues = items;
        mParentActivity = parent;
        backgrounds = new int[]{R.drawable.gradient_1, R.drawable.gradient_2, R.drawable.gradient_3, R.drawable.gradient_4, R.drawable.gradient_5, R.drawable.gradient_6, R.drawable.gradient_7, R.drawable.gradient_8, R.drawable.gradient_9, R.drawable.gradient_10, R.drawable.gradient_11};
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //setting gradient background
        holder.streakIcon.setBackgroundResource(R.drawable.ic_flame_disable);
        if (mValues.get(position).getCurrentStreak() == 0) {
            holder.cardBackground.setBackgroundResource(R.drawable.gradient_disabled);
        } else {
            holder.cardBackground.setBackgroundResource(backgrounds[position % backgrounds.length]);
            if (DateUtils.subtractDates(DateUtils.getCurrentDateWithoutTime(), mValues.get(position).getLastDate()) == 0)
                holder.streakIcon.setBackgroundResource(R.drawable.ic_flame);
        }

        holder.mStreakTextView.setText(holder.mStreakTextView.getContext().getString(R.string.current_streak_text, mValues.get(position).getCurrentStreak()));
        String displayText; //formatted string which will be displayed on card
        if (mValues.get(position).getMinutes() == 0) {
            displayText = holder.mTitleTextView.getContext().getString(R.string.task_string_format_without_time, mValues.get(position).getTitle(), mValues.get(position).getTimeStr(), mValues.get(position).getLocation());
        } else {
            String min = holder.mTitleTextView.getContext().getResources().getQuantityString(R.plurals.numberOfMinute, mValues.get(position).getMinutes(), mValues.get(position).getMinutes());
            displayText = holder.mTitleTextView.getContext().getString(R.string.task_string_format_with_time, mValues.get(position).getTitle(), min, mValues.get(position).getTimeStr(), mValues.get(position).getLocation());
        }

        holder.mTitleTextView.setText(displayText);
        holder.soundImageButton.setTag(displayText); //set the same displayText as tag, will be used in textToSpeech conversion
        holder.itemView.setTag(mValues.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mValues != null ? mValues.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView mTitleTextView;
        final TextView mStreakTextView;
        final ImageView streakIcon;
        final ImageView cardBackground;
        final ImageButton soundImageButton;
        //final Button createNotificationButton;

        ViewHolder(View view) {
            super(view);
            mTitleTextView = view.findViewById(R.id.tv_title);
            mStreakTextView = view.findViewById(R.id.tv_streak);
            streakIcon = view.findViewById(R.id.image_streak_icon);
            cardBackground = view.findViewById(R.id.card_background);
            soundImageButton = view.findViewById(R.id.ib_sound);
            soundImageButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mParentActivity.convertTextToSpeech((String) soundImageButton.getTag());
        }
    }

    /**
     * replace current taskList with new list
     * <p>
     * TODO : Find effective way to replace content of recyclerview , Use DiffUtil
     */
    public void setData(List<TaskEntity> taskList) {
        if (taskList == null) return;

        mValues.clear();
        mValues.addAll(taskList);
        this.notifyDataSetChanged();
    }
}