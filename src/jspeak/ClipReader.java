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
 * 
 * 
 * A TTS frontend to espeak and mbrola. Features include:
 * 
 * -Scanning the clipboard/watching it for changes and then reading the text aloud
 * 
 * -Toggle button to start and stop and starts the scanning of the clipboard
 * 
 * -Buttons to replay what's on the clipboard and stop playback
 * 
 * -Toggle buttons for switching to a mini gui and another for "Always on top"
 * 
 * -Progress bar for showing activity
 * 
 * -Sliders for Amplitutde (Volume), Word Gap (Delay), Pitch, and Speed (WPM)
 * 
 * -ComboBox for voice selection from installed voices
 * 
 * -Button for resetting to default options (sliders and voice)
 * 
 * History at:
 * https://github.com/BullShark/JSpeak
 *  
 */

package jspeak;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christopher Lemire <christopher.lemire@gmail.com>
 */
public class ClipReader {
  private String[] espeakcmd;
  private String str;
  private Process ps;
  private InputStream in;
  private Scanner scan;
  private boolean debug;

  public ClipReader(boolean debug) {
    // Options get default values to start with
    espeakcmd = new String[]{"espeak", "-v en", "-a 100", "-g 1", "-p 50", "-s 160", ""};
    str = ""; // Used for toString()
    ps = null;
    scan = null;
    this.debug = debug;
  }
  //TODO Read http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html?page=2

  public void readIt(String readme) {
    // Incase espeak is already running, kill it
    stopPlayBack();

    // Set the text to be read
    espeakcmd[6] = readme;

    try {
      /*
       * Removes the space to prevent java from interpretting it
       * As part of the voice filename
       */
      espeakcmd[1] = espeakcmd[1].replace(" ", "");

      ps = Runtime.getRuntime().exec(espeakcmd);
    } catch (IOException ex) {
      Logger.getLogger(ClipReader.class.getName()).log(Level.SEVERE, null, ex);
    }

    if(debug) {
      printPsOutErr(ps);
    }
  }

  public void replay() {
    readIt(espeakcmd[6]);
  }

  public void stopPlayBack() {
    try {
      ps = Runtime.getRuntime().exec("killall espeak"); // *nix only
    } catch (IOException ex) {
      Logger.getLogger(ClipReader.class.getName()).log(Level.SEVERE, null, ex);
    }
    /*
     * Tries to kill espeak
     * If no espeak is running, user doesn't need to see an error
     * 
     * Only enable if you need to see output/error
     */
    if(debug) {
      printPsOutErr(ps);
    }

    /*
     * Prevents a new espeak instance from starting before the current
     * If any espeak process running, is killed
     */
    try {
      ps.waitFor();
    } catch (InterruptedException ex) {
      Logger.getLogger(ClipReader.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void printPsOutErr(Process ps) {
    // Print command output
    in = ps.getInputStream();
    scan = new Scanner(in);
    scan.useDelimiter("\\n");

    while(scan.hasNext()) {
      System.out.println(scan.nextLine());
    }

    System.out.println();

    // Print command output
    in = ps.getErrorStream();
    scan = new Scanner(in);
    scan.useDelimiter("\\n");

    while(scan.hasNext()) {
      System.out.println(scan.nextLine());
    }

    System.out.println();
  }

  /*
   * Begin all set methods for espeak options
   */
  public boolean setVoice(String voice) {
    if(voice.equals("Default")) {
      espeakcmd[1] = "-v en";
    } else {
      espeakcmd[1] = "-v mb-" + voice;
    }
    return true;
  }

  public boolean setAmplitude(int amp) {
    if(amp > 0 && amp <= 200) {
      espeakcmd[2] = "-a " + new Integer(amp).toString();

      return true;
    } else {
      System.err.println("Cannot set amplitude to " + amp + ";\n"
      + "The amplitude must be set between 1 and 200 inclusive.");
      return false;
    }
  }

  public boolean setWordGap(int wg) {
    if(wg > 0 && wg <= 10) {
      espeakcmd[3] = "-g " + new Integer(wg).toString();

      return true;
    } else {
      System.err.println("Cannot set speed to " + wg + ";\n"
      + "The amplitude must be set between 1 and 10 inclusive.");
      return false;
    }
  }

  public boolean setPitch(int pit) {
    if(pit > 0 && pit <= 100) {
    espeakcmd[4] = "-p " + new Integer(pit).toString();

    return true;
    } else {
      System.err.println("Cannot set speed to " + pit + ";\n"
      + "The pitch must be set between 1 and 100 inclusive.");
      return false;
    }
  }

  public boolean setSpeed(int wpm) {
    if(wpm > 0 && wpm <= 200) {
    espeakcmd[5] = "-s " + new Integer(wpm).toString();

    return true;
    } else {
      System.err.println("Cannot set speed to " + wpm + ";"
      + "The words per minute must be set between 1 and 200 inclusive.");
      return false;
    }
  }

  public void setDebug(boolean debug) {
    this.debug = debug;
  }

  /*
   * Conceptial view of the command being executed as an Array
   */
  @Override public String toString() {
    str = "{";

    // Espeak Command
    for(int x = 0; x < espeakcmd.length; x++) {
      str += "\"" + espeakcmd[x] + "\", ";
    }

    str = str.replaceFirst(", $", "");
    str += "}";

    return str;
  }
}
