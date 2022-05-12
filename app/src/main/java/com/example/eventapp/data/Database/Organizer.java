package com.example.eventapp.data.Database;

import android.os.Parcel;
import android.os.Parcelable;

public class Organizer implements Parcelable {
    private String username;
    private String name;
    private String password;
    private String email;

    public Organizer() {}

    public Organizer(String name, String username, String password, String email) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    protected Organizer(Parcel in) {
        username = in.readString();
        name = in.readString();
        password = in.readString();
        email = in.readString();
    }

    public static final Creator<Organizer> CREATOR = new Creator<Organizer>() {
        @Override
        public Organizer createFromParcel(Parcel in) {
            return new Organizer(in);
        }

        @Override
        public Organizer[] newArray(int size) {
            return new Organizer[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(name);
        parcel.writeString(password);
        parcel.writeString(email);
    }
}
