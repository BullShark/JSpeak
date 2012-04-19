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
  private char c;
  private Scanner scan;

  public ClipReader() {
    /*
     * TODO Adjust all default values according to
     * https://github.com/BullShark/JSpeak/wiki/EspeakHeader
     * TODO Set min and max constraints accordingly
     */

    // Options get default values to start with
    espeakcmd = new String[]{"espeak", "-v mb-us1", "-a 100", "-g 1", "-p 50", "-s 160", ""};
    str = ""; // Used for toString()
    ps = null;
    scan = null;
  }

  public void readIt(String readme) {
    // Set the text to be read
    espeakcmd[6] = readme;

    // Start three processes
    try {
      /*
       * Removes the space to prevent java from interpretting it
       * As part of the voice filename
       */
      espeakcmd[1] = espeakcmd[1].replace(" ", "");

      ps = Runtime.getRuntime().exec(espeakcmd);

      // Print command output
      in = ps.getInputStream();
      scan = new Scanner(in);
      scan.useDelimiter("\\n");

      //TODO While !interrupted
      while(scan.hasNext()) {
        System.out.println(scan.nextLine());
      }

      System.out.println();
 
      // Print command output
      in = ps.getErrorStream();
      scan = new Scanner(in);
      scan.useDelimiter("\\n");

      //TODO While !interrupted
      while(scan.hasNext()) {
        System.out.println(scan.nextLine());
      }
      
      System.out.println();
     
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

   public boolean setWordGap(int wg) {
    //TODO Implement me setWordGap()
    //XXX Where is documentation for Word Gap?

    //espeakcmd = new String[]{"espeak", "-v mb-us1", "-a 100", "-g 10", "-p 50", "-s 160", ""};
    return true;
  }

 public boolean setPitch(int pit) {
    if(pit > 99 || pit < 0) {
      System.err.println("Cannot set speed to " + pit + ";"
      + "The pitch must be set between 0 and 99 inclusive.");
      return false;
    }

    espeakcmd[4] = "-p " + new Integer(pit).toString();

    return true;
  }

  public boolean setSpeed(int wpm) {
    if(wpm > 200 || wpm < 100) {
      System.err.println("Cannot set speed to " + wpm + ";"
      + "The words per minute must be set between 100 and 200 inclusive.");
      return false;
    }

    espeakcmd[5] = "-s " + new Integer(wpm).toString();

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