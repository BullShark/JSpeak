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
  private static JFrame frame;

  public JSpeak() {
    // Setting hidemode 2 helps prevent the JProgressBar from being resized
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
    ampSlider = new JSlider(JSlider.HORIZONTAL, 1, 200, 100);
    wgSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
    pitSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, 50);
    spSlider = new JSlider(JSlider.HORIZONTAL, 1, 200, 160);

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
    scanButton = new JButton(new ImageIcon(getClass().getResource("/jspeak/resources/scan.png")));
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
    expandButton = new JButton(new ImageIcon(getClass().getResource("/jspeak/resources/retract.png"))); //TODO Need blue matching button
    expandButton.setToolTipText("Expand/Retract");
    expandButton.addActionListener(this);
    
    //TODO If it can't monitor progress, just show progress until completed
    readProgress = new JProgressBar();
    readProgress.setPreferredSize(new Dimension(290,25));

    add(scanButton);
    add(rpButton);
    add(stopButton);
    add(expandButton, "wrap");
    add(readProgress , "wrap, center");
    add(lowerPanel);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == scanButton) {
      readProgress.setIndeterminate(true);
    } else if(e.getSource() == rpButton) {
      readProgress.setIndeterminate(false);
    } else if(e.getSource() == stopButton) {
      System.out.println(frame.getSize());
    } else if(e.getSource() == expandButton) {
      if(expanded) {
        lowerPanel.setVisible(false);
        frame.setSize(new Dimension(323, 143));
        expandButton.setIcon(new ImageIcon(getClass().getResource("/jspeak/resources/expand.png")));
        expanded = false;
      } else {
        lowerPanel.setVisible(true);
        frame.setSize(new Dimension(323, 347));
        expandButton.setIcon(new ImageIcon(getClass().getResource("/jspeak/resources/retract.png")));
        expanded = true;
      }
    } else if(e.getSource() == ampSlider) { //TODO Use these for reset to default button

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
    frame = new JFrame("JSpeak");
    frame.setAlwaysOnTop(true); //TODO Make checkbox for this
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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