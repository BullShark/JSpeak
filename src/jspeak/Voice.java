/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jspeak;

import java.io.File;

/**
 *
 * @author Christopher Lemire <christopher.lemire@gmail.com>
 */
public class Voice {
  private File f;
  public Voice() {
    //TODO Read http://docs.oracle.com/javase/tutorial/essential/io/dirs.html
    //TODO Read http://docs.oracle.com/javase/tutorial/essential/io/find.html
    f = new File("/usr/share/espeak-data/voices/mbrola");
    //TODO Get voices and store as an array
    f = new File("/usr/share/mbrola");
    //TODO Get voices and add to the array
  }

  public String[] getVoices() {
    return null;
  }
}
