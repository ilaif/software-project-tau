package com.live4music.client.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

/**
 * created by Ariel
 * 
 * exception for wrong employee details in manage tab
 */
public class EmployeeDetailsValidityException extends Exception{
	MessageBox msgBox;
	
	/**
	 * constructor for exception
	 * @param message
	 */
	public EmployeeDetailsValidityException(String message){
		super(message);
		
		msgBox = new MessageBox(Main.getMainShell(),SWT.ICON_ERROR | SWT.OK);
		msgBox.setMessage(message);
		msgBox.setText("Wrong employee details");
	}

	/**
	 * getter for the message box
	 * @return
	 */
	public MessageBox getMsgBox() {
		return msgBox;
	}
}
