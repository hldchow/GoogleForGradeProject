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
	
	private JLabel eYearL;
	private JTextField eYear;
	private JLabel eMonthL;
	private JTextField eMonth;
	private JLabel eDayL;
	private JTextField eDay;
	private JLabel eTimeHL;
	private JTextField eTimeH;
	private JLabel eTimeML;
	private JTextField eTimeM;

	private JLabel freqL;
	private JTextField freqT;
	private JLabel freqL2;
	private JLabel freqHL;
	private JTextField freqHT;
	private JLabel freqML;
	private JTextField freqMT;
	
	private JButton startBut;
	private JButton stopBut;
	private JButton rewindBut;
	private JButton resetBut;
	
	private int freq=1000;
	private int freqH=0;
	private int freqM=0;
	private int freqS=1;
	
	public TimeMachine(){
		currentTime=new Timestamp(0);
		currentTime.setYear(cal.get(Calendar.YEAR));
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
		
		//end time
		JPanel pEnd = new JPanel();
		Border pEndBorder = new TitledBorder(null, "End");
		pEnd.setBorder(pEndBorder);
		
		eYearL=new JLabel("Year:");
		pEnd.add(eYearL);
		eYear=new JTextField(4);
		eYear.setText(Integer.toString(cal.get(Calendar.YEAR)));
		pEnd.add(eYear);
		
		eMonthL=new JLabel("Month:");
		pEnd.add(eMonthL);
		eMonth=new JTextField(2);
		eMonth.setText(Integer.toString(cal.get(Calendar.MONTH)+1));
		pEnd.add(eMonth);
		
		eDayL=new JLabel("Day:");
		pEnd.add(eDayL);
		eDay=new JTextField(2);
		eDay.setText(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)+1));
		pEnd.add(eDay);

		eTimeHL=new JLabel("Hour:");
		pEnd.add(eTimeHL);
		eTimeH=new JTextField(2);
		eTimeH.setText("0");
		pEnd.add(eTimeH);

		eTimeML=new JLabel("Minute:");
		pEnd.add(eTimeML);
		eTimeM=new JTextField(2);
		eTimeM.setText("0");
		pEnd.add(eTimeM);
		
		//freq
		JPanel pFreq = new JPanel();
		Border pFreqBorder = new TitledBorder(null, "Frequency");
		pFreq.setBorder(pFreqBorder);
		
		freqL=new JLabel("Every");
		pFreq.add(freqL);
		freqT=new JTextField(2);
		pFreq.add(freqT);
		freqL2=new JLabel("milliseconds: ");
		pFreq.add(freqL2);
		freqHT=new JTextField(2);
		pFreq.add(freqHT);
		freqHL=new JLabel("hours");
		pFreq.add(freqHL);
		freqMT=new JTextField(2);
		pFreq.add(freqMT);
		freqML=new JLabel("minutes");
		pFreq.add(freqML);
		
		
		JPanel pTime = new JPanel();
		pTime.setLayout(new BorderLayout());
		pTime.add("North",pStart);
		pTime.add("Center",pEnd);
		pTime.add("South",pFreq);
		
		
		contentPane.add("North",pTime);
		
		
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
			freq=Integer.parseInt(freqT.getText());
			freqH=Integer.parseInt(freqHT.getText());
			freqM=Integer.parseInt(freqMT.getText());
			freqS=0;
		}
		else if(e.getSource()==stopBut){
			freq=1000;
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
				Thread.sleep(freq);
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
		if(CalGrid.timeLabel!=null){
			String Hr,Min,Sec;
			int hr=currentTime.getHours(),
					min=currentTime.getMinutes(),
					sec=currentTime.getSeconds();
			if(hr<10)
				Hr="0"+Integer.toString(currentTime.getHours());
			else 
				Hr=Integer.toString(currentTime.getHours());
			if(min<10)
				Min="0"+Integer.toString(currentTime.getMinutes());
			else 
				Min=Integer.toString(currentTime.getMinutes());
			if(sec<10)
				Sec="0"+Integer.toString(currentTime.getSeconds());
			else
				Sec=Integer.toString(currentTime.getSeconds());
			
			CalGrid.timeLabel.setText("     "+Hr+" : "+Min+" : "+Sec);
		}
	}

}
