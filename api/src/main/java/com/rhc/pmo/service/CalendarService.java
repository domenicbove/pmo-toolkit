package com.rhc.pmo.service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;

public class CalendarService {

	private Calendar calendar;
	
	public CalendarService(GoogleCredential credential) {
		setCalendar(new Calendar.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
				.setApplicationName("PMO Toolkit").build());
	}
	
	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}
	
	public void createEvent(String location, String description, /*DateTime startTime, DateTime endTime,*/ List<String> emails) throws IOException {
		
		  //Create a new event on fb 
        Event event = new Event()
        	.setSummary("Internal Kick Off Meeting")
        	.setLocation(location)
        	.setDescription(description);

		DateTime startDateTime = new DateTime("2016-12-28T09:00:00-07:00");
		EventDateTime start = new EventDateTime()
		    .setDateTime(startDateTime)
		    .setTimeZone("America/Los_Angeles");
		event.setStart(start);
		
		DateTime endDateTime = new DateTime("2016-12-28T17:00:00-07:00");
		EventDateTime end = new EventDateTime()
		    .setDateTime(endDateTime)
		    .setTimeZone("America/Los_Angeles");
		event.setEnd(end);
		

		ArrayList<EventAttendee> attendees = new ArrayList<EventAttendee>();
		for (String email : emails) {
			attendees.add(new EventAttendee().setEmail(email));
		}		
		event.setAttendees(attendees);

		
		//custom make reminders 
		EventReminder[] reminderOverrides = new EventReminder[] {
		    new EventReminder().setMethod("email").setMinutes(24 * 60),
		    new EventReminder().setMethod("popup").setMinutes(10),
		};
		
		//override the default reminders to your custom reminders
		Event.Reminders reminders = new Event.Reminders()
		    .setUseDefault(false) 
		    .setOverrides(Arrays.asList(reminderOverrides));
		event.setReminders(reminders);
		
		//create calendar and insert event
		String calendarId = "primary";
		event = calendar.events().insert(calendarId, event).execute();
		System.out.printf("Event created: %s\n", event.getHtmlLink());
		
		
		
	}
	
	
	
}

