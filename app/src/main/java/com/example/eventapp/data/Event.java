package com.example.eventapp.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable {
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private String organizer_id = "";

    public Event(String name, String description, String startDate, String endDate, String org_id) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        organizer_id = org_id;
    }

    public Event(String name, String description, String startDate, String endDate) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    protected Event(Parcel in) {
        name = in.readString();
        description = in.readString();
        startDate = in.readString();
        endDate = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getOrganizer_id() { return organizer_id; }

    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(startDate);
        parcel.writeString(endDate);
    }
}
