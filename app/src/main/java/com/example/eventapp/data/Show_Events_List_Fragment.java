package com.example.eventapp.data;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventapp.R;

import java.util.List;


public class Show_Events_List_Fragment extends Fragment {
    int id;

    Show_Events_List_Fragment(int id)
    {
        this.id = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_show__events__list_, container, false);
        Bundle bd = getArguments();
        RecyclerView eventsListRecView = fragmentView.findViewById(R.id.events_list_Rec_View);

        if (bd.getString("pushed-by").equals("organiser")) {
            OrganiserEventsListAdapter adapter = new OrganiserEventsListAdapter((FragmentActivity) requireActivity());
            DBhelper dbHelp = new DBhelper(fragmentView.getContext());
            List<Event> eventsList = dbHelp.getAllEventsForOrganiser(id);
            adapter.setEventsList(eventsList);

            eventsListRecView.setAdapter(adapter);
        }
        else {
            boolean isRegisteredEventsSwitchActivated = bd.getBoolean("registered-events-switch-status");
            String eventTimeLine = bd.getString("event-timeline");

            StudentEventsListAdapter adapter = new StudentEventsListAdapter((FragmentActivity) requireActivity());
            DBhelper dbHelp = new DBhelper(fragmentView.getContext());
            List<Event> eventsList;

            if (eventTimeLine.equals("past")) {
                if (!isRegisteredEventsSwitchActivated) {
                    System.out.println(isRegisteredEventsSwitchActivated + " " + eventTimeLine);
                    eventsList = dbHelp.getAllPastEventsForStudent(id);
                } else {
                    System.out.println(isRegisteredEventsSwitchActivated + " " + eventTimeLine);
                    eventsList = dbHelp.getRegisteredPastEventsForStudent(id);
                }
            }
            else if (eventTimeLine.equals("ongoing")) {
                if (!isRegisteredEventsSwitchActivated) {
                    System.out.println(isRegisteredEventsSwitchActivated + " " + eventTimeLine);
                    eventsList = dbHelp.getAllOngoingEventsForStudent(id);
                }
                else {
                    System.out.println(isRegisteredEventsSwitchActivated + " " + eventTimeLine);
                    eventsList = dbHelp.getRegisteredOngoingEventsForStudent(id);
                }
            }
            else {
                if (!isRegisteredEventsSwitchActivated) {
                    System.out.println(isRegisteredEventsSwitchActivated + " " + eventTimeLine);
                    eventsList = dbHelp.getAllUpcomingEventsForStudent(id);
                }
                else {
                    System.out.println(isRegisteredEventsSwitchActivated + " " + eventTimeLine);
                    eventsList = dbHelp.getRegisteredUpcomingEventsForStudent(id);
                }
            }
            adapter.setEventsList(eventsList);
            System.out.println(eventsList);

            eventsListRecView.setAdapter(adapter);
        }
        eventsListRecView.setLayoutManager(new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,
                false));
        return fragmentView;
    }
}