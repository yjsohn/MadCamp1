package com.example.basicapplicationfunction.Temp;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicapplicationfunction.Gallery.Gallery;
import com.example.basicapplicationfunction.MainActivity;
import com.example.basicapplicationfunction.R;

import java.util.List;

public class AlarmsListFragment extends Fragment implements OnToggleAlarmListener, DeleteMode {

    public static AlarmRecyclerViewAdapter alarmRecyclerViewAdapter;
    public static AlarmsListViewModel alarmsListViewModel;
    private RecyclerView alarmsRecyclerView;
    private ImageButton addAlarm;
    private FragmentActivity myContext;
    public static Button deleteAlarms;
    public static NavController navController = null;

    @Override
    public void toDeleteMode(){
        //this.getView().setBackgroundColor(Color.parseColor("#FFA500"));
    }

    @Override
    public void toOriginal(){
        //this.getView().setBackgroundColor(Color.parseColor("#E4E8F0"));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        alarmRecyclerViewAdapter = new AlarmRecyclerViewAdapter(this, this);
        alarmsListViewModel = new ViewModelProvider(this).get(AlarmsListViewModel.class);
        alarmsListViewModel.getAlarmsLiveData().observe(this, new Observer<List<Alarm>>() {
            @Override
            public void onChanged(List<Alarm> alarms) {
                if (alarms != null) {
                    alarmRecyclerViewAdapter.setAlarms(alarms);
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listalarms, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity activity = (MainActivity)getActivity();
        alarmsRecyclerView = view.findViewById(R.id.fragment_listalarms_recylerView);
        alarmsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        alarmsRecyclerView.setAdapter(alarmRecyclerViewAdapter);

        addAlarm = view.findViewById(R.id.fragment_listalarms_addAlarm);
        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController = Navigation.findNavController(v);
                Navigation.findNavController(v).navigate(R.id.action_alarmsListFragment_to_createAlarmFragment);
                //NavHostFragment finalHost = NavHostFragment.create(R.navigation.nav_graph);
                //finalHost.getNavController().navigate(R.id.action_alarmsListFragment_to_createAlarmFragment);
                /*NavHostFragment navHostFragment = (NavHostFragment) myContext.getSupportFragmentManager()
                        .findFragmentById(R.id.nav_graph);
                navHostFragment.getNavController();*/
            }
        });
        deleteAlarms = view.findViewById(R.id.all_alarm_delete);
        deleteAlarms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmRecyclerViewAdapter.getDeleteAlarms().forEach(alarm -> {
                    AlarmsListFragment.getAlarmsListViewModel().delete(alarm);

                    if (alarm.isStarted())
                        alarm.cancelAlarm(v.getContext());
                });
                alarmRecyclerViewAdapter.getDeleteAlarms().clear();
                alarmRecyclerViewAdapter.setButtonShow(false);
                deleteAlarms.setVisibility(View.GONE);
                alarmRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }

    public static AlarmsListViewModel getAlarmsListViewModel() {
        return alarmsListViewModel;
    }

    public static AlarmRecyclerViewAdapter getAlarmRecyclerViewAdapter() {
        return alarmRecyclerViewAdapter;
    }

    public static AlarmsListFragment newInstance() {
        AlarmsListFragment alarmsListFragment = new AlarmsListFragment();
        return alarmsListFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onToggle(Alarm alarm) {
        if (alarm.isStarted()) {
            alarm.cancelAlarm(getContext());
            alarmsListViewModel.update(alarm);
        } else {
            alarm.schedule(getContext());
            alarmsListViewModel.update(alarm);
        }
    }

     public void hideButton(){
         /*Rect viewRect = new Rect();
         mTooltip.getGlobalVisibleRect(viewRect);
         if (!viewRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
             setVisibility(View.GONE);
         }*/

         Log.d("touch", "touch");
     }

    public static NavController getNavController() {
        return navController;
    }

    public static void setNavController(NavController navController) {
        AlarmsListFragment.navController = navController;
    }
}