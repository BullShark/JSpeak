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

  public ClipReader() {
    // Options get default values to start with
    espeakcmd = new String[]{"espeak", "-v mb-us1", "-a 100", "-g 1", "-p 50", "-s 160", ""};
    str = ""; // Used for toString()
    ps = null;
    scan = null;
  }
  //TODO Read http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html?page=2

  public void readIt(String readme) {
    // Set the text to be read
    espeakcmd[6] = readme;

    try {
      /*
       * Removes the space to prevent java from interpretting it
       * As part of the voice filename
       */
      espeakcmd[1] = espeakcmd[1].replace(" ", "");

      //TODO How to determine when this process finishes and/or compute the time for completion using Process.waitFor() or checking if Process == null
      //TODO To prevent spawning two espeak processes and for JProgressBar
      ps = Runtime.getRuntime().exec(espeakcmd);
    } catch (IOException ex) {
      Logger.getLogger(ClipReader.class.getName()).log(Level.SEVERE, null, ex);
    }
    printPsOutErr(ps); //TODO Test me
  }

  public void replay() {
    stopPlayBack();
    readIt(espeakcmd[6]);
  }

  public void stopPlayBack() {
    try {
      ps = Runtime.getRuntime().exec("killall espeak"); //TODO *nix only
    } catch (IOException ex) {
      Logger.getLogger(ClipReader.class.getName()).log(Level.SEVERE, null, ex);
    }
    printPsOutErr(ps);
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
    //TODO Implement me setVoice()
    //TODO For espeak default voice, try setting to empty string
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
