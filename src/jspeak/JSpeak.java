package jspeak;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Christopher Lemire <christopher.lemire@gmail.com>
 */
public class JSpeak extends JPanel
                    implements ActionListener {
  private JButton scanButton, ppButton, stopButton, expandButton;
  private JProgressBar readProgress;
  private static ClipReader clipReader;

  public JSpeak() {
    setLayout(new MigLayout());

    /*
     * Buttons
     * TODO Replace all buttons with icons and add tooltips
     * TODO Set LookAndFeel
     */
    scanButton = new JButton("Scan");
    scanButton.addActionListener(this);
    scanButton.setBackground(Color.RED);

    // Play/Pause
    ppButton = new JButton("Play/Pause");
    ppButton.addActionListener(this);
    ppButton.setBackground(Color.RED);

    // Stop
    stopButton = new JButton("Stop");
    stopButton.addActionListener(this);
    stopButton.setBackground(Color.RED);

    // Expand
    expandButton = new JButton("Expand");
    expandButton.addActionListener(this);
    expandButton.setBackground(Color.RED);
    
    //TODO Add Progress bar below buttons
    //If it can't monitor progress, just show progress until completed
    readProgress = new JProgressBar();
    readProgress.setIndeterminate(true);
    readProgress.setPreferredSize(new Dimension(400,25));

    add(scanButton);
    add(ppButton);
    add(stopButton);
    add(expandButton, "wrap");
    add(readProgress, "span");
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