/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package designchallenge1;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CalendarProgram implements ActionListener{

	    Date d = new Date();
	
		public JFrame frmMain;
		public JPanel calendarPanel;
		public Container pane;
		public JScrollPane scrollCalendarTable;
	    public JLabel monthLabel, yearLabel;
		public JButton btnPrev, btnNext, newEvent;
	    public JComboBox cmbYear;
	        
		public JTable calendarTable;
	    public DefaultTableModel modelCalendarTable;
				 
	    public CalendarProgram() {

			frmMain = new JFrame ("Calendar Application");
	        frmMain.setSize(660, 750);
			frmMain.setResizable(false);
			frmMain.setVisible(true);
			frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			init();
	    }
	    
	    public void init() {
	    	try {
	            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	        } catch (Exception e) {}

			pane = frmMain.getContentPane();
			pane.setLayout(null);
			monthLabel = new JLabel ("January");
			yearLabel = new JLabel ("Change year:");
			cmbYear = new JComboBox();
			btnPrev = new JButton ("<<");
			btnNext = new JButton (">>");
			newEvent = new JButton ("Add Event");
						
			modelCalendarTable = new DefaultTableModel()
	        {
	              public boolean isCellEditable(int rowIndex, int mColIndex)
	              {
	                    return true;
	              }
	        };
	                
			calendarTable = new JTable(modelCalendarTable);
	                
			scrollCalendarTable = new JScrollPane(calendarTable);
			
			calendarPanel = new JPanel(null);
			calendarPanel.setBorder(BorderFactory.createTitledBorder("Calendar"));
			
			btnPrev.addActionListener(this);
			btnNext.addActionListener(this);
			cmbYear.addActionListener(this);
			newEvent.addActionListener(this);
			
			pane.add(calendarPanel);
			calendarPanel.add(monthLabel);
			calendarPanel.add(yearLabel);
			calendarPanel.add(cmbYear);
			calendarPanel.add(btnPrev);
			calendarPanel.add(btnNext);
			calendarPanel.add(newEvent);
			calendarPanel.add(scrollCalendarTable);
			
	        calendarPanel.setBounds(0, 0, 640, 670);
	        monthLabel.setBounds(320-monthLabel.getPreferredSize().width/2, 50, 200, 50);
			yearLabel.setBounds(20, 610, 160, 40);
			cmbYear.setBounds(100, 610, 160, 40);
			btnPrev.setBounds(20, 50, 100, 50);
			btnNext.setBounds(520, 50, 100, 50);
			newEvent.setBounds(460, 610,160, 45);
			scrollCalendarTable.setBounds(20, 100, 600, 500);
	                			
			String[] headers = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"}; //All headers
			for (int i=0; i<7; i++){
				modelCalendarTable.addColumn(headers[i]);
			}
			
			calendarTable.getParent().setBackground(calendarTable.getBackground()); //Set background

			calendarTable.getTableHeader().setResizingAllowed(false);
			calendarTable.getTableHeader().setReorderingAllowed(false);

			calendarTable.setColumnSelectionAllowed(true);
			calendarTable.setRowSelectionAllowed(true);
			calendarTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			calendarTable.setRowHeight(76);
			modelCalendarTable.setColumnCount(7);
			modelCalendarTable.setRowCount(6);
			
			for (int i = d.getYearBound()-100; i <= d.getYearBound()+100; i++)
	        {
				cmbYear.addItem(String.valueOf(i));
			}
			
			refreshCalendar (d.getMonthBound(), d.getYearBound()); //Refresh calendar
		}
	    
	    public void refreshCalendar(int month, int year)
	    {
			String[] months =  {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
			int nod, som, i, j;
				
			btnPrev.setEnabled(true);
			btnNext.setEnabled(true);
			
			if (month == 0 && year <= d.getYearBound()-10)
	                    btnPrev.setEnabled(false);
			if (month == 11 && year >= d.getYearBound()+100)
	                    btnNext.setEnabled(false);
	                
			monthLabel.setText(months[month]); 
			monthLabel.setBounds(320-monthLabel.getPreferredSize().width/2, 50, 360, 50);
	                
			cmbYear.setSelectedItem(""+year);
			
			for (i = 0; i < 6; i++)  
				for (j = 0; j < 7; j++)
					modelCalendarTable.setValueAt(null, i, j);
			
			GregorianCalendar cal = new GregorianCalendar(year, month, 1); 
			nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
			som = cal.get(GregorianCalendar.DAY_OF_WEEK);
			
			for (i = 1; i <= nod; i++)
	        {
				int row = new Integer((i+som-2)/7);
				int column  =  (i+som-2)%7;
				modelCalendarTable.setValueAt(i, row, column); 
			}

			ArrayList<String> colorsSet = new ArrayList<String>();
			Events evt = new Events();  
			
            for (int x = 0 ; x< evt.getEventsSize(); x++) {
            	if (evt.getEvents().get(x).getYear() == year){
                	if (evt.getEvents().get(x).getMonth() == month+1) {
                		
                		for (int y = 1; y <= nod; y++)
            	        {
            				int row = new Integer((y+som-2)/7);
            				int column  =(y+som-2)%7;
            				if (modelCalendarTable.getValueAt(row, column).equals(evt.getEvents().get(x).getDay())) {
            					modelCalendarTable.setValueAt(y+"  "+ evt.getEvents().get(x).getTitle(), row, column); 
            					colorsSet.add(Integer.toString(row));
            					colorsSet.add(Integer.toString(column));
            					colorsSet.add(evt.getEvents().get(x).getColor());
            				}
            			}        
                	}
            	}	
            }
			calendarTable.setDefaultRenderer(calendarTable.getColumnClass(0), new TableRenderer(colorsSet));		
	    }
	    
		public void actionPerformed(ActionEvent e){
			Object src = e.getSource();
			
			if (src.equals(btnPrev)) {
				if (d.getMonthToday() == 0){
					d.setMonthToday(11);
					d.setYearToday(d.getYearToday() - 1);
				}
				else{
					d.setMonthToday(d.getMonthToday() - 1);
				}

				refreshCalendar(d.getMonthToday(), d.getYearToday());
			}
			else if (src.equals(btnNext)) {
				if (d.getMonthToday() == 11){
					d.setMonthToday(0);
					d.setYearToday(d.getYearToday() + 1);
				}
				else{
					d.setMonthToday(d.getMonthToday() + 1);
				}

				refreshCalendar(d.getMonthToday(), d.getYearToday());
			}
			else if (src.equals(cmbYear)) {
				if (cmbYear.getSelectedItem() != null){
					String b = cmbYear.getSelectedItem().toString();
					d.setYearToday(Integer.parseInt(b));
				}

				refreshCalendar(d.getMonthToday(), d.getYearToday());
			}
			else if (src.equals(newEvent)) {
				new EventAdder(); 
			}
			
		}
}
