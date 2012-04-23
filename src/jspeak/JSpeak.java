package jspeak;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Christopher Lemire <christopher.lemire@gmail.com>
 */
public class JSpeak extends JPanel
                    implements ActionListener,
                    ItemListener, ChangeListener {
  private JButton rpButton, stopButton, resetButton;
  private JToggleButton scanTButton, expandTButton, topTButton;
  private JComboBox voiceComBox;
  private JProgressBar readProgress;
  private static ClipReader clipReader;
  private JPanel lowerPanel;
  private JSlider ampSlider, wgSlider, pitSlider, spSlider;
  private static JFrame frame;
  private ImageIcon scanIcon, rpIcon, stopIcon, expandIcon, retractIcon;
  private ImageIcon topIcon;

  public JSpeak() {
    voiceComBox = new JComboBox( new String[] {"Default", });

    /*
     * Create icons
     */
    String loc = "/jspeak/resources/";
    scanIcon = createImageIcon(loc + "scan.png");
    rpIcon = createImageIcon(loc + "replay.png");
    stopIcon = createImageIcon(loc + "stop.png");
    expandIcon = createImageIcon(loc + "expand.png");
    retractIcon = createImageIcon(loc + "retract.png");
    topIcon = createImageIcon(loc + "ontop.png");

    /*
     * Setting hidemode 2 helps prevent the
     * JProgressBar from being resized
     */
    setLayout(new MigLayout("hidemode 2, nogrid"));

    /*
     * JPanel for the expand/collapse button
     */
    lowerPanel = new JPanel();
    lowerPanel.setLayout(new MigLayout("hidemode 2"));

    /*
     * Create JSliders
     */
    ampSlider = new JSlider(JSlider.HORIZONTAL, 1, 200, 100);
    wgSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
    pitSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, 50);
    spSlider = new JSlider(JSlider.HORIZONTAL, 1, 200, 160);

    /*
     * Set JSlider options
     * TODO More JSlider options, check wiki implements link
     */

//Turn on labels at major tick marks.
//framesPerSecond.setMajorTickSpacing(10);
//framesPerSecond.setMinorTickSpacing(1);
//framesPerSecond.setPaintTicks(true);
//framesPerSecond.setPaintLabels(true);

    /*
     * JSlider Event Handelers
     */
    ampSlider.addChangeListener(this);
    wgSlider.addChangeListener(this);
    pitSlider.addChangeListener(this);
    spSlider.addChangeListener(this);

    /*
     * Buttons
     */
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
    expandTButton = new JToggleButton(retractIcon, false);
    expandTButton.setToolTipText("Expand/Retract");
    expandTButton.addItemListener(this);

    // Always On Top
    topTButton = new JToggleButton(topIcon, false);
    topTButton.setToolTipText("Always On Top");
    topTButton.addItemListener(this);

    // Reset Default Options
    resetButton = new JButton("Reset");
    resetButton.setToolTipText("Reset to Defaults");
    resetButton.addActionListener(this);

    //TODO If it can't monitor progress, just show progress until completed
    // Show Activity
    readProgress = new JProgressBar();
    readProgress.setPreferredSize(new Dimension(290, 25));

    /*
     * Add Bottom Panel Components
     */
    lowerPanel.add(new JLabel("Amplitude"));
    lowerPanel.add(ampSlider, "wrap");
    lowerPanel.add(new JLabel("Word Gap"));
    lowerPanel.add(wgSlider, "wrap");
    lowerPanel.add(new JLabel("Pitch"));
    lowerPanel.add(pitSlider, "wrap");
    lowerPanel.add(new JLabel("Speed"));
    lowerPanel.add(spSlider, "wrap");
    lowerPanel.add(new JLabel("Voice"));
    lowerPanel.add(voiceComBox, "split 2");
    lowerPanel.add(resetButton);

    add(scanTButton);
    add(rpButton);
    add(stopButton);
    add(expandTButton);
    add(topTButton, "wrap");
    add(readProgress , "wrap, center");
    add(lowerPanel);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == rpButton) {
      System.err.println("Implement me!"); //TODO Thread code here
    } else if(e.getSource() == stopButton) {
      System.out.println(frame.getSize());
      System.err.println("Implement me!"); //TODO Thread code here
    } else if(e.getSource() == resetButton) {
      this.ampSlider.setValue(100);
      clipReader.setAmplitude(100);
      this.wgSlider.setValue(1);
      clipReader.setWordGap(1);
      this.pitSlider.setValue(50);
      clipReader.setPitch(50);
      this.spSlider.setValue(160);
      clipReader.setSpeed(160);
      //TODO Reset Voice in Combo Box
    }
  }

  /*
   * For the JToggleButton(s)
   */
  @Override
  public void itemStateChanged(ItemEvent e) {
    if(e.getSource() == scanTButton) {
      if(e.getStateChange() == ItemEvent.SELECTED) {
        readProgress.setIndeterminate(true); //TODO DeleteMe
      } else {
        readProgress.setIndeterminate(false); //TODO DeleteMe
      }
      System.err.println("Implement me!"); //TODO Thread code here
    } else if(e.getSource() == expandTButton) {
      if(e.getStateChange() == ItemEvent.SELECTED) {
        lowerPanel.setVisible(false);
        frame.setSize(new Dimension(323, 143));
        expandTButton.setIcon(expandIcon);
      } else {
        lowerPanel.setVisible(true);
        frame.setSize(new Dimension(323, 378));
        expandTButton.setIcon(retractIcon);
      }
    } else if(e.getSource() == topTButton) {
      if(e.getStateChange() == ItemEvent.SELECTED) {
        frame.setAlwaysOnTop(true);
      } else {
        frame.setAlwaysOnTop(false);
      }
    }
  }

  /*
   * For JSlider(s)
   */
  @Override
  public void stateChanged(ChangeEvent e) {
    JSlider source = (JSlider)e.getSource();
    if(!source.getValueIsAdjusting()) {
      int value = source.getValue();

      if(source == ampSlider) {
        clipReader.setAmplitude(value);
      } else if(source == wgSlider) {
        clipReader.setWordGap(value);
      } else if(source == pitSlider) {
        clipReader.setPitch(value);
      } else if(source == spSlider) {
        clipReader.setSpeed(value);
      }
    }
  }

    private static ImageIcon createImageIcon(String path) {
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