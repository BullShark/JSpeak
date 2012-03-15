package jspeak;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christopher Lemire <christopher.lemire@gmail.com>
 */
public class ClipReader {
  String[] command;
  Process ps;
  public ClipReader() {
    // Options get default values to start with
    command = new String[]{"espeak", "-a 10", "-p 50", "-s 160", ""};
    ps = null;
  }

  public void readIt(String readme) {
    try {
      command[4] = readme;
      ps = Runtime.getRuntime().exec(command);
    } catch (IOException ex) {
      Logger.getLogger(ClipReader.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public boolean setAmplitude(int amp) {
    if(amp > 99 || amp < 0) {
      System.err.println("Cannot set speed to " + amp + ";"
      + "The amplitude must be set between 0 and 20 inclusive.");
      return false;
    }

    command[1] = "-a " + new Integer(amp).toString();

    return true;
  }

  public boolean setPitch(int pit) {
    if(pit > 99 || pit < 0) {
      System.err.println("Cannot set speed to " + pit + ";"
      + "The pitch must be set between 0 and 99 inclusive.");
      return false;
    }

    command[2] = "-p " + new Integer(pit).toString();

    return true;
  }

  public boolean setSpeed(int wpm) {
    if(wpm > 200 || wpm < 100) {
      System.err.println("Cannot set speed to " + wpm + ";"
      + "The words per minute must be set between 100 and 200 inclusive.");
      return false;
    }

    command[3] = "-s " + new Integer(wpm).toString();

    return true;
  }
}
