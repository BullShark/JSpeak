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
  private long pollTime;
  private static ClipReader clipReader;
  private boolean firstRun;

  public ClipboardScanner() {
    transfer = null;
    contents = "";
    tempContents = "";
    pollTime = 500; // In millisecons
    clipReader = new ClipReader();
    firstRun = true;
  }
  
  @Override
  public void run() {
    while(!Thread.interrupted()) {
      transfer = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);

      tempContents = contents;

      if(transfer != null && transfer.isDataFlavorSupported(DataFlavor.stringFlavor)) {
        try {
          contents = (String)transfer.getTransferData(DataFlavor.stringFlavor);
          if(hasChanged() && !firstRun) {
            clipReader.readIt(contents);
            System.out.println("New Content:\n\n" + clipReader.toString());
            //TODO Make a TerminalColors.java or a LinuxColors.java for use with printed content
          } else {
            tempContents = contents;
            firstRun = false;
          }
        } catch (UnsupportedFlavorException ex) {
          Logger.getLogger(ClipboardScanner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
          Logger.getLogger(ClipboardScanner.class.getName()).log(Level.SEVERE, null, ex);
        }
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

  /*
   * Return whether the clipboard contents have changed
   */
  public boolean hasChanged() {
    return !(tempContents.equals(contents));
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
