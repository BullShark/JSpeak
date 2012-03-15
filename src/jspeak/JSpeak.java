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
  private JButton button;
  private static ClipReader clipReader;
//  private JRadioButton scanB, noScanB;
//  private JToggleButton toggleB;
//  private ButtonGroup group;

  public JSpeak() {
    setLayout(new MigLayout());

    // Radio buttons
//    scanB = new JRadioButton("Clipboard Scan");
//    noScanB = new JRadioButton("Stop Scanning");

    // Radio button group
//    group = new ButtonGroup();
//    group.add(scanB);
//    group.add(noScanB);

    // Toggle button
//    toggleB = new JToggleButton("Scan Clipboard", true);
//    toggleB.setBackground(Color.GREEN);
    
    // Buttons
    button = new JButton("Push Me!");
    button.addActionListener(this);
    button.setBackground(Color.RED);

    //TODO Add Progress bar below buttons

    // Add gui components
//    add(scanB);
//    add(noScanB, "wrap");
//    add(toggleB);
    add(button);
  }

  @Override
  //React to the user pushing the Change button.
  public void actionPerformed(ActionEvent e) {
    System.out.println("Button Pressed!");
    clipReader.setSpeed(200);
    try {
      Runtime.getRuntime().exec( new String[]{"espeak", "You pushed the red DO NOT PUSH ME BUTTON IDIOT!"});
    } catch (IOException ex) {
      Logger.getLogger(JSpeak.class.getName()).log(Level.SEVERE, null, ex);
    }
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
    //  frame.setPreferredSize(new Dimension(200,200));

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

    clipReader = ClipboardScanner.getClipReader();

    //Schedule a job for the event dispatch thread:
    //creating and showing this application's GUI.
    SwingUtilities.invokeLater(new Runnable() {
      public void run() { createAndShowGUI(); }
    });
  }
}