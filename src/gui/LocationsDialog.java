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

	//private DefaultListModel<Location> listModel;
	private JList<Location> list;
	private JButton add_button;
	private JButton del_button;
	private JTextField modify_l;

	public LocationsDialog(ApptStorageControllerImpl controller)
	{	
		this.setAlwaysOnTop(true);
		setTitle("Locations Stored");
		setModal(false);
		//setVisible(false);
	
		Container contentPane;
		contentPane = getContentPane();		
		
		JPanel l_jlist = new JPanel();
		
		CalGrid grid = new CalGrid(new ApptStorageControllerImpl(null));
		setVisible(false);
		
		Location[] locations = grid.controller.getLocationList();
		if(locations == null){
			locations = new Location[0];
		}
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

	public static LocationsDialog getLD(){
		return ld;
	}
	
	/*public JList<Location> getL_List(){
		return list;
	}*/

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == add_button)
		{
			list.add(getName(), add_button);
		}
		else if(e.getSource() == del_button)
		{
			for(int i=0;i<list.getModel().getSize();i++)
			{
				if(list.getComponent(i).toString()== getName())
					list.remove(i);
			}
		}

	}
	/*list.addListSelectionListener(new ListSelectionListener(){
	public void valueChanged(ListSelectionEvent e){
		if(e.getSource() == add_button)
		{
			list.add("temp_loc", list);
		}
		else if(e.getSource() == del_button)
		{
			for(int i=0;i<list.getModel().getSize();i++)
			{
				if(list.getComponent(i).toString()=="temp_loc")
					list.remove(i);
			}
		}
	}
});*/


}

