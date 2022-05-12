package com.example.eventapp.data.Database;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Event implements Parcelable {
    private String name;
    private String description;
    private Timestamp startDate;
    private Timestamp endDate;
    private String organizer_id;

    public Event() {}

    public Event(String name, String description, Timestamp startDate, Timestamp endDate, String org_id) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        organizer_id = org_id;
    }

    protected Event(Parcel in) {
        name = in.readString();
        description = in.readString();
        organizer_id = in.readString();
        startDate = new Timestamp(new Date(in.readLong()));
        endDate = new Timestamp(new Date(in.readLong()));
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
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Event)) return false;
        Event e = (Event) o;
        return name.equals(e.name)
                && description.equals(e.description)
                && startDate.equals(e.startDate)
                && endDate.equals(e.endDate)
                && organizer_id.equals((e.organizer_id));

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(organizer_id);
        parcel.writeLong(startDate.toDate().getTime());
        parcel.writeLong(endDate.toDate().getTime());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public String getOrganizer_id() {
        return organizer_id;
    }

    public void setOrganizer_id(String organizer_id) {
        this.organizer_id = organizer_id;
    }
}
