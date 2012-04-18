package jspeak;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christopher Lemire <christopher.lemire@gmail.com>
 */
public class ClipReader {
  private String[] espeakcmd, mbrolacmd, aplaycmd;
  private String str;
  private Process p1, p2, p3;
  private Runtime rt;
  private InputStream in;

  public ClipReader() {
    // Options get default values to start with
    espeakcmd = new String[]{"espeak", "-a 10", "-p 50", "-s 160", ""};
    mbrolacmd = new String[]{"mbrola", "mbrola /usr/share/mbrola/us1/us1", "--"};
    aplaycmd  = new String[]{"aplay"}; //TODO aplay cmdline options
    str = "";
    rt = Runtime.getRuntime();
    //TODO espeak --stdout could be useful with mbrola
  }

  public void readIt(String readme) {
    // Set the text to be read
    espeakcmd[4] = readme;

    // Start three processes
    try {
      p1 = rt.exec(espeakcmd);
      p2 = rt.exec(mbrolacmd);
      p3 = rt.exec(aplaycmd);
    } catch (IOException ex) {
      Logger.getLogger(ClipReader.class.getName()).log(Level.SEVERE, null, ex);
    }

    // Start piping
    try {
      in = Piper.pipe(p1, p2, p3);
    } catch (InterruptedException ex) {
      Logger.getLogger(ClipReader.class.getName()).log(Level.SEVERE, null, ex);
    }
      // Show output of last process
      java.io.BufferedReader r = new java.io.BufferedReader(new java.io.InputStreamReader(in));
      String s = null;
    try {
      while ((s = r.readLine()) != null) {
          System.out.println(s);
      }
    } catch (IOException ex) {
      Logger.getLogger(ClipReader.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  /*
   * Begin all set methods for espeak options
   */
  public boolean setAmplitude(int amp) {
    if(amp > 99 || amp < 0) {
      System.err.println("Cannot set speed to " + amp + ";"
      + "The amplitude must be set between 0 and 20 inclusive.");
      return false;
    }

    espeakcmd[1] = "-a " + new Integer(amp).toString();

    return true;
  }

  public boolean setPitch(int pit) {
    if(pit > 99 || pit < 0) {
      System.err.println("Cannot set speed to " + pit + ";"
      + "The pitch must be set between 0 and 99 inclusive.");
      return false;
    }

    espeakcmd[2] = "-p " + new Integer(pit).toString();

    return true;
  }

  public boolean setSpeed(int wpm) {
    if(wpm > 200 || wpm < 100) {
      System.err.println("Cannot set speed to " + wpm + ";"
      + "The words per minute must be set between 100 and 200 inclusive.");
      return false;
    }

    espeakcmd[3] = "-s " + new Integer(wpm).toString();

    return true;
  }

  public boolean setWordGap(int wg) {
    //TODO Implement me
    return true;
  }

  /*
   * Begin all set methods for mbrola options
   */
  //TODO

  /*
   * Begin all set methods for aplay options
   */
  //TODO

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
    str += "}\n{";
    
    // Mbrola Command
    for(int x = 0; x < mbrolacmd.length; x++) {
      str += "\"" + mbrolacmd[x] + "\", ";
    }

    str = str.replaceFirst(", $", "");
    str += "}\n{";

    // Aplay Command
    for(int x = 0; x < aplaycmd.length; x++) {
      str += "\"" + aplaycmd[x] + "\", ";
    }

    str = str.replaceFirst(", $", "");
    str += "}\n";

    return str;
  }
}
