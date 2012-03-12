package jspeak;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Christopher Lemire <christopher.lemire@gmail.com>
 */
public class JSpeak extends JPanel
                    implements ActionListener {
  JButton button;

  public JSpeak() {
    setLayout(new MigLayout());
    button = new JButton("Push Me!");
    button.addActionListener(this);
	  button.setBackground(Color.red);
	  add(button);
  }

  @Override
  //React to the user pushing the Change button.
  public void actionPerformed(ActionEvent e) {
    System.out.println("Button Pressed!");
    try {
      Process ps = Runtime.getRuntime().exec( new String[]{"espeak", "You pushed the red DO NOT PUSH ME BUTTON IDIOT!"});
    } catch (IOException ex) {
	    Logger.getLogger(JSpeak.class.getName()).log(Level.SEVERE, null, ex);
		}
//	if(button.isSelected()) {
//	  button.setSelected(false);
//	} else {
//	  button.setSelected(true);
//	}
  }

  /**
   * Create the GUI and show it.  For thread safety,
   * this method should be invoked from the
   * event dispatch thread.
   */
  private static void createAndShowGUI() {
    //Create and set up the window.
    JFrame frame = new JFrame("JSpeak");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //	frame.setPreferredSize(new Dimension(200,200));

    //Add content to the window.
    frame.add(new JSpeak());

    //Display the window.
    frame.pack();
    frame.setVisible(true);
  }

  public static void main(String[] args) {
    Runnable runnable = new ClipboardScanner();
    Thread thread = new Thread(runnable);
    thread.start();

    //Schedule a job for the event dispatch thread:
    //creating and showing this application's GUI.
    SwingUtilities.invokeLater(new Runnable() {
    	public void run() { createAndShowGUI(); }
    });
  }
}