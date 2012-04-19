package jspeak;

import java.io.IOException;
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

  public ClipReader() {
    /*
     * TODO Adjust all default values according to
     * https://github.com/BullShark/JSpeak/wiki/EspeakHeader
     * TODO Set min and max constraints accordingly
     */

    // Options get default values to start with
    espeakcmd = new String[]{"espeak", "-v mb-us1", "-a 100", "-p 50", "-s 160", ""};
    str = ""; // Used for toString()
    ps = null;
  }

  public void readIt(String readme) {
    // Set the text to be read
    espeakcmd[5] = readme;

    // Start three processes
    try {
      ps = Runtime.getRuntime().exec(espeakcmd);
    } catch (IOException ex) {
      Logger.getLogger(ClipReader.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  /*
   * Begin all set methods for espeak options
   */
  public boolean setVoice(String voice) {
    //TODO Implement me setVoice()
    //TODO For espeak default voice, try setting to empty string
    return true;
  }

  public boolean setAmplitude(int amp) {
    if(amp > 99 || amp < 0) {
      System.err.println("Cannot set speed to " + amp + ";"
      + "The amplitude must be set between 0 and 20 inclusive.");
      return false;
    }

    espeakcmd[2] = "-a " + new Integer(amp).toString();

    return true;
  }

  public boolean setPitch(int pit) {
    if(pit > 99 || pit < 0) {
      System.err.println("Cannot set speed to " + pit + ";"
      + "The pitch must be set between 0 and 99 inclusive.");
      return false;
    }

    espeakcmd[3] = "-p " + new Integer(pit).toString();

    return true;
  }

  public boolean setSpeed(int wpm) {
    if(wpm > 200 || wpm < 100) {
      System.err.println("Cannot set speed to " + wpm + ";"
      + "The words per minute must be set between 100 and 200 inclusive.");
      return false;
    }

    espeakcmd[4] = "-s " + new Integer(wpm).toString();

    return true;
  }

  public boolean setWordGap(int wg) {
    //TODO Implement me setWordGap()
    //XXX Where is documentation for Word Gap?
    return true;
  }

  @Override
  /*
   * Conceptial view of the command being executed as an Array
   */
  public String toString() {
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