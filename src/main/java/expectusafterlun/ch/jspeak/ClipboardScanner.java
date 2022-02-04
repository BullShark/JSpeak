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

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christopher Lemire {@literal <goodbye300@aim.com>}
 */
public class ClipboardScanner implements Runnable {

	private Transferable transfer;
	private String contents, tempContents;
	private final long POLL_TIME;
	private static ClipReader clipReader;
	private boolean firstRun;
	private static boolean debug = false, quiet = false;

	public ClipboardScanner(boolean debug, boolean quiet) {
		transfer = null;
		contents = "";
		tempContents = "";
		POLL_TIME = 500; // In milliseconds
		clipReader = new ClipReader(debug, quiet);
		firstRun = true;
		this.debug = debug;
		this.quiet = quiet;
	}

	/**
	 * Get the clipboard contents. Compare it with the previous contents. Readit() if the clipboard content has changed.
	 * Loop and do it again, but wait while sleeping this Thread for POLL_TIME milliseconds.
	 */
	@Override
	public void run() {
		while (!Thread.interrupted()) {
			transfer = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);

			tempContents = contents;

			if (transfer != null && transfer.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				try {
					contents = (String) transfer.getTransferData(DataFlavor.stringFlavor);
					if (hasChanged() && !firstRun) {
						clipReader.readIt(contents);
						if(!quiet) { System.out.println("New Content:\n\n" + clipReader.toString() + "\n"); }
					} else {
						tempContents = contents;
						firstRun = false;
					}
				} catch (UnsupportedFlavorException | IOException ex) {
					Logger.getLogger(ClipboardScanner.class.getName()).log(Level.SEVERE, null, ex);
				}
			}

			/*
			 * Wait before checking the clipboard again
			 */
			try {
				Thread.sleep(POLL_TIME);
			} catch (InterruptedException ex) {
				if(debug && !quiet) { Logger.getLogger(ClipboardScanner.class.getName()).log(Level.SEVERE, null, ex); }
				return;
			}
		}
	}

	/**
	 * @return Return whether the clipboard contents have changed
	 */
	public boolean hasChanged() {
		return !(tempContents.equals(contents));
	}

	/**
	 * @return Return the clipboard contents
	 */
	public String getClipboardContents() {
		return contents;
	}

	/**
	 * Useful for calling its methods from the GUI
	 * 
	 * @return A ClipReader object with methods readIt(), replay(), setOptions(), stopPlayBack(), and toString()
	 */
	public static ClipReader getClipReader() {
		return clipReader;
	}
}
