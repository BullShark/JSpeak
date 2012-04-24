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
