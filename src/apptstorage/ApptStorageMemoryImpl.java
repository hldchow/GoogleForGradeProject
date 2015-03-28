package hkust.cse.calendar.apptstorage;

import java.util.ArrayList;
import java.util.HashMap;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

public class ApptStorageMemoryImpl extends ApptStorage {

	private User defaultUser = null;
	
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
				if(d.Overlap(((Appt) mAppts.get(i)).TimeSpan())){
					result.add((Appt) mAppts.get(i));
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
		
	}

	@Override
	public void RemoveAppt(Appt appt) {
		// TODO Auto-generated method stub
		mAppts.remove(appt.getID());
	}

	@Override
	public User getDefaultUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void LoadApptFromXml() {
		// TODO Auto-generated method stub
		
	}

}
