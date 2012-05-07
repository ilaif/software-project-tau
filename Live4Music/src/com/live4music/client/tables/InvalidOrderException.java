package com.live4music.client.tables;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import com.live4music.client.ui.*;

/**
 * 
 * exception for wrong order in stock tab
 */
public class InvalidOrderException extends Exception {
	
	private static final long serialVersionUID = -9017485340534996898L;
	
	MessageBox msgBox;
	
	/**
	 * constructor for exception
	 * @param message
	 */
	public InvalidOrderException(String message){
		super(message);
		
		msgBox = new MessageBox(Main.getMainShell(),SWT.ICON_ERROR | SWT.OK);
		msgBox.setMessage(message);
		msgBox.setText("Invalid order");
	}

	/**
	 * getter for the message box
	 * @return
	 */
	public MessageBox getMsgBox() {
		return msgBox;
	}
}
