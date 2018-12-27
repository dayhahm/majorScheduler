/**
 * This class generates a schedule for the user after 
 * he/she has valid inputs and chooses the starting class
 *
 * @author Erica Ma
 * @version 12/11/2018
 */
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class ScheduleLayoutPanel extends JPanel {

    // Instance variables of ScheduleLayoutPanel
    private int x;
    JTable table;
    JPanel msg, topPanel, buttonPanel;
    JLabel title;
    JButton quitButton;
    private Schedule mySchedule;
    ArrayList<Course> courses;

    /**
     * ScheduleLayoutPanel Constructor
     * no parameters
     */
    public ScheduleLayoutPanel(JTabbedPane pane, Schedule ms) {
        setLayout (new BoxLayout (this, BoxLayout.Y_AXIS));     
        Color background = new Color(255,255,204);
        setBackground(background);

        topPanel = new JPanel();
        topPanel.setBackground(background);
        title = new JLabel("<html><p></p><b><font size=6> YOUR SCHEDULE<br></font></b></html>");
        topPanel.add(title);
        add(topPanel);

        buttonPanel = new JPanel();
        buttonPanel.setBackground(background);
        quitButton = new JButton("<html><font size =5>Quit</font></b></html>");         
        quitButton.setPreferredSize(new Dimension(150, 40));
        buttonPanel.add(quitButton);
        add(buttonPanel);

        //add a quitbutton if the user wants quit
        quitButton.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent evt){
                    //pane.setSelectedIndex(0);
                    System.exit(0);
                }           
            });

        JLabel gifLabel = new JLabel();//create a label to contain a gif 
        gifLabel.setIcon(new ImageIcon("images/schedulepic.gif"));
        add(gifLabel);
        gifLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.mySchedule = ms;
        msg = this;
        ArrayList<String[]> data = new ArrayList<String[]>();// Data to be displayed in the JTable
        data.add(new String[]{"Course", "Semester", "Year"});
        courses = mySchedule.getCourses();
        int prevSem = 0;
        int sem = 0;
        int sameSem = 0;
        for (int i=0; i<courses.size(); i++) {
            if (prevSem == sem) {
                sameSem++;
            }
            if (sameSem == 2 ||
            ((courses.size()-i)/2 <= 8-sem && (courses.size()-i) % 2 == 0) || 
            Math.random() < .5) {
                sem++;
                sameSem = 0;
            }
            if (i != 0 && courses.get(i).getReq().equals(courses.get(i-1))) {
                sem++;
                sameSem = 0;
            }
            String term = null;
            if (sem % 2 == 0) {
                term = "Fall";
            } 
            else {
                term = "Spring";
            }

            String year = null;
            if (sem < 2) {
                year = "Freshman";
            } 
            else if (sem < 4) {
                year = "Sophomore";
            } 
            else if (sem < 6) {
                year = "Junior";
            } 
            else {
                year = "Senior";
            }

            data.add(new String[]{courses.get(i).getCourseName(), term, year});
            prevSem = sem;
        }

        String[][] dataArray = new String[data.size()][];
        for (int i = 0; i < data.size(); i++) {
            dataArray[i] = data.get(i);
        }
        // Column Names 
        String[] columnNames = {"Name", "Row Number", "Department"};
        // Initializing the JTable 
        table = new JTable(dataArray, columnNames);
        table.setDefaultEditor(Object.class, null);
        table.setRowHeight(30);
        table.setBounds(30, 40, 500, 800);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if (evt.getClickCount() == 1) {
                        final JTable jTable = (JTable) evt.getSource();
                        final int row = jTable.getSelectedRow();
                        final int column = jTable.getSelectedColumn();
                        //final String valueInCell = (String) jTable.getValueAt(row, column);
                        //search for courses based on name
                        //System.out.println(valueInCell);
                        String[] des = courses.get(row-1).getDescription().split(" "); //do manually line breaks, split by space
                        String val = "";
                        val += courses.get(row-1).getCourseName() + "\n";
                        for (int i=0; i<des.length;i++) {
                            val += des[i] + " ";
                            if (i != 0 && i % 10 == 0) {
                                val += "\n";
                            }
                        }

                        //default title and icon
                        JOptionPane.showMessageDialog(msg, val);
                    }
                }
            });
        this.add(table);
    }
}

