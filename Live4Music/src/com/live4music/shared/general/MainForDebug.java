package com.live4music.shared.general;

import com.live4music.shared.general.Debug.DebugOutput;

public class MainForDebug {
	public static void main(String[] args) {
		for(int i=0; i<60; i++){
			Debug.log("This is my message "+i, DebugOutput.FILE);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}

}
