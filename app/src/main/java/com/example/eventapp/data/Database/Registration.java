package com.example.eventapp.data.Database;

public class Registration {
    String studentUsername;
    String eventId;
    Integer attendance;

    public String getStudentUsername() {
        return studentUsername;
    }

    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Integer getAttendance() {
        return attendance;
    }

    public Registration() {}

    @Override
    public String toString() {
        return "Registration{" +
                "studentUsername='" + studentUsername + '\'' +
                ", eventId='" + eventId + '\'' +
                ", Attendance='" + attendance + '\'' +
                '}';
    }

    public Registration(String studentUsername, String eventId) {
        this.studentUsername = studentUsername;
        this.eventId = eventId;
        attendance = 0;
    }
}
