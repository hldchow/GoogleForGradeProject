package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.gui.Utility;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.TimeSpan;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;


public class AppScheduler extends JDialog implements ActionListener,
ComponentListener, FocusListener {

	private JLabel yearL;
	private JTextField yearF;
	private JLabel monthL;
	private JTextField monthF;
	private JLabel dayL;
	private JTextField dayF;
	private JLabel sTimeHL;
	private JTextField sTimeH;
	private JLabel sTimeML;
	private JTextField sTimeM;
	private JLabel eTimeHL;
	private JTextField eTimeH;
	private JLabel eTimeML;
	private JTextField eTimeM;

	private JCheckBox remiCheck;
	private JLabel remiHL;
	private JTextField remiH;
	private JLabel remiML;
	private JTextField remiM;

	private JLabel freqLabel;
	private JComboBox freqCombo;

	private DefaultListModel model;
	private JTextField titleField;

	private JComboBox locationField;

	private JButton saveBut;
	private JButton CancelBut;
	private JButton inviteBut;
	private JButton rejectBut;

	private Appt NewAppt;
	private CalGrid parent;
	private boolean isNew = true;
	private boolean isChanged = true;
	private boolean isJoint = false;

	private JTextArea detailArea;

	private JSplitPane pDes;
	JPanel detailPanel;

	//	private JTextField attendField;
	//	private JTextField rejectField;
	//	private JTextField waitingField;
	private int selectedApptId = -1;


	private void commonConstructor(String title, CalGrid cal) {
		parent = cal;
		this.setAlwaysOnTop(true);
		setTitle(title);
		setModal(false);

		Container contentPane;
		contentPane = getContentPane();

		JPanel pDate = new JPanel();
		Border dateBorder = new TitledBorder(null, "DATE");
		pDate.setBorder(dateBorder);

		yearL = new JLabel("YEAR: ");
		pDate.add(yearL);
		yearF = new JTextField(6);
		yearF.addFocusListener(this);
		pDate.add(yearF);
		monthL = new JLabel("MONTH: ");
		pDate.add(monthL);
		monthF = new JTextField(4);
		monthF.addFocusListener(this);
		pDate.add(monthF);
		dayL = new JLabel("DAY: ");
		pDate.add(dayL);
		dayF = new JTextField(4);
		dayF.addFocusListener(this);
		pDate.add(dayF);

		JPanel psTime = new JPanel();
		Border stimeBorder = new TitledBorder(null, "START TIME");
		psTime.setBorder(stimeBorder);
		sTimeHL = new JLabel("Hour");
		psTime.add(sTimeHL);
		sTimeH = new JTextField(4);
		sTimeH.addFocusListener(this);
		psTime.add(sTimeH);
		sTimeML = new JLabel("Minute");
		psTime.add(sTimeML);
		sTimeM = new JTextField(4);
		sTimeM.addFocusListener(this);
		psTime.add(sTimeM);

		JPanel peTime = new JPanel();
		Border etimeBorder = new TitledBorder(null, "END TIME");
		peTime.setBorder(etimeBorder);
		eTimeHL = new JLabel("Hour");
		peTime.add(eTimeHL);
		eTimeH = new JTextField(4);
		eTimeH.addFocusListener(this);
		peTime.add(eTimeH);
		eTimeML = new JLabel("Minute");
		peTime.add(eTimeML);
		eTimeM = new JTextField(4);
		eTimeM.addFocusListener(this);
		peTime.add(eTimeM);

		JPanel pFreq = new JPanel();
		Border freqBorder = new TitledBorder(null, "FREQUENCY");
		pFreq.setBorder(freqBorder);
		String[] freqString={"One-time","Daily","Weekly","Monthly"};
		freqCombo=new JComboBox(freqString);
		pFreq.add(freqCombo);
		freqLabel=new JLabel("event");

		JPanel pRemi=new JPanel();
		Border remiBorder = new TitledBorder(null, "REMINDER");
		pRemi.setBorder(remiBorder);
		remiCheck=new JCheckBox("Remind me before");
		pRemi.add(remiCheck);
		remiH=new JTextField(2);
		remiH.addFocusListener(this);
		remiH.setText("0");
		remiH.disable();
		pRemi.add(remiH);
		remiHL=new JLabel("hour(s)");
		pRemi.add(remiHL);
		remiM=new JTextField(2);
		remiM.addFocusListener(this);
		remiM.setText("0");
		remiM.disable();
		pRemi.add(remiM);
		remiML=new JLabel("minute(s)");
		pRemi.add(remiML);

		remiCheck.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if(e.getStateChange()==ItemEvent.SELECTED){
					remiH.enable();
					remiM.enable();
					repaint();
				}
				else if(e.getStateChange()==ItemEvent.DESELECTED){
					remiH.disable();
					remiM.disable();
					repaint();
				}
			}
		});

		JPanel pFreqAndRemi=new JPanel();
		pFreqAndRemi.setLayout(new BorderLayout());
		pFreqAndRemi.add("West",pFreq);
		pFreqAndRemi.add(pRemi);

		JPanel pTime = new JPanel();
		pTime.setLayout(new BorderLayout());
		pTime.add("West", psTime);
		pTime.add(peTime);
		pTime.add("South", pFreqAndRemi);

		Location[] locations = cal.controller.getLocationList();
		if(locations == null){
			locations = new Location[0];
		}
		JPanel plocation=new JPanel();
		Border locatBorder = new TitledBorder(null, "LOCATION");
		plocation.setBorder(locatBorder);
		locationField=new JComboBox(locations);
		plocation.add(locationField);

		JPanel pTimeAndpLocation=new JPanel();
		pTimeAndpLocation.setLayout(new BorderLayout());
		pTimeAndpLocation.setBorder(new BevelBorder(BevelBorder.RAISED));
		pTimeAndpLocation.add(pTime,BorderLayout.NORTH);
		pTimeAndpLocation.add(plocation,BorderLayout.SOUTH);

		JPanel top = new JPanel();
		top.setLayout(new BorderLayout());
		top.setBorder(new BevelBorder(BevelBorder.RAISED));
		top.add(pDate, BorderLayout.NORTH);
		top.add(pTimeAndpLocation, BorderLayout.CENTER);

		contentPane.add("North", top);

		JPanel titleAndTextPanel = new JPanel();
		JLabel titleL = new JLabel("TITLE");
		titleField = new JTextField(15);
		titleField.addFocusListener(this);
		titleAndTextPanel.add(titleL);
		titleAndTextPanel.add(titleField);

		detailPanel = new JPanel();
		detailPanel.setLayout(new BorderLayout());
		Border detailBorder = new TitledBorder(null, "Appointment Description");
		detailPanel.setBorder(detailBorder);
		detailArea = new JTextArea(20, 30);
		detailArea.addFocusListener(this);

		detailArea.setEditable(true);
		JScrollPane detailScroll = new JScrollPane(detailArea);
		detailPanel.add(detailScroll);

		pDes = new JSplitPane(JSplitPane.VERTICAL_SPLIT, titleAndTextPanel,
				detailPanel);

		top.add(pDes, BorderLayout.SOUTH);

		if (NewAppt != null) {
			detailArea.setText(NewAppt.getInfo());

		}

		JPanel panel2 = new JPanel();
		panel2.setLayout(new FlowLayout(FlowLayout.RIGHT));

		//		inviteBut = new JButton("Invite");
		//		inviteBut.addActionListener(this);
		//		panel2.add(inviteBut);

		saveBut = new JButton("Save");
		saveBut.addActionListener(this);
		panel2.add(saveBut);

		rejectBut = new JButton("Reject");
		rejectBut.addActionListener(this);
		panel2.add(rejectBut);
		rejectBut.show(false);

		CancelBut = new JButton("Cancel");
		CancelBut.addActionListener(this);
		panel2.add(CancelBut);

		contentPane.add("South", panel2);
		NewAppt = new Appt();

		if (this.getTitle().equals("Join Appointment Content Change") || this.getTitle().equals("Join Appointment Invitation")){
			inviteBut.show(false);
			rejectBut.show(true);
			CancelBut.setText("Consider Later");
			saveBut.setText("Accept");
		}
		if (this.getTitle().equals("Someone has responded to your Joint Appointment invitation") ){
			inviteBut.show(false);
			rejectBut.show(false);
			CancelBut.show(false);
			saveBut.setText("confirmed");
		}
		if (this.getTitle().equals("Join Appointment Invitation") || this.getTitle().equals("Someone has responded to your Joint Appointment invitation") || this.getTitle().equals("Join Appointment Content Change")){
			allDisableEdit();
		}
		pack();

	}

	AppScheduler(String title, CalGrid cal, int selectedApptId) {
		this.selectedApptId = selectedApptId;
		commonConstructor(title, cal);
	}

	AppScheduler(String title, CalGrid cal) {
		commonConstructor(title, cal);
	}

	public void actionPerformed(ActionEvent e) {

		// distinguish which button is clicked and continue with require function
		if (e.getSource() == CancelBut) {

			setVisible(false);
			dispose();
		} else if (e.getSource() == saveBut) {
			saveButtonResponse();

		} else if (e.getSource() == rejectBut){
			if (JOptionPane.showConfirmDialog(this, "Reject this joint appointment?", "Confirmation", JOptionPane.YES_NO_OPTION) == 0){
				NewAppt.addReject(getCurrentUser());
				NewAppt.getAttendList().remove(getCurrentUser());
				NewAppt.getWaitingList().remove(getCurrentUser());
				this.setVisible(false);
				dispose();
			}
		}
		parent.getAppList().clear();
		parent.getAppList().setTodayAppt(parent.GetDayAppt(parent.currentD));
		parent.repaint();
	}

	private JPanel createPartOperaPane() {
		JPanel POperaPane = new JPanel();
		JPanel browsePane = new JPanel();
		JPanel controPane = new JPanel();

		POperaPane.setLayout(new BorderLayout());
		TitledBorder titledBorder1 = new TitledBorder(BorderFactory
				.createEtchedBorder(Color.white, new Color(178, 178, 178)),
				"Add Participant:");
		browsePane.setBorder(titledBorder1);

		POperaPane.add(controPane, BorderLayout.SOUTH);
		POperaPane.add(browsePane, BorderLayout.CENTER);
		POperaPane.setBorder(new BevelBorder(BevelBorder.LOWERED));
		return POperaPane;

	}

	private int getValidNoti(){
		int notimin=Utility.getNumber(remiH.getText());
		if (notimin==-1)
			return -1;
		if (notimin<0){
			JOptionPane.showMessageDialog(this, "Please input proper time",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return -1;
		}
		if (notimin>(Utility.getNumber(sTimeH.getText())*60+Utility.getNumber(sTimeM.getText()))){
			JOptionPane.showMessageDialog(this, "The notification should be within the day",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return -1;
		}
		return notimin;
	}

	private int[] getValidDate() {

		int[] date = new int[3];
		date[0] = Utility.getNumber(yearF.getText());
		date[1] = Utility.getNumber(monthF.getText());
		if (date[0] < 1980 || date[0] > 2100) {
			JOptionPane.showMessageDialog(this, "Please input proper year",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if (date[1] <= 0 || date[1] > 12) {
			JOptionPane.showMessageDialog(this, "Please input proper month",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		date[2] = Utility.getNumber(dayF.getText());
		int monthDay = CalGrid.monthDays[date[1] - 1];
		if (date[1] == 2) {
			GregorianCalendar c = new GregorianCalendar();
			if (c.isLeapYear(date[0]))
				monthDay = 29;
		}
		if (date[2] <= 0 || date[2] > monthDay) {
			JOptionPane.showMessageDialog(this,
					"Please input proper month day", "Input Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
		return date;
	}

	private int getTime(JTextField h, JTextField min) {

		int hour = Utility.getNumber(h.getText());
		if (hour == -1)
			return -1;
		int minute = Utility.getNumber(min.getText());
		if (minute == -1)
			return -1;

		return (hour * 60 + minute);

	}

	private int[] getValidTimeInterval() {

		int[] result = new int[2];
		result[0] = getTime(sTimeH, sTimeM);
		result[1] = getTime(eTimeH, eTimeM);
		if ((result[0] % 15) != 0 || (result[1] % 15) != 0) {
			JOptionPane.showMessageDialog(this,
					"Minute Must be 0, 15, 30, or 45 !", "Input Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}

		if (!sTimeM.getText().equals("0") && !sTimeM.getText().equals("15") && !sTimeM.getText().equals("30") && !sTimeM.getText().equals("45") 
				|| !eTimeM.getText().equals("0") && !eTimeM.getText().equals("15") && !eTimeM.getText().equals("30") && !eTimeM.getText().equals("45")){
			JOptionPane.showMessageDialog(this,
					"Minute Must be 0, 15, 30, or 45 !", "Input Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}

		if (result[1] == -1 || result[0] == -1) {
			JOptionPane.showMessageDialog(this, "Please check time",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if (result[1] <= result[0]) {
			JOptionPane.showMessageDialog(this,
					"End time should be bigger than \nstart time",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if ((result[0] < AppList.OFFSET * 60)
				|| (result[1] > (AppList.OFFSET * 60 + AppList.ROWNUM * 2 * 15))) {
			JOptionPane.showMessageDialog(this, "Out of Appointment Range !",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		return result;
	}


	private void saveButtonResponse() {
		// Fix Me!
		// Save the appointment to the hard disk
		int[] Appt_Date = getValidDate();
		int[] Appt_Time = getValidTimeInterval();

		if(Appt_Date==null||Appt_Time==null)
			return;

		Timestamp start=CreateTimeStamp(Appt_Date,Appt_Time[0]);
		Timestamp end=CreateTimeStamp(Appt_Date,Appt_Time[1]);
		TimeSpan timespan=new TimeSpan(start,end);

		Appt temp=new Appt();
		temp.setTimeSpan(timespan);
		temp.setFreq(freqCombo.getSelectedIndex());
		temp.setID(NewAppt.getID());

		if(remiH.getText() != null || remiM.getText() != null){
			int th1 = Integer.parseInt(sTimeH.getText());
			int th2 = Integer.parseInt(remiH.getText());
			th1-=th2;
			th2 = Integer.parseInt(sTimeM.getText());
			int n_time = Integer.parseInt(remiM.getText());
			th2-=n_time;

			n_time = th1*100+th2;
			Timestamp noti_time = CreateTimeStamp(Appt_Date,n_time);
			if(TimeMachine.getCurrentTime()==noti_time){
				JOptionPane.showMessageDialog(this, "Reminder",
						titleField.getText()+" schedules after "+remiH+" hours "+remiM+" mins", JOptionPane.OK_OPTION);

			}
		}

		if(parent.controller.isTimeConflict(temp)==true) {
			JOptionPane.showMessageDialog(this, "Overlap with other appointments !", "Error: Overlapped Appointments", JOptionPane.ERROR_MESSAGE);
			return;
		}

		NewAppt.setTitle(titleField.getText());
		NewAppt.setInfo(detailArea.getText());
		NewAppt.setTimeSpan(timespan);
		NewAppt.setFreq(freqCombo.getSelectedIndex());

		if(NewAppt.TimeSpan().StartTime().before(TimeMachine.getCurrentTime())) {
			JOptionPane.showMessageDialog(this, "Past Appointments cannot be scheduled.", "Error: Schedule", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if(remiCheck.isSelected())
			NewAppt.setReminder(Integer.parseInt(remiH.getText())*60+Integer.parseInt(remiM.getText()));

		parent.controller.ManageAppt(NewAppt, ApptStorageControllerImpl.NEW);

		this.setVisible(false);
		dispose();

	}

	private Timestamp CreateTimeStamp(int[] date, int time) {
		Timestamp stamp = new Timestamp(0);
		stamp.setYear(date[0]);
		stamp.setMonth(date[1] - 1);
		stamp.setDate(date[2]);
		stamp.setHours(time / 60);
		stamp.setMinutes(time % 60);
		return stamp;
	}

	public void updateSetApp(Appt appt) {
		// Fix Me!
		Calendar ref = new GregorianCalendar();
		ref.setTime(appt.TimeSpan().StartTime());
		yearF.setText(Integer.toString(ref.get(Calendar.YEAR)));
		monthF.setText(Integer.toString(ref.get(Calendar.MONTH)+1));
		dayF.setText(Integer.toString(ref.get(Calendar.DAY_OF_MONTH)));
		sTimeH.setText(Integer.toString(ref.get(Calendar.HOUR_OF_DAY)));
		sTimeM.setText(Integer.toString(ref.get(Calendar.MINUTE)));

		ref.setTime(appt.TimeSpan().EndTime());
		eTimeH.setText(Integer.toString(ref.get(Calendar.HOUR_OF_DAY)));
		eTimeM.setText(Integer.toString(ref.get(Calendar.MINUTE)));

		titleField.setText(appt.getTitle());
		detailArea.setText(appt.getInfo());
		NewAppt=appt;
	}

	public void componentHidden(ComponentEvent e) {

	}

	public void componentMoved(ComponentEvent e) {

	}

	public void componentResized(ComponentEvent e) {

		Dimension dm = pDes.getSize();
		double width = dm.width * 0.93;
		double height = dm.getHeight() * 0.6;
		detailPanel.setSize((int) width, (int) height);

	}

	public void componentShown(ComponentEvent e) {

	}

	public String getCurrentUser()		// get the id of the current user
	{
		return this.parent.mCurrUser.ID();
	}

	private void allDisableEdit(){
		yearF.setEditable(false);
		monthF.setEditable(false);
		dayF.setEditable(false);
		sTimeH.setEditable(false);
		sTimeM.setEditable(false);
		eTimeH.setEditable(false);
		eTimeM.setEditable(false);
		titleField.setEditable(false);
		detailArea.setEditable(false);
	}
	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		((JTextComponent) e.getSource()).selectAll();
	}
	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub

		if(e.getSource()==yearF||e.getSource()==titleField||e.getSource()==detailArea)
			return;

		String text=((JTextComponent) e.getSource()).getText();
		if(text==""||Utility.getNumber(text)==-1){
			((JTextComponent) e.getSource()).setText("0");
			return;
		}

		int update=Utility.getNumber(text);
		int ulimit=45,llimit=0;
		if(e.getSource()==sTimeM||e.getSource()==eTimeM||e.getSource()==remiM){
			if(update>45)
				update=45;
			else if(update<0)
				update=0;
			else
				update=update-update%15;
		}
		else if(e.getSource()==monthF){
			ulimit=12;
			llimit=1;
		}
		else if(e.getSource()==dayF){
			ulimit=31;
			llimit=1;
		}
		else if(e.getSource()==remiH||e.getSource()==sTimeH||e.getSource()==eTimeH){
			ulimit=18;
			llimit=8;
		}
		if(update>ulimit)
			update=ulimit;
		else if(update<llimit)
			update=llimit;
		((JTextComponent) e.getSource()).setText(Integer.toString(update));

	}
}