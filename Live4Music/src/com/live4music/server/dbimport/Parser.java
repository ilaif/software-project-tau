/*
The Viba IT FreeDB to MySQL parser
Copyright (C) 2006  Viba IT Handelsbolag

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */


package com.live4music.server.dbimport;

import java.io.*;
import java.util.ArrayList;

/**
 * This is the main parser of the Viba IT FreeDB to MySQL parser.
 * 
 * @author Viba IT Handelsbolag
 */

public class Parser {

	private File f;
	private BufferedReader br;

	private int length;
	private int dyear;
	
	private ArrayList<Integer> trackOffsets = new ArrayList<Integer>();

	private String dtitle;
	private String dgenre;

	private String[] discid = new String[1];
	private String[] ttitle = new String[200];

	/**
	 * Constructs a parser for the given file.
	 * 
	 * @param file
	 */
	public Parser(File file) {
		f = file;
	}
	
	public Parser(BufferedReader bf) {
		br = bf;
		f = null;
	}

	/**
	 * Close and clean up the parser.
	 */
	public void close() {
		try {
			br.close();
			ttitle = null;
			dgenre = null;
			dtitle = null;
			discid = null;
			f = null;
		} catch (IOException e) {
			System.err.println("Error in parser: Couldn't close BufferedReader. " + e);
		}
	}

	/**
	 * Returns the disc length in seconds.
	 * @return seconds
	 */
	public int getDiscLength() {
		return length;
	}

	/**
	 * Return an array with strings representing all found disc IDs found.
	 * @return String array
	 */
	public String[] getDiscId() {
		return discid;
	}

	/**
	 * Return the title of the disc.
	 * @return Title
	 */
	public String getDtitle() {
		return dtitle;
	}

	/**
	 * Return the year of the disc release.
	 * @return year
	 */
	public int getDyear() {
		return dyear;
	}

	/**
	 * Return the genre of the disc.
	 * @return genre
	 */
	public String getDgenre() {
		return dgenre;
	}

	/**
	 * Returns all titles found in an array of strings.
	 * @return titles
	 */
	public String[] getTtitle() {
		return ttitle;
	}
	
	public ArrayList<Integer> getTrackOffsets()
	{
		return trackOffsets;
	}

	/**
	 * Determine if the file parsed is a CDDB file or not.
	 * @return True if it is a CDDB file, False otherwise
	 */
	public boolean isCddbFormat() {
		if (f!= null && f.isFile()) {
			// Open file and check first row
			try {
				br = new BufferedReader(new FileReader(f));
				if (br.readLine().startsWith("# xmcd"))
					return true;
			} catch (FileNotFoundException e) {
				return false;
			} catch (IOException e) {
				System.err.println("Error in parser: Failed to read file "
						+ br.toString() + ". " + e);
				return false;
			}
		}
		else if (br != null)
		{
			try {				
				if (br.readLine().startsWith("# xmcd"))
					return true;
			} catch (FileNotFoundException e) {
				return false;
			} catch (IOException e) {
				System.err.println("Error in parser: Failed to read file "
						+ br.toString() + ". " + e);
				return false;
			}			
		}
		return false;
	}

	/**
	 * Run parser on the given file using the definition from
	 * http://www.freedb.org/modules.php?name=Sections&sop=viewarticle&artid=29
	 * 
	 * @return Returns <code>true</code> on success and <code>false</code>
	 *         on failure.
	 */
	public boolean run() {
		String line;
		boolean readingTrackOffsets = false;
		try {
			// Read the file
			int title_index = 0;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("# Disc length: ")) {
					readingTrackOffsets = false;	// Added by Rotem
					length = Integer.parseInt(line.split("\\s")[3]);
				} else if (line.startsWith("DISCID=")) {
					if (line.indexOf(",") == -1) {
						discid[0] = line.substring(line.indexOf("=")+1);
					}else{
						discid = line.substring(line.indexOf("=")+1).split(","); // Deprecated in definition
					}
				/* Added by Rotem */
				} else if (line.startsWith("# Track frame offsets:")) {
					readingTrackOffsets = true;					
				} else if (line.startsWith("DTITLE=")) {
					dtitle = line.substring(line.indexOf("=")+1);
				} else if (line.startsWith("DYEAR=")) {
					int len = line.substring(6).trim().length();
					if (len == 4) {
						dyear = Integer.parseInt(line.substring(line.indexOf("=")+1).trim());
						if (dyear > 10000) {
							dyear = 0;
						}
					} else {
						dyear = 0;
					}
				} else if (line.startsWith("DGENRE=")) {
					dgenre = line.substring(line.indexOf("=")+1);
				} else if (line.startsWith("TTITLE")) {
					title_index = Integer.parseInt(line.substring(6, line.indexOf("=")));
					if (ttitle[title_index] == null) {
						ttitle[title_index] = line.substring(line.indexOf("=")+1);
					} else {
						ttitle[title_index] += line.substring(line.indexOf("=")+1);
					}
				}
				/* Added by Rotem */
				else if (readingTrackOffsets)
				{
					String trackOffset = line.substring(1).trim();
					if (trackOffset.length() > 1)
					{
						int Offset = Integer.parseInt(trackOffset);
						trackOffsets.add(Offset);
					}
				}
				line = null;
			}
		} catch (NumberFormatException e) {
			System.err.println("Error in parser: Argument is not a number in file " + f.getAbsolutePath() );
			return false;
		} catch (IOException e) {
			System.err.println("Error in parser: Failed to parse file "	+ f.getAbsolutePath() + ". " + e);
			return false;
		} catch (NullPointerException e) {
			System.err.println("Error in parser: NullPointerException in file " + f.getAbsolutePath());
			return false;
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("Error in parser: Array index out of bounds for file " + f.getAbsolutePath());
			return false;
		}
		return true;
	}

}
