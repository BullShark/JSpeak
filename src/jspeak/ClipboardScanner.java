package jspeak;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christopher Lemire <christopher.lemire@gmail.com>
 */
public class ClipboardScanner implements Runnable {
  private Transferable transfer;
  private String contents, tempContents;
  private boolean changed;
  private long pollTime;
  private static ClipReader clipReader;

  public ClipboardScanner() {
    transfer = null;
    contents = "";
    tempContents = "";
    changed = false;
    pollTime = 500; // In millisecons
    clipReader = new ClipReader();
  }

  @Override
  public void run() {
    while(!Thread.interrupted()) {
      transfer = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);

      tempContents = contents;

      if(transfer != null && transfer.isDataFlavorSupported(DataFlavor.stringFlavor)) {
	      try {
					contents = (String)transfer.getTransferData(DataFlavor.stringFlavor);
					if(tempContents.equals(contents)) {
						changed = false;
					} else {
						changed = true;
						clipReader.readIt(contents);
					}
	      } catch (UnsupportedFlavorException ex) {
					Logger.getLogger(ClipboardScanner.class.getName()).log(Level.SEVERE, null, ex);
	      } catch (IOException ex) {
					Logger.getLogger(ClipboardScanner.class.getName()).log(Level.SEVERE, null, ex);
	      }

	      if(hasChanged()) {
					System.out.println("New Contents:\n\t" + getClipboardContents() + "\n");
	      }

	      /*
	       * Wait before checking the clipboard again
	       */
	      try {
					Thread.sleep(pollTime);
	      } catch (InterruptedException ex) {
					Logger.getLogger(ClipboardScanner.class.getName()).log(Level.SEVERE, null, ex);
	      }
      }
    }
  }

  /*
   * Return whether the clipboard contents have changed
   */
  public boolean hasChanged() {
    return changed;
  }

  /*
   * Return the clipboard contents
   */
  public String getClipboardContents() {
    return contents;
  }

  /*
   * Useful for calling its methods from the GUI
   */
  public static ClipReader getClipReader() {
    return clipReader;
  }
}
