
/**
 * This class reads one tgf file that represents all the classes
 * and another txt file that containts the major requirements,
 * storing this information and take the user to the next panel.
 *
 * @author Erica Ma
 * @version 12/11/2018
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class InfoPanel extends JPanel {

    // Instance variables of InfoPanel 
    private JButton next;
    private JLabel enterTGF, enterTXT, note;
    private JTextField TGFInput, TXTInput;
    private JPanel TGFPanel, TXTPanel, notePanel, buttonPanel;
    private Font TGFFont, TXTFont;
    String tgfFile, majorFile;

    /** InfoPanel constructor
     * no parameters
     */
    public InfoPanel(JTabbedPane pane) {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); //use BoxLayout
        Color background = new Color(255,255,204); //same light yellow color
        setBackground(background); // set the background color

        // Instantiate TGFPanel
        TGFPanel = new JPanel();
        TGFPanel.setBackground(background);
        enterTGF = new JLabel("<html><p><b><font size = 5>Please type in your tgf filepath:</font></b></p></html>");
        TGFFont = new Font("SansSerif", Font.PLAIN, 15);
        TGFInput = new JTextField(11);
        TGFInput.setFont(TGFFont);
        TGFInput.addActionListener(new TGFListener());
        TGFPanel.add(enterTGF);
        TGFPanel.add(TGFInput);

        // Instantiate TXTPanel
        TXTPanel = new JPanel();
        enterTXT = new JLabel("<html><b><font size = 5>Please type in your major requirements filepath:</font></b></html>");
        TXTFont = new Font("SansSerif", Font.PLAIN, 15);
        TXTInput = new JTextField(10);
        TXTInput.setFont(TXTFont);
        TXTPanel.add(enterTXT);
        TXTInput.addActionListener(new TXTListener());
        TXTPanel.add(TXTInput);
        TXTPanel.setBackground(background);

        // Instantiate notePanel
        notePanel = new JPanel();
        note = new JLabel("<html><font size = 5>After entering all the information, <br>please click 'Next' to get started.<br></font></html>");
        notePanel.add(note);
        notePanel.setBackground(background);

        // Instantiate buttonPanel
        buttonPanel = new JPanel();
        next = new JButton("<html><font size =5>Next</font></html>");
        next.setPreferredSize(new Dimension(150, 40));
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                String tgfFile = TGFInput.getText();
                System.out.println(tgfFile);
                String majorFile = TXTInput.getText();
                System.out.println(majorFile);
//                Schedule mySchedule = new Schedule(tgfFile, majorFile);
                Schedule mySchedule = new Schedule(tgfFile, majorFile);
                pane.addTab ("Preference", new PreferencePanel(pane, mySchedule));
                pane.setSelectedIndex(2);
            }

        });
        buttonPanel.add(next);
        buttonPanel.setBackground(background);

        // Add JPanels to InfoPanel
        add(TGFPanel);
        add(TXTPanel);
        add(notePanel);
        add(buttonPanel);

    }
    
    /**
    *  implements an action listener for the tgf file input.
    */
    private class TGFListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == TGFInput) {
                String depend = TGFInput.getText();
            }
        }
    }

    /**
    *  implements a action listener for the txt file input.
    */
    private class TXTListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == TXTInput) {
                String majorReqs = TXTInput.getText();
            }
        }
    }
}

