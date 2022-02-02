/*
* Copyright (C) 2012 Christopher Lemire <goodbye300@aim.com>
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
 */
package expectusafterlun.ch.jspeak;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christopher Lemire {@literal <goodbye300@aim.com>}
 */
public class ClipReader {

	private final String[] ESPEAKCMD;
	private String str;
	private Process ps;
	private InputStream in;
	private Scanner scan;
	private static boolean debug = false, quiet = false;

	/**
	 * Sets up the espeak command String Array containing the text to speak and the espeak command options and sets other global vars.
	 * 
	 * @param debug Verbose debugging output
	 * @param quiet Silence all output except critical errors.
	 */
	public ClipReader(boolean debug, boolean quiet) {
		// Options get default values to start with
		ESPEAKCMD = new String[]{"espeak", "-v en", "-a 100", "-g 1", "-p 50", "-s 160", ""};
		str = ""; // Used for toString()
		ps = null;
		scan = null;
		this.debug = debug;
		this.quiet = quiet;
	}

	/**
	 * Read aloud the text in "readme".
	 *
	 * @param readme The string of text to be read
	 */
	public void readIt(String readme) {
		// Incase espeak is already running, kill it
		// Unless it's not initialized
		if (ps != null) {
			stopPlayBack();
		}

		// Set the text to be read
		ESPEAKCMD[6] = readme;

		try {
			/*
			 * Removes the space to prevent java from interpretting it
			 * As part of the voice filename
			 */
			ESPEAKCMD[1] = ESPEAKCMD[1].replace(" ", "");

			ps = Runtime.getRuntime().exec(ESPEAKCMD);
		} catch (IOException ex) {
			Logger.getLogger(ClipReader.class.getName()).log(Level.SEVERE, null, ex);
		}

		if (debug && !quiet) {
			printPsOutErr(ps);
		}
	}

	/**
	 * Do a replay of the last thing spoken.
	 */
	public void replay() {
		readIt(ESPEAKCMD[6]);
	}

	/**
	 * Stop the playback.
	 */
	public void stopPlayBack() {
		if(ps != null) { ps.destroy(); }
		/*
		 * Tries to kill espeak
		 * Only shows an error if espeak fails to stop
		 * 
		 * Only enable if you need to see output/error
		 */
		if (debug && !quiet) {
			printPsOutErr(ps);
		}

		/*
		 * Prevents a new espeak instance from starting before the current
		 * If any espeak process running, is killed
		 */
		try {
			if(ps != null) { ps.waitFor();}
		} catch (InterruptedException ex) {
			Logger.getLogger(ClipReader.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Print the error and output streams of ps. Return from this function if not debug or quiet.
	 * 
	 * @param ps The process used for printing stream
	 */
	public void printPsOutErr(Process ps) {
		if(ps == null) { return; }
		if(!debug || quiet) { return; }
		
		// Print command output
		in = ps.getInputStream();
		scan = new Scanner(in);
		scan.useDelimiter("\\n");

		while (scan.hasNext()) {
			System.out.println(scan.nextLine());
		}

		System.out.println();

		// Print command output
		in = ps.getErrorStream();
		scan = new Scanner(in);
		scan.useDelimiter("\\n");

		while (scan.hasNext()) {
			System.out.println(scan.nextLine());
		}

		System.out.println();
	}

	/**
	 * Begin all set methods for espeak options
	 *
	 * @param voice The voice to be set
	 * @return Whether setting the voice was successful
	 */
	public boolean setVoice(String voice) {
		if (voice.equals("Default")) {
			ESPEAKCMD[1] = "-v en";
		} else {
			ESPEAKCMD[1] = "-v mb-" + voice;
		}
		return true;
	}

	/**
	 * Sets the amplitude.
	 *
	 * @param amp The amplitude value between 0 and 200 (included)
	 * @return Whether setting the amplitude was successful
	 */
	public boolean setAmplitude(int amp) {
		if (amp > 0 && amp <= 200) {
			ESPEAKCMD[2] = "-a " + amp;

			return true;
		} else {
			System.err.println("Cannot set amplitude to " + amp + ";\n"
				+ "The amplitude must be set between 1 and 200 inclusive.");
			return false;
		}
	}

	/**
	 * Set the Word Gap, the gap between spoken words.
	 *
	 * @param wg The Word Gap to be set
	 * @return Whether setting the Word Gap was successful
	 */
	public boolean setWordGap(int wg) {
		if (wg > 0 && wg <= 10) {
			ESPEAKCMD[3] = "-g " + wg;

			return true;
		} else {
			System.err.println("Cannot set speed to " + wg + ";\n"
				+ "The amplitude must be set between 1 and 10 inclusive.");
			return false;
		}
	}

	/**
	 * Set the pitch of the spoken voice.
	 *
	 * @param pit Valid pit values are between 0 and 100 (included).
	 * @return true if success setting the speed, false otherwise
	 */
	public boolean setPitch(int pit) {
		if (pit > 0 && pit <= 100) {
			ESPEAKCMD[4] = "-p " + pit;

			return true;
		} else {
			System.err.println("Cannot set speed to " + pit + ";\n"
				+ "The pitch must be set between 1 and 100 inclusive.");
			return false;
		}
	}

	/**
	 * Set the speech speed in Words Per Minute.
	 *
	 * @param wpm Words Per Minute
	 * @return true if success setting the speed, false otherwise
	 */
	public boolean setSpeed(int wpm) {
		if (wpm > 0 && wpm <= 200) {
			ESPEAKCMD[5] = "-s " + wpm;

			return true;
		} else {
			System.err.println("Cannot set speed to " + wpm + ";"
				+ "The words per minute must be set between 1 and 200 inclusive.");
			return false;
		}
	}

	/**
	 * Set debug to true or false for this class.
	 * 
	 * @param debug Whether or not to be verbose with messages
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * Set quiet to true or false for this class.
	 * 
	 * @param quiet Whether or not to be quiet with messages
	 */
	public void setQuiet(boolean quiet) {
		this.quiet = quiet;
	}

	/*
	 * Conceptial view of the command being executed as an Array
	 */
	@Override
	public String toString() {
		str = "{";

		// Espeak Command
		for (String s : ESPEAKCMD) {
			str += "\"" + s + "\", ";
		}

		str = str.replaceFirst(", $", "");

		str += "}";

		return str;
	}
}
