
/**
 * This class shows a welcome panel with author names 
 * and basic information of the program, to make the 
 * user get started.
 *
 * @author Erica Ma
 * @version 12/11/2018
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;


public class WelcomePanel extends JPanel{
    
    // Instance variables for WelcomePanel
    private JLabel welcome,msg,authors;
    private JButton startButton;
    //private JPanel fill1, fill2, fill3;//fill-in colors
    private JPanel titlePanel, midPanel, msgPanel, buttonPanel;    
    private JLabel imgLabel;; // image Jlable

    /** WelcomePanel constructor
     * no parameters
     */
    public WelcomePanel(JTabbedPane pane){
      		pane.setUI(new BasicTabbedPaneUI() {
			@Override
			protected int calculateTabAreaHeight(int tabPlacement, int horizRunCount, int maxTabHeight) {
				return 0;
			}
			
			@Override
			protected void paintTab(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect) {
			}
		});
        setLayout (new BoxLayout (this, BoxLayout.Y_AXIS)); //Box Layout used for WelcomePanel
      
      Color background = new Color(255,255,204); //use a light yellow color
      setBackground(background); // set the background
      
      // Instantiate titlePanel, fill in inputs message and background color
      titlePanel = new JPanel();
      titlePanel.setBackground(background);
      welcome = new JLabel("<html><p></p><b><font size=6> 4-Year Major Planner<br></font></b></html>");
      titlePanel.add(welcome);//add the welcome JLabel to the panel
      
      // Instantiate midPanel
      midPanel = new JPanel();
      midPanel.setBackground(background);
      authors = new JLabel("<html><p></p><b><font size=5> Designed by Debbie, Kate, and Erica.</font></b></html>");
      midPanel.add(authors); //add the authors JLabel to the panel

      // Create a label that contains the icon image
      JLabel imgLabel = new JLabel();
      imgLabel.setIcon(new ImageIcon("images/wellesley.jpg"));
      //imgLabel.setLocation(50, 100);
      add(imgLabel);
      imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);//center the image

      
      // Instantiate msgPanel
      msgPanel = new JPanel();
      msgPanel.setBackground(background);
      msg = new JLabel("<html><font size = 5>You will have a better <br>idea to plan for your <br>four years in college <br>through our program.<br></font><br> </html>");
      msgPanel.add(msg);
      
      // Instantiate buttonPanel
      buttonPanel = new JPanel();
      buttonPanel.setBackground(background);
      startButton = new JButton("<html><font size =5>Start</font></html>");
      // Add an action listener to go to the next page when "start" is hit
      startButton.addActionListener(new ActionListener(){
          @Override
          public void actionPerformed(ActionEvent evt){
              pane.setSelectedIndex(1);
          }
              
      });
      startButton.setPreferredSize(new Dimension(100, 40));//set the dimesion of the button
      buttonPanel.add(startButton);
      
      // Add JPanels to the WelcomePanel
      add(titlePanel);
      add(midPanel);
      add(msgPanel);
      add(buttonPanel);
    }
}

