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
	
	private Calendar cal=new GregorianCalendar();
	
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
	
	private JButton startBut;
	private JButton stopBut;
	private JButton rewindBut;
	private JButton resetBut;
	
	private int freqH=0;
	private int freqM=0;
	private int freqS=1;
	
	private CalGrid parent;
	
	public TimeMachine(){
		currentTime=new Timestamp(0);
		currentTime.setYear(cal.get(Calendar.YEAR)-1900);
		currentTime.setMonth(cal.get(Calendar.MONTH));
		currentTime.setDate(cal.get(Calendar.DAY_OF_MONTH));
		currentTime.setHours(cal.get(Calendar.HOUR_OF_DAY));
		currentTime.setMinutes(cal.get(Calendar.MINUTE));
		
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
		sYear=new JTextField(4);
		sYear.setText(Integer.toString(cal.get(Calendar.YEAR)));
		pStart.add(sYear);
		
		sMonthL=new JLabel("Month:");
		pStart.add(sMonthL);
		sMonth=new JTextField(2);
		sMonth.setText(Integer.toString(cal.get(Calendar.MONTH)+1));
		pStart.add(sMonth);
		
		sDayL=new JLabel("Day:");
		pStart.add(sDayL);
		sDay=new JTextField(2);
		sDay.setText(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)));
		pStart.add(sDay);

		sTimeHL=new JLabel("Hour:");
		pStart.add(sTimeHL);
		sTimeH=new JTextField(2);
		sTimeH.setText("0");
		pStart.add(sTimeH);

		sTimeML=new JLabel("Minute:");
		pStart.add(sTimeML);
		sTimeM=new JTextField(2);
		sTimeM.setText("0");
		pStart.add(sTimeM);
		
		
		//freq
		JPanel pFreq = new JPanel();
		Border pFreqBorder = new TitledBorder(null, "Frequency");
		pFreq.setBorder(pFreqBorder);
		
		freqL=new JLabel("Pass ");
		pFreq.add(freqL);
		freqHT=new JTextField(2);
		freqHT.setText("12");
		pFreq.add(freqHT);
		freqHL=new JLabel("hours");
		pFreq.add(freqHL);
		freqMT=new JTextField(2);
		freqMT.setText("00");
		pFreq.add(freqMT);
		freqML=new JLabel("minutes per second");
		pFreq.add(freqML);
		
		
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
		pBut.add(startBut);
		stopBut=new JButton("Stop");
		pBut.add(stopBut);
		rewindBut=new JButton("Rewind");
		pBut.add(rewindBut);
		resetBut=new JButton("Reset");
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
			currentTime.setMonth(Integer.parseInt(sMonth.getText()));
			currentTime.setDate(Integer.parseInt(sDay.getText()));
			freqH=Integer.parseInt(freqHT.getText());
			freqM=Integer.parseInt(freqMT.getText());
			freqS=0;
		}
		else if(e.getSource()==stopBut){
			freqH=0;
			freqM=0;
			freqS=1;
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
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try {
				Thread.sleep(1000);
				updateTime();
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
