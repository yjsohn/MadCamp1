package com.example.basicapplicationfunction.Temp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicapplicationfunction.R;

import java.util.ArrayList;
import java.util.List;

public class AlarmRecyclerViewAdapter extends RecyclerView.Adapter<AlarmViewHolder> {
    private List<Alarm> alarms;
    private OnToggleAlarmListener listener;
    private List<Alarm> deleteAlarms;
    Boolean buttonShow = false;
    private DeleteMode mCallback;

    public AlarmRecyclerViewAdapter(OnToggleAlarmListener listener, DeleteMode deleteMode) {
        this.alarms = new ArrayList<Alarm>();
        this.listener = listener;
        this.buttonShow = false;
        this.deleteAlarms = new ArrayList<Alarm>();
        this.mCallback = deleteMode;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);
        return new AlarmViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        Alarm alarm = alarms.get(position);
        if(buttonShow==false) {
            holder.alarmDelete.setVisibility(View.GONE);
            holder.alarmDelete.setChecked(false);
        }
        else {
            holder.alarmDelete.setVisibility(View.VISIBLE);
        }
        holder.bind(alarm, listener);
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    @Override
    public void onViewRecycled(@NonNull AlarmViewHolder holder) {
        super.onViewRecycled(holder);
        holder.alarmStarted.setOnCheckedChangeListener(null);
    }

    public void setAlarms(List<Alarm> alarms) {
        this.alarms = alarms;
        notifyDataSetChanged();
    }

    public void setButtonShow(Boolean buttonShow) {
        this.buttonShow = buttonShow;
        if(buttonShow == true)
            mCallback.toDeleteMode();
        else
            mCallback.toOriginal();
    }

    public Boolean getButtonShow() {
        return buttonShow;
    }

    public List<Alarm> getDeleteAlarms() {
        return deleteAlarms;
    }

    public List<Alarm> getAlarms() {
        return alarms;
    }
}