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

/**
 *
 * @author Christopher Lemire {@literal <goodbye300@aim.com>}
 */
public class Replay implements Runnable {

	private final ClipReader clipReader;

	/**
	 * Sets up this object but does not replay until run() is executed.
	 *
	 * @param clipReader The ClipReader containing the streams, Process, ESPEAKCMD String Array
	 */
	public Replay(ClipReader clipReader) {
		this.clipReader = clipReader;
	}

	/**
	 * Replays the last spoken audio message.
	 */
	@Override
	public void run() {
		clipReader.replay();
	}

}
