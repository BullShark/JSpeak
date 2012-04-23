/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jspeak;

/**
 *
 * @author Christopher Lemire <christopher.lemire@gmail.com>
 */
public class Replay implements Runnable {
  private final ClipReader clipReader;
  public Replay(ClipReader clipReader) {
    this.clipReader = clipReader;
  }

  @Override
  public void run() {
    clipReader.replay();
  }

}
