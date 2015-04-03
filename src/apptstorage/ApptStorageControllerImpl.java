package hkust.cse.calendar.apptstorage;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

/* This class is for managing the Appt Storage according to different actions */
public class ApptStorageControllerImpl {

	/* Remove the Appt from the storage */
	public final static int REMOVE = 1;

	/* Modify the Appt the storage */
	public final static int MODIFY = 2;

	/* Add a new Appt into the storage */
	public final static int NEW = 3;
	
	/*
	 * Add additional flags which you feel necessary
	 */
	
	public ArrayList<Location> getLocationList(){
		return mApptStorage.getLocationList();
	}

	public ArrayList<String> getLocationNameList() {
		// TODO Auto-generated method stub
		ArrayList<String> tar=new ArrayList<String>();
		ArrayList<Location> src=getLocationList();
		if(src==null)
			return null;
		for(int i=0;i<src.size();i++){
			tar.add(src.get(i).getName());
		}
		return tar;
	}
	
	public void setLocationList(ArrayList<Location> locations){
		mApptStorage.setLocationList(locations);
	}
	
	/* The Appt storage */
	private ApptStorage mApptStorage;

	/* Create a new object of ApptStorageControllerImpl from an existing storage of Appt */
	public ApptStorageControllerImpl(ApptStorage storage) {
		mApptStorage = storage;
	}

	/* Retrieve the Appt's in the storage for a specific user within the specific time span */
	public Appt[] RetrieveAppts(User entity, TimeSpan time) {
		return mApptStorage.RetrieveAppts(entity, time);
	}

	// overload method to retrieve appointment with the given joint appointment id
	public Appt RetrieveAppts(int joinApptID) {
		return mApptStorage.RetrieveAppts(joinApptID);
	}
	
	/* Manage the Appt in the storage
	 * parameters: the Appt involved, the action to take on the Appt */
	public void ManageAppt(Appt appt, int action) {

		if (action == NEW) {				// Save the Appt into the storage if it is new and non-null
			if (appt == null)
				return;
			mApptStorage.SaveAppt(appt);
		} else if (action == MODIFY) {		// Update the Appt in the storage if it is modified and non-null
			if (appt == null)
				return;
			mApptStorage.UpdateAppt(appt);
		} else if (action == REMOVE) {		// Remove the Appt from the storage if it should be removed
			mApptStorage.RemoveAppt(appt);
		} 
	}

	/* Get the defaultUser of mApptStorage */
	public User getDefaultUser() {
		return mApptStorage.getDefaultUser();
	}
	
	public boolean isTimeConflict(Appt appt) {
		HashMap hm=mApptStorage.mAppts;
		
		if(appt.getFreq()==Appt.ONETIME) {
			Appt[] appts=RetrieveAppts(getDefaultUser(),appt.TimeSpan());
			if(appts.length==0)
				return false;
			else if(appts.length==1&&appts[0].getID()==appt.getID())
				return false;
				
		}
		else if(appt.getFreq()==Appt.DAILY) {
			Object[] keys=hm.keySet().toArray();
			for(int i=0;i<keys.length;i++){
				if(appt.getID()==((Appt) hm.get(keys[i])).getID())
					continue;
				Timestamp st=new Timestamp(((Appt) hm.get(keys[i])).TimeSpan().StartTime().getTime());
				Timestamp et=new Timestamp(((Appt) hm.get(keys[i])).TimeSpan().EndTime().getTime());
				TimeSpan ts=new TimeSpan(st, et);
				ts.StartTime().setYear(appt.TimeSpan().StartTime().getYear());
				ts.StartTime().setMonth(appt.TimeSpan().StartTime().getMonth());
				ts.StartTime().setDate(appt.TimeSpan().StartTime().getDate());
				ts.EndTime().setYear(appt.TimeSpan().EndTime().getYear());
				ts.EndTime().setMonth(appt.TimeSpan().EndTime().getMonth());
				ts.EndTime().setDate(appt.TimeSpan().EndTime().getDate());
				
				if(ts.Overlap(appt.TimeSpan())==true)
					return true;
			}
		}
		else if(appt.getFreq()==Appt.WEEKLY) {
			Object[] keys=hm.keySet().toArray();
			for(int i=0;i<keys.length;i++){
				if(appt.getID()==((Appt) hm.get(keys[i])).getID())
					continue;
				Calendar cal=new GregorianCalendar();
				
				cal.setTime(appt.TimeSpan().StartTime());
				int weekday=cal.get(Calendar.DAY_OF_WEEK);
				
				cal.setTime(((Appt) hm.get(keys[i])).TimeSpan().StartTime());
				int weekday2=cal.get(Calendar.DAY_OF_WEEK);

				if(weekday==weekday2){
					Timestamp st=new Timestamp(((Appt) hm.get(keys[i])).TimeSpan().StartTime().getTime());
					Timestamp et=new Timestamp(((Appt) hm.get(keys[i])).TimeSpan().EndTime().getTime());
					TimeSpan ts=new TimeSpan(st, et);
					ts.StartTime().setYear(appt.TimeSpan().StartTime().getYear());
					ts.StartTime().setMonth(appt.TimeSpan().StartTime().getMonth());
					ts.StartTime().setDate(appt.TimeSpan().StartTime().getDate());
					ts.EndTime().setYear(appt.TimeSpan().EndTime().getYear());
					ts.EndTime().setMonth(appt.TimeSpan().EndTime().getMonth());
					ts.EndTime().setDate(appt.TimeSpan().EndTime().getDate());
					
					if(ts.Overlap(appt.TimeSpan())==true)
						return true;
				}
			}
		}
		else if	(appt.getFreq()==Appt.MONTHLY) {
			Object[] keys=hm.keySet().toArray();
			for(int i=0;i<keys.length;i++){
				if(appt.getID()==((Appt) hm.get(keys[i])).getID())
					continue;

				Timestamp st=new Timestamp(((Appt) hm.get(keys[i])).TimeSpan().StartTime().getTime());
				Timestamp et=new Timestamp(((Appt) hm.get(keys[i])).TimeSpan().EndTime().getTime());
				TimeSpan ts=new TimeSpan(st, et);
				ts.StartTime().setYear(appt.TimeSpan().StartTime().getYear());
				ts.StartTime().setMonth(appt.TimeSpan().StartTime().getMonth());
				ts.EndTime().setYear(appt.TimeSpan().EndTime().getYear());
				ts.EndTime().setMonth(appt.TimeSpan().EndTime().getMonth());
				
				if(ts.Overlap(appt.TimeSpan())==true)
					return true;
			}
		}
			
		return false;
	}

	// method used to load appointment from xml record into hash map
	public void LoadApptFromXml(){
		mApptStorage.LoadApptFromXml();
	}
}
