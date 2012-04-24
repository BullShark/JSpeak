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

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

/**
 *
 * @author Christopher Lemire <christopher.lemire@gmail.com>
 */
public class MbrolaVoices {
  private File espeakDir, mbrolaDir;
  private final FilenameFilter ff;
  private File[] fileArr;
  private String[] fstrArr;

  public MbrolaVoices() {
    //TODO Read http://docs.oracle.com/javase/tutorial/essential/io/dirs.html
    //TODO Read http://docs.oracle.com/javase/tutorial/essential/io/find.html
    //TODO Get voices and store as an array
    
    ff = new FilenameFilter() {
      @Override public boolean accept(File dir, String name) {
        return name.matches("[a-z]{2}[1-9]");
      }
    };

    mbrolaDir = new File("/usr/share/espeak-data/voices/mbrola");
    espeakDir = new File("/usr/share/mbrola");

    /*
     * Do not need to handle an arbitrary number of directories
     * Documentation states espeak looks under two specific directories
     * For mbrola voices
     */
    if(espeakDir.exists() && mbrolaDir.exists()) {
      fileArr = concatAll(espeakDir.listFiles(ff), mbrolaDir.listFiles(ff));
    } else if(espeakDir.exists()) {
      fileArr = espeakDir.listFiles(ff);
    } else if(mbrolaDir.exists()) {
      fileArr = mbrolaDir.listFiles(ff);
    } else {
      System.err.println("Mbrola voices directory not found. Install espeak.\n"
              + "If espeak is installed, and you still see this message, "
              + "please report a bug at\n"
              + "https://github.com/BullShark/JSpeak/issues");
      fileArr = null;
    }

    if(fileArr != null) {
      fstrArr = new String[fileArr.length];
      for(int j=0; j<fileArr.length; j++) {
        fstrArr[j] = fileArr[j].getName();
      }
    }
  }

  /*
   * JComboBox's constructor takes a String Array
   */
  public String[] getVoices() {
    return fstrArr;
  }

  public static <T> T[] concatAll(T[] first, T[]... rest) {
    int totalLength = first.length;
    for (T[] array : rest) {
      totalLength += array.length;
    }
    T[] result = Arrays.copyOf(first, totalLength);
    int offset = first.length;
    for (T[] array : rest) {
      System.arraycopy(array, 0, result, offset, array.length);
      offset += array.length;
    }
    return result;
  }
}