package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.*;
import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.unit.Location;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


import javax.swing.JLabel;
import javax.swing.JOptionPane;


public abstract class LocationsDialog extends JFrame implements ActionListener 
{

	//private static final long serialVersionUID = 1L;

	//private ApptStorageControllerImpl _controller;

	//private JPanel locPanel;
	private DefaultListModel<Location> listModel;
	private JList<Location> list;
	private JTextField locNameText;
	private JButton add_button;
	private JButton del_button;
	private JTextField modify_l;

	public LocationsDialog()
	{
		//setTitle("Locations");

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		/*Container contentPane;
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		

		JPanel l_list = new JPanel();
		l_list.setLayout(getLayout());
		getContentPane().add(l_list);
		JScrollPane scrollPane_1 = new JScrollPane(_locations);*/
		
		DefaultListModel model = new DefaultListModel();
		JList l_list = new JList(model);
		
		l_list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		l_list.setVisibleRowCount(-1);
		l_list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		
		JFrame l_frame = new JFrame ("Locations");
		l_frame.getContentPane().add(new JScrollPane(l_list));
		l_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		l_frame.setVisible(true);
		l_frame.setLocationRelativeTo(null);
		l_frame.pack();
		

		
		add_button = new JButton("Add");
		add_button.addActionListener(this);
		del_button = new JButton("Remove");
		del_button.addActionListener(this);

		modify_l = new JTextField(50);
		String temp_loc = modify_l.getText();



		list.addListSelectionListener(new ListSelectionListener(){
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
		});

		/*public LocationsDialog(ApptStorageControllerImpl controller){
		_controller = controller;
		this.setLayout(new BorderLayout());
		this.setLocationByPlatform(true);
		this.setSize(300, 200);
		this.setTitle("Locations");



		listModel = new DefaultListModel<Location>();

		list = new JList<Location>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e){
				if(e.getSource() == add_button)
				{

				}
				else if(e.getSource() == del_button)
				{

				}
			}
		});
	}*/
	}
}

