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
 */

package expectusafterlun.ch.jspeak;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

/**
 *
 * @author Christopher Lemire {@literal <goodbye300@aim.com>}
 */
public class MbrolaVoices {
	private File espeakDir, mbrolaDir;
	private final FilenameFilter FF;
	private final File[] FILEARR;
	private String[] fstrArr;

	public MbrolaVoices() {
		FF = new FilenameFilter() {
			@Override public boolean accept(File dir, String name) {
				return name.matches("[a-z]{2}[1-9]");
			}
		};

		String os = System.getProperty("os.name");
		mbrolaDir = null;
		espeakDir = null;

		/* Debug */
		System.out.println("System OS is: " + os);

		switch (os) {
			case "Linux" -> {
				mbrolaDir = new File("/usr/share/espeak-data/voices/mb");
				espeakDir = new File("/usr/share/mbrola");

				System.setProperty("mbrola.base", mbrolaDir.toString());
			}
			case "Windows 95", "Windows 98", "Windows XP", "Windows Vista", "Windows NT (unknown)", "Windows 7", "Windows 8", "Windows 8.1", "Windows 10" -> {
				mbrolaDir = new File(System.getProperty("user.home") + "\\source\\repos\\MBROLA\\VisualC\\Win32\\Debug");
				espeakDir = new File("C:\\Program Files (x86)\\eSpeak\\espeak-data\\mbrola");

				System.setProperty("mbrola.base", mbrolaDir.toString());
			}
			default -> {
				System.err.println("Unsupported operating system!");
				System.exit(-1);
			}
		}

		/*
		 * Do not need to handle an arbitrary number of directories
		 * Documentation states espeak looks under two specific directories
		 * For mbrola voices
		 */
		if(espeakDir.exists() && mbrolaDir.exists()) {
			FILEARR = concatAll(espeakDir.listFiles(FF), mbrolaDir.listFiles(FF));
		} else if(espeakDir.exists()) {
			FILEARR = espeakDir.listFiles(FF);
		} else if(mbrolaDir.exists()) {
			FILEARR = mbrolaDir.listFiles(FF);
		} else {
			System.err.println("""
					Mbrola voices directory not found. Install espeak.
					If espeak is installed, and you still see this message, please report a bug at
					https://github.com/BullShark/JSpeak/issues""");
			FILEARR = null;
		}

		if(FILEARR != null) {
			fstrArr = new String[FILEARR.length];
			for(int j=0; j<FILEARR.length; j++) {
				fstrArr[j] = FILEARR[j].getName();
			}
		}
	}

	/*
	 * JComboBox's constructor takes a String Array
	 * Returns null when there are no voices
	 */
	public String[] getVoices() {
		return fstrArr;
	}

	/*
	 * Combine two or more arrays
	 */
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
