
/**
 * This class displays the GUI of our program.
 * @author Erica Ma
 * @version 12/11/2018
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class SchedulerGUI
{
  // Instance variables of SchedulerGUI
  private JPanel basePanel = new JPanel();
  private String gFile, hFile;
  
  public static void main (String[] args) {
    // creates and shows a frame 
    JFrame frame = new JFrame("My Schedule");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    //create a panel, and add it to the frame
    JTabbedPane pane = new JTabbedPane();
    
    pane.addTab ("Welcome", new WelcomePanel(pane));
    pane.addTab ("Info", new InfoPanel(pane));
    
    frame.getContentPane().add(pane);
    
    frame.setTitle("4-Year Major Planner");
    frame.setSize(1200, 600);
    frame.pack();
    frame.setVisible(true);
  }
}


