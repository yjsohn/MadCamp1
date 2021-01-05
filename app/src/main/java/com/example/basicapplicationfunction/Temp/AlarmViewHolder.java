package com.example.basicapplicationfunction.Temp;

import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicapplicationfunction.MainActivity;
import com.example.basicapplicationfunction.R;

import java.util.List;

import butterknife.OnClick;

public class AlarmViewHolder extends RecyclerView.ViewHolder {
    private TextView alarmTime;
    private ImageView alarmRecurring;
    private TextView alarmRecurringDays;
    private TextView alarmTitle;

    CheckBox alarmDelete;
    Switch alarmStarted;


    public AlarmViewHolder(@NonNull View itemView) {
        super(itemView);

        alarmTime = itemView.findViewById(R.id.item_alarm_time);
        alarmStarted = itemView.findViewById(R.id.item_alarm_started);
        alarmRecurring = itemView.findViewById(R.id.item_alarm_recurring);
        alarmRecurringDays = itemView.findViewById(R.id.item_alarm_recurringDays);
        alarmTitle = itemView.findViewById(R.id.item_alarm_title);
        alarmDelete = itemView.findViewById(R.id.item_alarm_delete);

        itemView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                int pos = getAdapterPosition();
                AlarmRecyclerViewAdapter alarmRecyclerViewAdapter = (AlarmRecyclerViewAdapter) AlarmsListFragment.getAlarmRecyclerViewAdapter();

                if(pos != RecyclerView.NO_POSITION && alarmRecyclerViewAdapter.getButtonShow()==false) {
                    alarmRecyclerViewAdapter.setButtonShow(true);
                    alarmRecyclerViewAdapter.notifyDataSetChanged();
                    alarmDelete.setChecked(true);
                    AlarmsListFragment.deleteAlarms.setVisibility(View.VISIBLE);

                    List<Alarm> alarms = alarmRecyclerViewAdapter.getAlarms();
                    List<Alarm> deleteAlarms = alarmRecyclerViewAdapter.getDeleteAlarms();
                    Alarm alarm = alarms.get(pos);

                    deleteAlarms.add(alarm);


                    /*
                    List<Alarm> alarms = alarmRecyclerViewAdapter.getAlarms();
                    Alarm alarm = alarms.get(pos);

                    if (alarm.isStarted())
                        alarm.cancelAlarm(v.getContext());

                    AlarmsListFragment.getAlarmsListViewModel().delete(alarm);
                    alarms.remove(pos);
                    alarmRecyclerViewAdapter.notifyItemChanged(pos);*/

                    return true;
                }
                return true;
            }
        });

        itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlarmRecyclerViewAdapter alarmRecyclerViewAdapter = (AlarmRecyclerViewAdapter) AlarmsListFragment.getAlarmRecyclerViewAdapter();
                if(alarmRecyclerViewAdapter.getButtonShow()==true){
                    int pos = getAdapterPosition();

                    List<Alarm> alarms = alarmRecyclerViewAdapter.getAlarms();
                    List<Alarm> deleteAlarms = alarmRecyclerViewAdapter.getDeleteAlarms();
                    Alarm alarm = alarms.get(pos);

                    if(deleteAlarms.contains(alarm)) {
                        alarmDelete.setChecked(false);
                        deleteAlarms.remove(alarm);
                    }
                    else {
                        alarmDelete.setChecked(true);
                        deleteAlarms.add(alarm);
                    }
                }
                else{

                }
            }
        });

        alarmDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlarmRecyclerViewAdapter alarmRecyclerViewAdapter = (AlarmRecyclerViewAdapter) AlarmsListFragment.getAlarmRecyclerViewAdapter();
                if(alarmRecyclerViewAdapter.getButtonShow()==true){
                    int pos = getAdapterPosition();

                    List<Alarm> alarms = alarmRecyclerViewAdapter.getAlarms();
                    List<Alarm> deleteAlarms = alarmRecyclerViewAdapter.getDeleteAlarms();
                    Alarm alarm = alarms.get(pos);

                    if(deleteAlarms.contains(alarm)) {
                        alarmDelete.setChecked(false);
                        deleteAlarms.remove(alarm);
                    }
                    else {
                        alarmDelete.setChecked(true);
                        deleteAlarms.add(alarm);
                    }
                }
            }
        });

    }

    public void bind(Alarm alarm, OnToggleAlarmListener listener) {
        String alarmText = String.format("%02d:%02d", alarm.getHour(), alarm.getMinute());

        alarmTime.setText(alarmText);
        alarmStarted.setChecked(alarm.isStarted());

        if (alarm.isRecurring()) {
            alarmRecurring.setImageResource(R.drawable.ic_repeat_black_24dp);
            alarmRecurringDays.setText(alarm.getRecurringDaysText());
        } else {
            alarmRecurring.setImageResource(R.drawable.ic_looks_one_black_24dp);
            alarmRecurringDays.setText("Once Off");
        }

        if (alarm.getTitle().length() != 0) {
            alarmTitle.setText(alarm.getTitle());
        } else {
            alarmTitle.setText("My alarm");
        }

        alarmStarted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listener.onToggle(alarm);
            }
        });

        /*alarmDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = getAdapterPosition();
                if(pos != RecyclerView.NO_POSITION) {
                    AlarmRecyclerViewAdapter alarmRecyclerViewAdapter = (AlarmRecyclerViewAdapter) AlarmsListFragment.getAlarmRecyclerViewAdapter();
                    List<Alarm> alarms = alarmRecyclerViewAdapter.getAlarms();
                    Alarm alarm = alarms.get(pos);

                    AlarmsListFragment.getAlarmsListViewModel().delete(alarm);

                    if (alarm.isStarted())
                        alarm.cancelAlarm(v.getContext());

                    //alarms.remove(pos);
                    List<Alarm> deleteAlarms = alarmRecyclerViewAdapter.getDeleteAlarms();
                    deleteAlarms.remove(alarm);
                    alarmRecyclerViewAdapter.notifyItemChanged(pos);
                    alarmDelete.setVisibility(v.GONE);
                    return;
                }
                return;
            }
        });*/


    }
}