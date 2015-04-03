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

	private ApptStorageControllerImpl controller;
	
	private DefaultListModel<String> listModel;
	private JList<String> list;
	private JPanel pList;
	private JScrollPane scrollP;
	
	private JButton add_button;
	private JButton del_button;
	private JTextField modify_l;
	private int i = 1;

	/**
	 * @param con
	 */
	public LocationsDialog(ApptStorageControllerImpl con)
	{	
		this.controller=con;
		this.setAlwaysOnTop(true);
		setTitle("Locations Stored");
		setModal(false);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});
		
		Container contentPane;
		contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());		

		listModel = new DefaultListModel<String>();
		list=new JList<String>(listModel);
		list.setFixedCellWidth(300);
		scrollP=new JScrollPane(list);
		pList = new JPanel();
		pList.add(scrollP);
		updateList();
		
		contentPane.add("North",pList);
		
		JPanel f_b = new JPanel();
		modify_l = new JTextField(15);
		modify_l.setText("");
		f_b.add(modify_l);
		
		add_button = new JButton("Add");
		add_button.addActionListener(this);
		f_b.add(add_button);
		del_button = new JButton("Remove");
		del_button.addActionListener(this);
		f_b.add(del_button);

		contentPane.add("South",f_b);
		
		repaint();
		pack();

	}
	
	public void updateList(){
		ArrayList<Location> locs=controller.getLocationList();
		listModel.clear();
		if(locs==null)
			return;
		for(int i=0;i<locs.size();i++){
			listModel.addElement(locs.get(i).getName());
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == add_button)
		{
			ArrayList<Location> locs=controller.getLocationList();
			String newStr=modify_l.getText();
			modify_l.setText("");
			if(locs==null){
				locs=new ArrayList<Location>();
				locs.add(new Location(newStr));
				controller.setLocationList(locs);
			}
			else{
				ArrayList<Location> newLocs=new ArrayList<Location>();
				for(int i=0;i<locs.size();i++){
					if(locs.get(i).getName().equals(newStr))
						return;
					else
						newLocs.add(locs.get(i));
				}
				newLocs.add(new Location(newStr));
				controller.setLocationList(newLocs);
			}
		}
		else if(e.getSource() == del_button)
		{
			ArrayList<Location> locs=controller.getLocationList();
			String tar=list.getSelectedValue();
			for(int i=0;i<locs.size();i++)
			{
				if(locs.get(i).getName().equals(tar)){
					locs.remove(i);
					break;
				}
			}
		}
		updateList();

	}

}

