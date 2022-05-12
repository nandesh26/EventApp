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
import com.example.eventapp.data.Database.Event;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Show_Events_List_Fragment extends Fragment {
    String user_id;
    SearchView searchView;
    List<Event> eventsList;
    StudentEventsListAdapter adapter;
    DBhelper dbHelp;

    FirebaseFirestore fireDb;

    Show_Events_List_Fragment(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fireDb = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_show__events__list_, container, false);
        Bundle bd = getArguments();

        if (bd.getString("pushed-by").equals("organizer")) {
            RecyclerView eventsListRecView = fragmentView.findViewById(R.id.events_list_Rec_View);
            OrganiserEventsListAdapter org_adapter = new OrganiserEventsListAdapter((FragmentActivity) requireActivity());
//            DBhelper orgdbHelp = new DBhelper(fragmentView.getContext());
            updateOrgEventsList(user_id, org_adapter, eventsListRecView);
        } else if (bd.getString("pushed-by").equals("student")) {
            searchView = requireActivity().findViewById(R.id.search);
            RecyclerView eventsListRecView = fragmentView.findViewById(R.id.events_list_Rec_View);
            adapter = new StudentEventsListAdapter((FragmentActivity) requireActivity(), user_id);
//            dbHelp = new DBhelper(fragmentView.getContext());


            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {

                    List<Event> filteredEventsList = new ArrayList<Event>();
                    String event_info;
                    for (Event event : eventsList) {
                        event_info = event.getName().toLowerCase() + " " + event.getDescription().toLowerCase() + " "
                                + event.getStartDate().toDate() + " " + event.getEndDate().toDate();

                        if (event_info.contains(s.toLowerCase())) {
                            filteredEventsList.add(event);
                        }
                    }

                    adapter.setEventsList(filteredEventsList);
                    eventsListRecView.setAdapter(adapter);
                    eventsListRecView.setLayoutManager(new LinearLayoutManager(requireContext(),
                            LinearLayoutManager.VERTICAL,
                            false));
                    return false;
                }
            });


            boolean isRegisteredEventsSwitchActivated = bd.getBoolean("registered-events-switch-status");
            String eventTimeLine = bd.getString("event-timeline");

            if (eventTimeLine.equals("recommended")) {
                if (!isRegisteredEventsSwitchActivated) {
                    System.out.println(isRegisteredEventsSwitchActivated + " " + eventTimeLine);
                    updateStudentEventsList(adapter, eventsListRecView);
                } else {
                    System.out.println(isRegisteredEventsSwitchActivated + " " + eventTimeLine);
                    updateStudentEventsList(adapter, eventsListRecView);
                }
            } else if (eventTimeLine.equals("past")) {
                if (!isRegisteredEventsSwitchActivated) {
                    System.out.println(isRegisteredEventsSwitchActivated + " " + eventTimeLine);
                    updateAllPastStudentEventsList(adapter, eventsListRecView);
                } else {
                    System.out.println(isRegisteredEventsSwitchActivated + " " + eventTimeLine);
                    updateAllPastStudentEventsList(adapter, eventsListRecView);
                }
            } else if (eventTimeLine.equals("ongoing")) {
                if (!isRegisteredEventsSwitchActivated) {
                    System.out.println(isRegisteredEventsSwitchActivated + " " + eventTimeLine);
                    updateAllOngoingStudentEventsList(adapter, eventsListRecView);
                } else {
                    System.out.println(isRegisteredEventsSwitchActivated + " " + eventTimeLine);
                    updateAllOngoingStudentEventsList(adapter, eventsListRecView);
                }
            } else if (eventTimeLine.equals("upcoming")) {
                if (!isRegisteredEventsSwitchActivated) {
                    System.out.println(isRegisteredEventsSwitchActivated + " " + eventTimeLine);
                    updateAllUpcomingStudentEventsList(adapter, eventsListRecView);
                } else {
                    System.out.println(isRegisteredEventsSwitchActivated + " " + eventTimeLine);
                    updateAllUpcomingStudentEventsList(adapter, eventsListRecView);
                }
            }
        }
        return fragmentView;
    }

    private void updateOrgEventsList(String user_id, OrganiserEventsListAdapter org_adapter, RecyclerView eventsListRecView) {
        List<Event> orgEventsList = new ArrayList<>();
        System.out.println(user_id);
        fireDb.collection("Event")
                .whereEqualTo("organizer_id", user_id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
//                System.out.println("task successful");
                        for (QueryDocumentSnapshot doc : task.getResult()) {
//                    System.out.println("in for loop");
                            Event event = doc.toObject(Event.class);
                            orgEventsList.add(event);
                        }
                        eventsList = orgEventsList;
                        org_adapter.setEventsList(orgEventsList);
                        eventsListRecView.setAdapter(org_adapter);
                        eventsListRecView.setLayoutManager(new LinearLayoutManager(requireContext(),
                                LinearLayoutManager.VERTICAL,
                                false));
                    } else {
                        Toast.makeText(requireContext(), "Error getting events", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateStudentEventsList(StudentEventsListAdapter adapter, RecyclerView eventsListRecView) {
        List<Event> res = new ArrayList<>();
        CollectionReference eventsRef = fireDb.collection("Event");
        eventsRef.orderBy("endDate", Query.Direction.DESCENDING).orderBy("startDate")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Event event = doc.toObject(Event.class);
                            res.add(event);
                        }
                        eventsList = res;
                        adapter.setEventsList(res);
                        eventsListRecView.setAdapter(adapter);
                        eventsListRecView.setLayoutManager(new LinearLayoutManager(requireContext(),
                                LinearLayoutManager.VERTICAL,
                                false));
                    } else {
                        Toast.makeText(requireContext(), "Error getting events", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateAllPastStudentEventsList(StudentEventsListAdapter adapter, RecyclerView eventsListRecView) {
        List<Event> res = new ArrayList<>();
        Timestamp now = Timestamp.now();
        CollectionReference eventsRef = fireDb.collection("Event");
        eventsRef.whereLessThan("endDate", now)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Event event = doc.toObject(Event.class);
                            res.add(event);
                        }
                        eventsList = res;
                        adapter.setEventsList(res);
                        eventsListRecView.setAdapter(adapter);
                        eventsListRecView.setLayoutManager(new LinearLayoutManager(requireContext(),
                                LinearLayoutManager.VERTICAL,
                                false));
                    } else {
                        Toast.makeText(requireContext(), "Error getting events", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateAllOngoingStudentEventsList(StudentEventsListAdapter adapter, RecyclerView eventsListRecView) {
        List<Event> resLess = new ArrayList<>();
        List<Event> resMore = new ArrayList<>();
        List<Event> res = new ArrayList<>();
        Timestamp now = Timestamp.now();
        CollectionReference eventsRef = fireDb.collection("Event");
        eventsRef.whereLessThan("startDate", now)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Event event = doc.toObject(Event.class);
                            resLess.add(event);
                        }
                        eventsRef.whereGreaterThan("endDate", now)
                                .get()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        for (QueryDocumentSnapshot doc : task1.getResult()) {
                                            Event event = doc.toObject(Event.class);
                                            resMore.add(event);
                                        }
                                        System.out.println(resMore);
                                        System.out.println(resLess);
                                        for (Event event : resLess) {
                                            if (resMore.contains(event)) {
                                                res.add(event);
                                            }
                                        }
                                        eventsList = res;
                                        adapter.setEventsList(res);
                                        eventsListRecView.setAdapter(adapter);
                                        eventsListRecView.setLayoutManager(new LinearLayoutManager(requireContext(),
                                                LinearLayoutManager.VERTICAL,
                                                false));
                                    } else {
                                        Toast.makeText(requireContext(), "Error getting events", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(requireContext(), "Error getting events", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateAllUpcomingStudentEventsList(StudentEventsListAdapter adapter, RecyclerView eventsListRecView) {
        List<Event> res = new ArrayList<>();
        Timestamp now = Timestamp.now();
        CollectionReference eventsRef = fireDb.collection("Event");
        eventsRef.whereGreaterThan("startDate", now)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Event event = doc.toObject(Event.class);
                            res.add(event);
                        }
                        eventsList = res;
                        adapter.setEventsList(res);
                        eventsListRecView.setAdapter(adapter);
                        eventsListRecView.setLayoutManager(new LinearLayoutManager(requireContext(),
                                LinearLayoutManager.VERTICAL,
                                false));
                    } else {
                        Toast.makeText(requireContext(), "Error getting events", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}