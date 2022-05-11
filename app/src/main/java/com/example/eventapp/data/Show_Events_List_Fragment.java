package com.example.eventapp.data;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.eventapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Show_Events_List_Fragment extends Fragment {
    int id;
    SearchView searchView;
    List<Event> eventsList;
    StudentEventsListAdapter adapter;
    DBhelper dbHelp;

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

        if (!bd.getString("pushed-by").equals("organiser")) {
            searchView = getActivity().findViewById(R.id.search);
            adapter = new StudentEventsListAdapter((FragmentActivity) requireActivity());
            dbHelp = new DBhelper(fragmentView.getContext());


            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {

                    List<Event> filteredEventsList = new ArrayList<Event>();
                    String event_info;
                    for(Event event:eventsList) {
                        event_info = event.getName().toString().toLowerCase() + " " + event.getDescription().toString().toLowerCase() + " "
                                + event.getStartDate().toString() + " " + event.getEndDate();

                        if (event_info.contains(s.toLowerCase())) {
                            filteredEventsList.add(event);
                        }
                    }

                    adapter.setEventsList(filteredEventsList);
                    eventsListRecView.setAdapter(adapter);
                    return false;
                }
            });
        }


        if (bd.getString("pushed-by").equals("organiser")) {
            OrganiserEventsListAdapter org_adapter = new OrganiserEventsListAdapter((FragmentActivity) requireActivity());
            DBhelper orgdbHelp = new DBhelper(fragmentView.getContext());
            List<Event> OrgeventsList = orgdbHelp.getAllEventsForOrganiser(id);
            org_adapter.setEventsList(OrgeventsList);

            eventsListRecView.setAdapter(org_adapter);
        }
        else {
            boolean isRegisteredEventsSwitchActivated = bd.getBoolean("registered-events-switch-status");
            String eventTimeLine = bd.getString("event-timeline");

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