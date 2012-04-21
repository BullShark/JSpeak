package jspeak;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
  private JButton scanButton, rpButton, stopButton, expandButton;
  private JProgressBar readProgress;
  private static ClipReader clipReader;
  private JPanel lowerPanel;
  private JSlider ampSlider, wgSlider, pitSlider, spSlider;
  private boolean expanded;

  public JSpeak() {
//    setLayout(new MigLayout("",    // Layout Constraints
//                            "",    // Column Constraints
//                            ""));  // Row Constraints
    setLayout(new MigLayout("hidemode 2, nogrid"));

    expanded = true;

    /*
     * JPanel for the expand/collapse button
     */
    lowerPanel = new JPanel();
    lowerPanel.setLayout(new MigLayout("hidemode 2")); //FIXME; Not working

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
    lowerPanel.add(new JLabel("Amplitude"));
    lowerPanel.add(ampSlider, "wrap");
    lowerPanel.add(new JLabel("Word Gap"));
    lowerPanel.add(wgSlider, "wrap");
    lowerPanel.add(new JLabel("Pitch"));
    lowerPanel.add(pitSlider, "wrap");
    lowerPanel.add(new JLabel("Speed"));
    lowerPanel.add(spSlider, "wrap");

    /*
     * Buttons
     * TODO Use MissingIcon.java
     */
//    scanButton = new JButton(new ImageIcon(getClass().getResource("/jspeak/resources/scan.png")));
    scanButton = new JButton(); //TODO Need blue matching button
    scanButton.setToolTipText("Scan/Watch for Clipboard Changes");
    scanButton.addActionListener(this);

    // Replay
    rpButton = new JButton(new ImageIcon(getClass().getResource("/jspeak/resources/replay.png")));
    rpButton.setToolTipText("Replay");
    rpButton.addActionListener(this);

    // Stop
    stopButton = new JButton(new ImageIcon(getClass().getResource("/jspeak/resources/stop.png")));
    stopButton.setToolTipText("Stop Playback");
    stopButton.addActionListener(this);

    // Expand
//    expandButton = new JButton(new ImageIcon(getClass().getResource("/jspeak/resources/retract.png"))); //TODO Need blue matching button
    expandButton = new JButton();
    expandButton.setToolTipText("Expand/Retract");
    expandButton.addActionListener(this);
    
    //TODO If it can't monitor progress, just show progress until completed
    readProgress = new JProgressBar();
    readProgress.setPreferredSize(new Dimension(400,25)); //TODO Correct Demension size

    add(scanButton);
    add(rpButton);
    add(stopButton);
    add(expandButton, "wrap"); //TODO Read docs on wrap; Seems to be setting column restraints for all columns
    add(readProgress , "wrap"); //FIXME Causing alignment issues with JButtons
    add(lowerPanel);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == scanButton) {
      readProgress.setIndeterminate(true);
    } else if(e.getSource() == rpButton) {
      readProgress.setIndeterminate(false);
    } else if(e.getSource() == stopButton) {

    } else if(e.getSource() == expandButton) {
      if(expanded) {
        lowerPanel.setVisible(false);
        lowerPanel.setEnabled(false);
        this.setSize(412, 61); //FIXME Fails
        this.validate();
        System.out.println(this.getSize());
//java.awt.Dimension[width=412,height=61]
//        expandButton.setIcon(new ImageIcon(getClass().getResource("/jspeak/resources/expand.png")));
        expanded = false;
      } else {
        lowerPanel.setVisible(true);
        lowerPanel.setEnabled(true);
//        expandButton.setIcon(new ImageIcon(getClass().getResource("/jspeak/resources/retract.png")));
        expanded = true;
      }
    } else if(e.getSource() == ampSlider) {

    } else if(e.getSource() == wgSlider) {

    } else if(e.getSource() == pitSlider) {

    } else if(e.getSource() == spSlider) {

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