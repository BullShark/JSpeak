package jspeak;

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
  private JPanel lowerPanel;
  private JSlider ampSlider, wgSlider, pitSlider, spSlider;

  public JSpeak() {
    setLayout(new MigLayout());

    /*
     * JPanel for the expand/collapse button
     */
    lowerPanel = new JPanel();
    lowerPanel.setLayout(new MigLayout());

    /*
     * Create JSliders
     */
    ampSlider = new JSlider();
    wgSlider = new JSlider();
    pitSlider = new JSlider();
    spSlider = new JSlider();

    /*
     * TODO Set JSlider options
     */

    /*
     * Add JSliders
     */
    lowerPanel.add(ampSlider, "wrap");
    lowerPanel.add(wgSlider, "wrap");
    lowerPanel.add(pitSlider, "wrap");
    lowerPanel.add(spSlider, "wrap");

    /*
     * Buttons
     * TODO Replace all buttons with icons and add tooltips
     * TODO Set LookAndFeel
     */
    scanButton = new JButton("Scan");
    scanButton.addActionListener(this);

    // Play/Pause
    ppButton = new JButton("Play/Pause");
    ppButton.addActionListener(this);

    // Stop
    stopButton = new JButton("Stop");
    stopButton.addActionListener(this);

    // Expand
    expandButton = new JButton("Expand");
    expandButton.addActionListener(this);
    
    //TODO Add Progress bar below buttons
    //If it can't monitor progress, just show progress until completed
    readProgress = new JProgressBar();
    readProgress.setIndeterminate(true);
    readProgress.setPreferredSize(new Dimension(400,25));

    add(scanButton);
    add(ppButton);
    add(stopButton);
    add(expandButton, "wrap");
    add(readProgress, "span, wrap");
    add(lowerPanel);
  }

  @Override
  //React to the user pushing the Change button.
  public void actionPerformed(ActionEvent e) {
    //TODO HashMap getActionEvent()
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

    try {
      // Set System L&F
      UIManager.setLookAndFeel(
        UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException ex) {
      Logger.getLogger(JSpeak.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      Logger.getLogger(JSpeak.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      Logger.getLogger(JSpeak.class.getName()).log(Level.SEVERE, null, ex);
    } catch (UnsupportedLookAndFeelException ex) {
      Logger.getLogger(JSpeak.class.getName()).log(Level.SEVERE, null, ex);
    }

    //Schedule a job for the event dispatch thread:
    //creating and showing this application's GUI.
    SwingUtilities.invokeLater(new Runnable() {
      public void run() { createAndShowGUI(); }
    });
  }
}