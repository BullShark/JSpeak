/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
    command = new String[]{""};
    ps = null;
  }

  public void readIt(String readme) {
    try {
      Process ps = Runtime.getRuntime().exec(command);
    } catch (IOException ex) {
      Logger.getLogger(ClipReader.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
