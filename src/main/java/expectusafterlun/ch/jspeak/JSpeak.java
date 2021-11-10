/*
 * Copyright (C) 2012 Christopher Lemire <christopher.lemire@gmail.com>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package expectusafterlun.ch.jspeak;

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
 * @author Christopher Lemire {@literal <goodbye300@aim.com>}
 */
public class JSpeak extends JPanel
                    implements ActionListener,
                    ItemListener, ChangeListener {
  private static Thread clipThread, rpThread;
  private final JButton rpButton, stopButton, resetButton;
  private final JToggleButton scanTButton, expandTButton, topTButton;
  private final JComboBox voiceComBox;
  private final JProgressBar readProgress;
  private static ClipReader clipReader;
  private static Replay replayer;
  private static ClipboardScanner clipScan;
  private final JPanel lowerPanel;
  private final JSlider ampSlider, wgSlider, pitSlider, spSlider;
  private static JFrame frame;
  private final ImageIcon scanIcon, rpIcon, stopIcon, expandIcon, retractIcon, topIcon;
  private final MbrolaVoices voices;
  private final String defaultvc;
  private static boolean debug;

  public JSpeak() {
    defaultvc = "Default"; // Used for default espeak voice
    voices = new MbrolaVoices();
    if(voices.getVoices() != null) {
      voiceComBox = new JComboBox(voices.getVoices());
    } else {
      voiceComBox = new JComboBox();
    }
    voiceComBox.addItem(defaultvc);
    voiceComBox.setSelectedItem(defaultvc);
    voiceComBox.addActionListener(this);
    
    // Used for buttons' size
    Dimension size = new Dimension(44, 44);

    /*
     * Create icons
     */
    String location = "/expectusafterlun/ch/jspeak/";

    scanIcon = createImageIcon(location + "scan.png");
    rpIcon = createImageIcon(location + "replay.png");
    stopIcon = createImageIcon(location + "stop.png");
    expandIcon = createImageIcon(location + "expand.png");
    retractIcon = createImageIcon(location + "retract.png");
    topIcon = createImageIcon(location + "ontop.png");

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
     * JSlider Event Handelers
     */
    ampSlider.addChangeListener(this);
    wgSlider.addChangeListener(this);
    pitSlider.addChangeListener(this);
    spSlider.addChangeListener(this);

    /*
     * Buttons
     * 
     * Scan for Changes
     */
    scanTButton = new JToggleButton(scanIcon, false);
    scanTButton.setMaximumSize(size);
    scanTButton.setToolTipText("Scan/Watch for Clipboard Changes");
    scanTButton.addItemListener(this);

    // Replay
    rpButton = new JButton(rpIcon);
    rpButton.setMaximumSize(size);
    rpButton.setToolTipText("Replay");
    rpButton.addActionListener(this);

    // Stop
    stopButton = new JButton(stopIcon);
    stopButton.setMaximumSize(size);
    stopButton.setToolTipText("Stop Playback");
    stopButton.addActionListener(this);

    // Expand
    expandTButton = new JToggleButton(retractIcon, false);
    expandTButton.setMaximumSize(size);
    expandTButton.setToolTipText("Expand/Retract");
    expandTButton.addItemListener(this);

    // Always On Top
    topTButton = new JToggleButton(topIcon, false);
    topTButton.setMaximumSize(size);
    topTButton.setToolTipText("Always On Top");
    topTButton.addItemListener(this);

    // Reset Default Options
    resetButton = new JButton("Reset");
    resetButton.setToolTipText("Reset to Defaults");
    resetButton.addActionListener(this);

    // Show Activity
    readProgress = new JProgressBar();
    readProgress.setPreferredSize(new Dimension(291, 25));

    /*
     * Some components need to be disabled until and only when
     * Scanning of the clipboard is active
     */
    rpButton.setEnabled(false);
    stopButton.setEnabled(false);
    ampSlider.setEnabled(false);
    wgSlider.setEnabled(false);
    pitSlider.setEnabled(false);
    spSlider.setEnabled(false);
    voiceComBox.setEnabled(false);
    resetButton.setEnabled(false);

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

  @Override public void actionPerformed(ActionEvent e) {
    if(e.getSource() == rpButton) {
      replayer = new Replay(clipReader);
      rpThread = new Thread(replayer);
      rpThread.start();
    } else if(e.getSource() == stopButton) {
      clipReader.stopPlayBack();
    } else if(e.getSource() == resetButton) {
      ampSlider.setValue(100);
      clipReader.setAmplitude(100);
      wgSlider.setValue(1);
      clipReader.setWordGap(1);
      pitSlider.setValue(50);
      clipReader.setPitch(50);
      spSlider.setValue(160);
      clipReader.setSpeed(160);
      voiceComBox.setSelectedItem(defaultvc);
      clipReader.setVoice(defaultvc);
    } else if(e.getSource() == voiceComBox) {
      clipReader.setVoice((String)voiceComBox.getSelectedItem());
    }
  }

  /*
   * For the JToggleButton(s)
   */
  @Override public void itemStateChanged(ItemEvent e) {
    if(e.getSource() == scanTButton) {
      if(e.getStateChange() == ItemEvent.SELECTED) {
        clipScan = new ClipboardScanner(debug);
        clipThread = new Thread(clipScan);
        clipReader = ClipboardScanner.getClipReader();
        clipThread.start();
        readProgress.setIndeterminate(true);

        rpButton.setEnabled(true);
        stopButton.setEnabled(true);
        ampSlider.setEnabled(true);
        wgSlider.setEnabled(true);
        pitSlider.setEnabled(true);
        spSlider.setEnabled(true);
        voiceComBox.setEnabled(true);
        resetButton.setEnabled(true);

        clipReader.setAmplitude(ampSlider.getValue());
        clipReader.setWordGap(wgSlider.getValue());
        clipReader.setPitch(pitSlider.getValue());
        clipReader.setSpeed(spSlider.getValue());
        clipReader.setVoice((String)voiceComBox.getSelectedItem());
      } else {
        readProgress.setIndeterminate(false);
        clipThread.interrupt();

        rpButton.setEnabled(false);
        stopButton.setEnabled(false);
        ampSlider.setEnabled(false);
        wgSlider.setEnabled(false);
        pitSlider.setEnabled(false);
        spSlider.setEnabled(false);
        voiceComBox.setEnabled(false);
        resetButton.setEnabled(false);
      }
    } else if(e.getSource() == expandTButton) {
      if(e.getStateChange() == ItemEvent.SELECTED) {
        lowerPanel.setVisible(false);
        frame.pack();
        expandTButton.setIcon(expandIcon);
      } else {
        lowerPanel.setVisible(true);
        frame.pack();
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
  @Override public void stateChanged(ChangeEvent e) {
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
    // Create and set up the window.
    frame = new JFrame("JSpeak");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Add content to the window.
    frame.add(new JSpeak());

    // Display the window.
    frame.pack();
    frame.setVisible(true);
  }

  /**
   * Try to set L&F to GTK+ first.If that fails, set the System L&F. Otherwise, Linux gets the ugly metal L&F.
   * @param args Use -g or --debug for debugging output.
   */
  public static void main(String[] args) {
    
    boolean lookAndFeel;
    try {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        lookAndFeel = true;

    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        System.err.println("GTK+ not found, Using System Look and Feel\n");
        lookAndFeel = false;
    }

    /* Only set the Look and Feel if it has not already successfully been set */
    if(!lookAndFeel) {
        try {
                // Set System L&F
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());

        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex) {
          Logger.getLogger(JSpeak.class.getName()).log(Level.SEVERE, null, ex);
          System.exit(-1);
        }
    }

    if(args.length == 1) {
      if (args[0].equals("-g") || args[0].equals("--debug")) {
        debug = true;
      } else {
        System.err.println("Usage: java -jar JSpeak.jar [-g|--debug]");
        System.exit(-1);
      }
    }

    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() { if(clipReader != null) clipReader.stopPlayBack(); }
    });

    System.out.println("""
		       This software was created by Christopher Lemire <goodbye300@aim.com>
		       Feedback is appreciated!
		       
		       For command output and error use -g or --debug
		       """);

    /*
     * Schedule a job for the event dispatch thread:
     * Creating and showing this application's GUI.
     */
    SwingUtilities.invokeLater(new Runnable() {
      public void run() { createAndShowGUI(); }
    });
  }
}
