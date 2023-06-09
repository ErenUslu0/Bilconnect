
package com.example.myapplication;

import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class Event implements Comparable<Event>{
    protected String title;
    protected String description;
    protected String location;
    protected String date;
    protected int userCount;
    protected String usersIdList;
    protected int quota;
    protected String hostId;
    protected String Time;
    protected boolean active;

    protected String eventId;

    protected String campus;

    public Event(String title,int quota,String desc,String loc, String date, String time,String hostId,String campus, String eventId)
    {
        this.title = title;
        this.description = desc;
        this.location = loc;
        this.date = date;
        this.userCount = 1;
        this.quota = quota;
        this.Time = time;
        this.campus = campus;
        this.usersIdList = hostId + ",";
        this.hostId = hostId;
        this.active = true;
        this.eventId = eventId;
    }

    public Event(){

    }

    public void removeUser(String uid)
    {
        if(usersIdList.contains(uid)) {
            usersIdList = usersIdList.replace((uid + ","), "");
            userCount--;
        }
    }

    public void addUser(String uid)
    {
        if(userCount<quota && !usersIdList.contains(uid))
        {
            usersIdList = usersIdList + uid + ",";
            userCount++;
        }
    }

    public boolean isFinished()
    {
        Calendar calendar = Calendar.getInstance();
        String dateFormat = "dd.MM.yyyy";  // Define the desired date format
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
        String currentDate = simpleDateFormat.format(calendar.getTime());

        int currentDay = Integer.valueOf(currentDate.substring(0,2));
        int currentMonth = Integer.valueOf(currentDate.substring(3,5));
        int currentYear = Integer.valueOf(currentDate.substring(6,10));

        int eventDay = Integer.valueOf(date.substring(0,2));
        int eventMonth = Integer.valueOf(date.substring(3,5));
        int eventYear = Integer.valueOf(date.substring(6,10));

        SimpleDateFormat sdf = new SimpleDateFormat("mm/hh");
        String currentTime = sdf.format(new Date());

        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        int eventHour = Integer.valueOf(Time.substring(0,2));
        int eventMinute = Integer.valueOf(Time.substring(3,5));

        if (currentYear > eventYear) {
            return true;  // Event has already occurred in a previous year
        } else if (currentYear == eventYear && currentMonth > eventMonth) {
            return true;  // Event has already occurred in the same year but a previous month
        } else if (currentYear == eventYear && currentMonth == eventMonth && currentDay > eventDay) {
            return true;  // Event has already occurred in the same year and month but a previous day
        } else if (currentYear == eventYear && currentMonth == eventMonth && currentDay == eventDay && currentHour > eventHour) {
            return true;  // Event has already occurred on the same day but a previous hour
        } else if (currentYear == eventYear && currentMonth == eventMonth && currentDay == eventDay && currentHour == eventHour && currentMinute >= eventMinute) {
            return true;  // Event has already occurred at the exact same time or passed
        } else {
            return false;  // Event is still in the future
        }
    }

    public boolean isFull() {
        if(userCount>=quota)
        {
            this.active = false;
            return true;
        }
        return false;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return Time;
    }

    public String getHostId() {
        return hostId;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getCampus() {
        return campus;
    }

    public int getQuota() {
        return quota;
    }

    public void setUsersIdList(String usersIdList) {
        this.usersIdList = usersIdList;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public void setTime(String time) {
        Time = time;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public void setHostId(String hostUser) {
        this.hostId = hostUser;
    }


    public String getEventId() {
        return eventId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public int getUserCount() {return userCount;}

    public String getUsersIdList() {return usersIdList;}

    public String getHostUser() {
        return hostId;
    }


    @Override
    public int compareTo(Event o) {
        int eventDay = Integer.valueOf(date.substring(0,2));
        int eventMonth = Integer.valueOf(date.substring(3,5));
        int eventYear = Integer.valueOf(date.substring(6,10));

        int eventHour = Integer.valueOf(Time.substring(0,2));
        int eventMinute = Integer.valueOf(Time.substring(3,5));

        int otherDay = Integer.valueOf(o.getDate().substring(0,2));
        int otherMonth = Integer.valueOf(o.getDate().substring(3,5));
        int otherYear = Integer.valueOf(o.getDate().substring(6,10));

        int otherHour = Integer.valueOf(o.getTime().substring(0,2));
        int otherMinute = Integer.valueOf(o.getTime().substring(3,5));

        if (otherYear > eventYear) {
            return -1;  // Event has already occurred in a previous year
        } else if (otherYear == eventYear && otherMonth > eventMonth) {
            return -1;  // Event has already occurred in the same year but a previous month
        } else if (otherYear == eventYear && otherMonth == eventMonth && otherDay > eventDay) {
            return -1;  // Event has already occurred in the same year and month but a previous day
        } else if (otherYear == eventYear && otherMonth == eventMonth && otherDay == eventDay && otherHour > eventHour) {
            return -1;  // Event has already occurred on the same day but a previous hour
        } else if (otherYear == eventYear && otherMonth == eventMonth && otherDay == eventDay && otherHour == eventHour && otherMinute >= eventMinute) {
            return -1;  // Event has already occurred at the exact same time or passed
        } else {
            return 1;  // Event is still in the future
        }
    }
}

