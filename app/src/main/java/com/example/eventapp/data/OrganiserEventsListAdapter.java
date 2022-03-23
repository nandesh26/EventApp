package com.example.eventapp.data;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventapp.R;

import java.util.ArrayList;
import java.util.List;


public class OrganiserEventsListAdapter extends RecyclerView.Adapter<OrganiserEventsListAdapter.ViewHolder> {

    private List<Event> eventsList = new ArrayList<>();
    private final FragmentActivity activity;

    public OrganiserEventsListAdapter(FragmentActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.organiser_event_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.eventId.setText(String.valueOf(position + 1));
        holder.eventName.setText(eventsList.get(position).getName());
        holder.eventDescription.setText(eventsList.get(position).getDescription());
        holder.eventStartDate.setText(eventsList.get(position).getStartDate());
        holder.eventEndDate.setText(eventsList.get(position).getEndDate());

//        Bundle bundle = new Bundle();
//        bundle.putInt("newsIndex", position);
//        bundle.putParcelable("newsObj", newsList.get(position));
//        holder.parent.setOnClickListener(view -> {
//            FragmentManager fm = activity.getSupportFragmentManager();
//            fm.beginTransaction()
//                    .replace(R.id.newsListFragment, NewsFragment.class, bundle)
//                    .setReorderingAllowed(true)
//                    .addToBackStack("news")
//                    .commit();
//        });
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public void setEventsList(List<Event> eventsList) {
        this.eventsList = eventsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView eventId;
        private final TextView eventName;
        private final TextView eventDescription;
        private final TextView eventStartDate;
        private final TextView eventEndDate;
        private final LinearLayout parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.organiser_event_parent);
            eventId =itemView.findViewById(R.id.organiser_event_id);
            eventName = itemView.findViewById(R.id.organiser_event_name);
            eventDescription = itemView.findViewById(R.id.organiser_event_description);
            eventStartDate = itemView.findViewById(R.id.organiser_event_start_date);
            eventEndDate = itemView.findViewById(R.id.organiser_event_end_date);
        }
    }
}

