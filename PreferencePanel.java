
/**
 * This class contains a dropdown menu for the user to choose a starting
 * course before generating the schedule.
 *
 * @author Erica Ma
 * @version 12/11/2018
 */
import javafoundations.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class PreferencePanel extends JPanel{
    // Instance variables for PreferencePanel components
    private Schedule mySchedule;
    private JPanel titlePanel, middlePanel, generatePanel;
    private JLabel note;
    private JTextField startingPoint;
    private JComboBox SPCombo;
    private JButton generateButton;

    /** PreferencePanel constructor 
     * @param pane, represents a JTabbedpane
     * @param ms, represents a Schedule
     */
    public PreferencePanel(JTabbedPane pane, Schedule ms){
        this.mySchedule = ms;
        Color background = new Color(255,255,204);//use the same light yellow color
        setBackground(background);
        setLayout (new BoxLayout (this, BoxLayout.Y_AXIS)); // uses a BoxLayout

        // Instantiate titlePanel
        titlePanel = new JPanel();
        titlePanel.setBackground(background);
        note = new JLabel ("<html><p></p><p></p><b><font size = 5>What is your starting point? </font></b></html>");
        titlePanel.add(note);

        // Instantiate buttonPanel
        middlePanel = new JPanel();
        middlePanel.setBackground(background);
        startingPoint = new JTextField(5);
        ArrayList<String> options = new ArrayList<>();//create an arraylists containing all the possible starting points
        options.add("<html><font size =4>-----------Choose One-----------</font></b></html>");
        ArrayList<Course> courses = mySchedule.findSkippableClasses();//
        for (int i=0; i<courses.size(); i++){
            options.add(courses.get(i).getCourseName());
        }

        SPCombo = new JComboBox(options.toArray());
        SPCombo.setAlignmentX(CENTER_ALIGNMENT);
        middlePanel.add(SPCombo);

        // Instantiate homePanel, set its background and add its respective components
        generatePanel = new JPanel();
        generatePanel.setBackground(background);
        generateButton = new JButton("<html><font size =5>Generate</font></html>"); 
        generateButton.setPreferredSize(new Dimension(150, 40));
        generatePanel.add(generateButton); 
        generateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    Course course = null;
                    String selection = SPCombo.getSelectedItem().toString();
                    Course skip = new Course(selection, "");
                    mySchedule.fillAllReqs();
                    mySchedule.skipClasses(skip);
                    mySchedule.createASchedule(skip);
                    System.out.println(mySchedule);
                    pane.addTab("Schedule", new ScheduleLayoutPanel(pane, mySchedule));
                    pane.setSelectedIndex(3);
                    return;
                }
            });

        //Add JPanels to the main panel
        add(titlePanel);
        add(middlePanel);
        add(generatePanel);
    }
}

