package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.*;
import hkust.cse.calendar.unit.*;
import hkust.cse.calendar.gui.*;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


import javax.swing.text.JTextComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class LocationsDialog extends JDialog implements ActionListener
{

	private static LocationsDialog ld= null;
	private static ApptStorageControllerImpl controller;
	
	private DefaultListModel<String> listModel;
	private JList<String> list;
	private JButton add_button;
	private JButton del_button;
	private JTextField modify_l;
	private int i = 1;

	private ArrayList<String> ListArray = new ArrayList <String>();
	
	public LocationsDialog()
	{	
		this.setAlwaysOnTop(true);
		setTitle("Locations Stored");
		setModal(false);
	
		Container contentPane;
		contentPane = getContentPane();		
		
		JPanel l_jlist = new JPanel(new BorderLayout());
		listModel = new DefaultListModel();
		if(list == null){
			list = new JList(listModel);
		}

		
		l_jlist.add(new JScrollPane(list)); 
		
		contentPane.add("North",l_jlist);
		
		JPanel f_b = new JPanel();
		modify_l = new JTextField(50);
		f_b.add(modify_l);
		

		add_button = new JButton("Add");
		f_b.add(add_button);
		del_button = new JButton("Remove");
		f_b.add(del_button);

		contentPane.add("South",f_b);

		pack();


	}

	public void setController(ApptStorageControllerImpl con){
		controller=con;
		Location[] locations = controller.getLocationList();
		if(locations == null){
			locations = new Location[0];
		}
	}
	
	public static LocationsDialog getLD(){
		if(ld==null){
			ld=new LocationsDialog();
		}
		return ld;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == add_button)
		{
			ListArray.add(modify_l.getText());

			listModel.addElement(modify_l.toString());
			list.add(modify_l.getText(), add_button);
		}
		else if(e.getSource() == del_button)
		{
			for(int i=0;i<listModel.getSize();i++)
			{
				if(listModel.getElementAt(i)== modify_l.toString())
					listModel.remove(i);
			}
		}

	}

}

