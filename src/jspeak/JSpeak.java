package jspeak;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Christopher Lemire <christopher.lemire@gmail.com>
 */
public class JSpeak extends JPanel
                    implements ActionListener,
                    ItemListener {
  private JButton scanButton, rpButton, stopButton, expandButton;
  private JToggleButton scanTButton, expandTButton;
  //TODO JCheckBox
  //TODO JComboBox
  private JProgressBar readProgress;
  private static ClipReader clipReader;
  private JPanel lowerPanel;
  private JSlider ampSlider, wgSlider, pitSlider, spSlider;
  private boolean expanded;
  private static JFrame frame;
  private JLabel logoLabel;
  private ImageIcon scanIcon, rpIcon, stopIcon, expandIcon, retractIcon;

  public JSpeak() {
    /*
     * Create icons
     * 
     * protected static ImageIcon createImageIcon(String path) {
     */
    String loc = "/jspeak/resources/";
    scanIcon = createImageIcon(loc + "scan.png");
    rpIcon = createImageIcon(loc + "replay.png");
    stopIcon = createImageIcon(loc + "stop.png");
    expandIcon = createImageIcon(loc + "expand.png");
    retractIcon = createImageIcon(loc + "retract.png");

    /*
     * Setting hidemode 2 helps prevent the
     * JProgressBar from being resized
     */
    setLayout(new MigLayout("hidemode 2, nogrid"));

//    expanded = true;

    /*
     * JPanel for the expand/collapse button
     */
    lowerPanel = new JPanel();
    lowerPanel.setLayout(new MigLayout("hidemode 2"));

    /*
     * Create JSliders
     * TODO More JSlider options, check wiki implements link
     * TODO ChangeListener
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
     */
    // Scan for Changes
//    scanButton = new JButton(new ImageIcon(getClass().getResource("/jspeak/resources/scan.png")));
//    scanButton.setToolTipText("Scan/Watch for Clipboard Changes");
//    scanButton.addActionListener(this);

    // Scan for Changes
    scanTButton = new JToggleButton(scanIcon, false);
    scanTButton.setToolTipText("Scan/Watch for Clipboard Changes");
    scanTButton.addItemListener(this);

    // Replay
    rpButton = new JButton(rpIcon);
    rpButton.setToolTipText("Replay");
    rpButton.addActionListener(this);

    // Stop
    stopButton = new JButton(stopIcon);
    stopButton.setToolTipText("Stop Playback");
    stopButton.addActionListener(this);

    // Expand
//    expandButton = new JButton(expandIcon);
//    expandButton.setToolTipText("Expand/Retract");
//    expandButton.addActionListener(this);
    
    // Expand
    expandTButton = new JToggleButton(expandIcon, false);
    expandTButton.setToolTipText("Expand/Retract");
    expandTButton.addItemListener(this);

    //TODO If it can't monitor progress, just show progress until completed
    // Show Activity
    readProgress = new JProgressBar();
    readProgress.setPreferredSize(new Dimension(290, 25));


//    add(scanButton);
    add(scanTButton);
    add(rpButton);
    add(stopButton);
//    add(expandButton, "wrap");
    add(expandTButton, "wrap");
    add(readProgress , "wrap, center");
    add(lowerPanel);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == scanButton) {
      frame.setAlwaysOnTop(true); //TODO Make checkbox for this
      readProgress.setIndeterminate(true);
    } else if(e.getSource() == rpButton) {
      frame.setAlwaysOnTop(false); //TODO Make checkbox for this
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

  /*
   * For the JToggleButton(s)
   */
  @Override
  public void itemStateChanged(ItemEvent e) {
    if(e.getSource() == scanTButton) {
      if(e.getStateChange() == ItemEvent.SELECTED) {
        //TODO Thread code here
        readProgress.setIndeterminate(true); //TODO DeleteMe
      } else {
        readProgress.setIndeterminate(false); //TODO DeleteMe
      }

    } else if(e.getSource() == expandTButton) {
      if(e.getStateChange() == ItemEvent.SELECTED) {
        lowerPanel.setVisible(false);
        frame.setSize(new Dimension(323, 143));
        System.out.println(JSpeak.class.getResource("/jspeak/resources/")); //Testing: Pointing to correct directory
        System.out.println(JSpeak.class.getResource(".")); //Testing: Pointing to correct directory
//        expandButton.setIcon(expandIcon); // Causing NullPointerException
        expandButton.setIcon(new ImageIcon(getClass().getResource("/jspeak/resources/expand.png"))); // Also causing NullPointerException
      } else {
        lowerPanel.setVisible(true);
        frame.setSize(new Dimension(323, 347));
//        expandButton.setIcon(retractIcon);
      }
    }
  }

  /*
   * TODO Test and Use createImageIcon
   * TODO Use one obj instead of new ImageIcon() for images
   * TODO Implement to use MissingIcon.java
   */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = JSpeak.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
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