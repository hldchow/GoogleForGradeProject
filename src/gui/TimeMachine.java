package hkust.cse.calendar.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;


public class TimeMachine extends JDialog implements Runnable, ActionListener, FocusListener {
	
	private static TimeMachine tm=null;
	
	private static Timestamp currentTime;
	
	private Calendar cal;
	
	private JLabel sYearL;
	private JTextField sYear;
	private JLabel sMonthL;
	private JTextField sMonth;
	private JLabel sDayL;
	private JTextField sDay;
	private JLabel sTimeHL;
	private JTextField sTimeH;
	private JLabel sTimeML;
	private JTextField sTimeM;
	
	private String datestr,timestr;
	private JLabel timeL;

	private JLabel freqL;
	private JLabel freqHL;
	private JTextField freqHT;
	private JLabel freqML;
	private JTextField freqMT;
	private JLabel freqSL;
	private JTextField freqST;
	
	private JButton startBut;
	private JButton stopBut;
	private JButton rewindBut;
	private JButton resetBut;
	
	private int freqH=0;
	private int freqM=0;
	private int freqS=1;
	
	private CalGrid parent;
	
	public TimeMachine(){
		cal=GregorianCalendar.getInstance();
		
		currentTime=new Timestamp(0);
		currentTime.setYear(cal.get(Calendar.YEAR)-1900);
		currentTime.setMonth(cal.get(Calendar.MONTH));
		currentTime.setDate(cal.get(Calendar.DAY_OF_MONTH));
		currentTime.setHours(cal.get(Calendar.HOUR_OF_DAY));
		currentTime.setMinutes(cal.get(Calendar.MINUTE));
		currentTime.setSeconds(cal.get(Calendar.SECOND));
		
		Thread t = new Thread(this);
		t.start();
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});
		
		this.setAlwaysOnTop(true);
		setTitle("Time Machine");
		setModal(false);
		setVisible(false);

		Container contentPane;
		contentPane = getContentPane();

		//Time
		JPanel pTime = new JPanel();
		Border pTimeBorder = new TitledBorder(null, "Time");
		pTime.setBorder(pTimeBorder);
		
		timeL=new JLabel(" ");
		pTime.add(timeL);
		
		
		//start time
		JPanel pStart = new JPanel();
		Border pStartBorder = new TitledBorder(null, "Start");
		pStart.setBorder(pStartBorder);
		
		sYearL=new JLabel("Year:");
		pStart.add(sYearL);
		sYear=new JTextField(3);
		sYear.setText(Integer.toString(cal.get(Calendar.YEAR)));
		sYear.addFocusListener(this);
		pStart.add(sYear);
		
		sMonthL=new JLabel("Month:");
		pStart.add(sMonthL);
		sMonth=new JTextField(2);
		sMonth.setText(Integer.toString(cal.get(Calendar.MONTH)+1));
		sMonth.addFocusListener(this);
		pStart.add(sMonth);
		
		sDayL=new JLabel("Day:");
		pStart.add(sDayL);
		sDay=new JTextField(2);
		sDay.setText(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)));
		sDay.addFocusListener(this);
		pStart.add(sDay);

		sTimeHL=new JLabel("Hour:");
		pStart.add(sTimeHL);
		sTimeH=new JTextField(2);
		sTimeH.setText("0");
		sTimeH.addFocusListener(this);
		pStart.add(sTimeH);

		sTimeML=new JLabel("Minute:");
		pStart.add(sTimeML);
		sTimeM=new JTextField(2);
		sTimeM.setText("0");
		sTimeM.addFocusListener(this);
		pStart.add(sTimeM);
		
		
		//freq
		JPanel pFreq = new JPanel();
		Border pFreqBorder = new TitledBorder(null, "Frequency");
		pFreq.setBorder(pFreqBorder);
		
		freqL=new JLabel("Pass ");
		pFreq.add(freqL);
		freqHT=new JTextField(2);
		freqHT.setText("12");
		freqHT.addFocusListener(this);
		pFreq.add(freqHT);
		freqHL=new JLabel("hours");
		pFreq.add(freqHL);
		freqMT=new JTextField(2);
		freqMT.setText("0");
		freqMT.addFocusListener(this);
		pFreq.add(freqMT);
		freqML=new JLabel("minutes");
		pFreq.add(freqML);
		freqST=new JTextField(2);
		freqST.setText("0");
		freqST.addFocusListener(this);
		pFreq.add(freqST);
		freqSL=new JLabel("seconds per second");
		pFreq.add(freqSL);
		
		
		JPanel pTop = new JPanel();
		pTop.setLayout(new BorderLayout());
		pTop.add("North",pTime);
		pTop.add("Center",pStart);
		pTop.add("South",pFreq);
		
		contentPane.add("North",pTop);
		
		//buttons
		JPanel pBut = new JPanel();
		pBut.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		startBut=new JButton("Start");
		startBut.addActionListener(this);
		pBut.add(startBut);

		rewindBut=new JButton("Rewind");
		rewindBut.addActionListener(this);
		pBut.add(rewindBut);
		
		stopBut=new JButton("Stop");
		stopBut.addActionListener(this);
		stopBut.setEnabled(false);
		pBut.add(stopBut);
		
		resetBut=new JButton("Reset");
		resetBut.addActionListener(this);
		pBut.add(resetBut);
		
		contentPane.add("South",pBut);
		
		pack();
	}
	
	public static Timestamp getCurrentTime(){
		return currentTime;
	}
	
	public static TimeMachine getTimeMachine(){
		if(tm==null){
			tm=new TimeMachine();
		}
		return tm;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==startBut){
			currentTime.setYear(Integer.parseInt(sYear.getText())-1900);
			currentTime.setMonth(Integer.parseInt(sMonth.getText())-1);
			currentTime.setDate(Integer.parseInt(sDay.getText()));
			currentTime.setHours(Integer.parseInt(sTimeH.getText()));
			currentTime.setMinutes(Integer.parseInt(sTimeM.getText()));
			
			freqH=Integer.parseInt(freqHT.getText());
			freqM=Integer.parseInt(freqMT.getText());
			freqS=Integer.parseInt(freqST.getText());
			
			startBut.setEnabled(false);
			rewindBut.setEnabled(false);
			stopBut.setEnabled(true);
		}
		else if(e.getSource()==rewindBut){
			freqH=Integer.parseInt(freqHT.getText());
			freqM=Integer.parseInt(freqMT.getText());
			freqS=Integer.parseInt(freqST.getText());
			
			if(freqH>0)
				freqH*=-1;
			if(freqM>0)
				freqM*=-1;
			if(freqS>0)
				freqS*=-1;
			
			startBut.setEnabled(false);
			rewindBut.setEnabled(false);
			stopBut.setEnabled(true);
		}
		else if(e.getSource()==stopBut){
			freqH=0;
			freqM=0;
			freqS=1;
			
			startBut.setEnabled(true);
			rewindBut.setEnabled(true);
			stopBut.setEnabled(false);
		}
		else if(e.getSource()==resetBut){
			cal=GregorianCalendar.getInstance();
			currentTime.setYear(cal.get(Calendar.YEAR)-1900);
			currentTime.setMonth(cal.get(Calendar.MONTH));
			currentTime.setDate(cal.get(Calendar.DAY_OF_MONTH));
			currentTime.setHours(cal.get(Calendar.HOUR_OF_DAY));
			currentTime.setMinutes(cal.get(Calendar.MINUTE));
			currentTime.setSeconds(cal.get(Calendar.SECOND));
			freqH=0;
			freqM=0;
			freqS=1;

			startBut.setEnabled(true);
			rewindBut.setEnabled(true);
			stopBut.setEnabled(false);
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		((JTextComponent) e.getSource()).selectAll();
	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
		int llimit=0,ulimit=99;
		int result=Utility.getNumber(((JTextComponent) e.getSource()).getText());
		if(result==-1)
			result=0;
		if(e.getSource()==sYear){
			llimit=1980;
			ulimit=2100;
		}
		else if(e.getSource()==sMonth){
			llimit=1;
			ulimit=12;
		}
		else if(e.getSource()==sDay){
			llimit=1;
			ulimit=31;
		}
		if(result<llimit)
			result=llimit;
		if(result>ulimit)
			result=ulimit;
		((JTextComponent) e.getSource()).setText(Integer.toString(result));
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		long time=1000;
		while(true){
			try {
				long start = System.currentTimeMillis();
				Thread.sleep(time);
				updateTime();
				long overTime = System.currentTimeMillis() - start-1000;
				time=1000-overTime;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void updateTime() {
		// TODO Auto-generated method stub
		currentTime.setHours(currentTime.getHours()+freqH);
		currentTime.setMinutes(currentTime.getMinutes()+freqM);
		currentTime.setSeconds(currentTime.getSeconds()+freqS);

		String Mon,Day,Hr,Min,Sec;
		int mon=currentTime.getMonth()+1,
				day=currentTime.getDate(),
				hr=currentTime.getHours(),
				min=currentTime.getMinutes(),
				sec=currentTime.getSeconds();
		if(mon<10)
			Mon="0"+mon;
		else 
			Mon=""+mon;
		if(day<10)
			Day="0"+day;
		else 
			Day=""+day;
		if(hr<10)
			Hr="0"+hr;
		else 
			Hr=""+hr;
		if(min<10)
			Min="0"+min;
		else 
			Min=""+min;
		if(sec<10)
			Sec="0"+sec;
		else
			Sec=""+sec;

		datestr=(currentTime.getYear()+1900)+" / "+Mon+" / "+Day;
		timestr=Hr+" : "+Min+" : "+Sec;
		timeL.setText(datestr+"           "+timestr);
	}


}
