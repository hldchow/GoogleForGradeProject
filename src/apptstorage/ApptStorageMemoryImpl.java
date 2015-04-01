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

public class ApptStorageMemoryImpl extends ApptStorage {

	private User defaultUser = null;
	
	//Locations
	Location[] _locations;
	@Override
	public Location[] getLocationList(){
		return _locations;
	}
	
	@Override 
	public void setLocationList(Location[] locations){
		_locations = locations;
	}
	//end for locations handling
	
	public ApptStorageMemoryImpl( User user )
	{
		mAppts=new HashMap<Integer,Appt>();
		defaultUser=user;
		mAssignedApptID=0;
	}
	
	@Override
	public void SaveAppt(Appt appt) {
		// TODO Auto-generated method stub
		appt.setID(mAssignedApptID);
		mAppts.put(mAssignedApptID, appt);
		mAssignedApptID++;
	}

	@Override
	public Appt[] RetrieveAppts(TimeSpan d) {
		// TODO Auto-generated method stub
		ArrayList<Appt> result=new ArrayList<Appt>();
		for(int i=0;i<mAssignedApptID;i++){
			if(mAppts.containsKey(i)){
				
				Appt target=(Appt) mAppts.get(i);
				int freq=target.getFreq();
				TimeSpan tarTime=target.TimeSpan();
				
				if(freq==Appt.ONETIME){
					//Dont need to modify tartime
				}
				else if(freq==Appt.DAILY){
					Timestamp nstime=tarTime.StartTime();
					Timestamp netime=tarTime.EndTime();
					nstime.setYear(d.StartTime().getYear());
					nstime.setMonth(d.StartTime().getMonth());
					nstime.setDate(d.StartTime().getDate());
					netime.setYear(d.StartTime().getYear());
					netime.setMonth(d.StartTime().getMonth());
					netime.setDate(d.StartTime().getDate());
					
					tarTime.StartTime(nstime);
					tarTime.EndTime(netime);
				}
				else if(freq==Appt.WEEKLY){
					Timestamp nstime=tarTime.StartTime();
					Timestamp netime=tarTime.EndTime();
					
					Calendar ref=new GregorianCalendar();
					ref.setTime(nstime);
					int apptWeek=ref.get(Calendar.DAY_OF_WEEK);
					
					ref.setTime(d.StartTime());
					if(apptWeek==ref.get(Calendar.DAY_OF_WEEK)){
						nstime.setYear(d.StartTime().getYear());
						nstime.setMonth(d.StartTime().getMonth());
						nstime.setDate(d.StartTime().getDate());
						netime.setYear(d.StartTime().getYear());
						netime.setMonth(d.StartTime().getMonth());
						netime.setDate(d.StartTime().getDate());
						
						tarTime.StartTime(nstime);
						tarTime.EndTime(netime);
					}
				}
				else if(freq==Appt.MONTHLY){
					Timestamp nstime=tarTime.StartTime();
					Timestamp netime=tarTime.EndTime();
					nstime.setYear(d.StartTime().getYear());
					nstime.setMonth(d.StartTime().getMonth());
					netime.setYear(d.StartTime().getYear());
					netime.setMonth(d.StartTime().getMonth());
					
					tarTime.StartTime(nstime);
					tarTime.EndTime(netime);
					
				}
				if(tarTime.Overlap(d)){
					result.add(target);
				}
			}
		}
		Appt[] resultArr=null;
		if(result!=null){
			resultArr=new Appt[result.size()];
			result.toArray(resultArr);
		}
		return resultArr;
	}

	@Override
	public Appt[] RetrieveAppts(User entity, TimeSpan time) {
		// TODO Auto-generated method stub
		return RetrieveAppts(time);
	}

	@Override
	public Appt RetrieveAppts(int joinApptID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void UpdateAppt(Appt appt) {
		// TODO Auto-generated method stub
		mAppts.put(appt.getID(), appt);
	}

	@Override
	public void RemoveAppt(Appt appt) {
		// TODO Auto-generated method stub
		mAppts.remove(appt.getID());
	}

	@Override
	public User getDefaultUser() {
		// TODO Auto-generated method stub
		return defaultUser;
	}

	@Override
	public void LoadApptFromXml() {
		// TODO Auto-generated method stub
		
	}

}
