package com.heroku.java.bean;

import java.sql.Date;
import java.sql.Time;


public class Appointment {
    public String appt_name;
    public String custemail;
    public String appt_phone;
    public String appt_address;
    public Date date;
    public String day;
    public Time time;
    public String message;

    public Appointment() {
    }


    public Appointment(String appt_name, String custemail, String appt_phone, String appt_address, Date date, String day, Time time, String message) {
        this.appt_name = appt_name;
        this.custemail = custemail;
        this.appt_phone = appt_phone;
        this.appt_address = appt_address;
        this.date = date;
        this.day = day;
        this.time = time;
        this.message = message;
    }


    public String getAppt_name() {
        return this.appt_name;
    }

    public void setAppt_name(String appt_name) {
        this.appt_name = appt_name;
    }

    public String getCustemail() {
        return this.custemail;
    }

    public void setCustemail(String custemail) {
        this.custemail = custemail;
    }

    public String getAppt_phone() {
        return this.appt_phone;
    }

    public void setAppt_phone(String appt_phone) {
        this.appt_phone = appt_phone;
    }

    public String getAppt_address() {
        return this.appt_address;
    }

    public void setAppt_address(String appt_address) {
        this.appt_address = appt_address;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDay() {
        return this.day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Time getTime() {
        return this.time;
    }

    public void setTime(Time time) {
        this.time = time;
    }


    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
