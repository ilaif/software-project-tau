package com.live4music.client.ui;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

import com.cloudgarden.resource.SWTResourceManager;
import org.eclipse.swt.widgets.Label;

/** Initial Dialog
* ==============
* presents stores table for selection by user
* program is initiated by selected store's view 
*/
public class InitialDialog extends org.eclipse.swt.widgets.Dialog {
	private static Display display;
	private static InitialDialog inst;
	private static Shell shell;
	private static Shell dialogShell;
//	private static Button initDialogButtonExit;
	private static Button initDialogButtonStart;
	private static Composite initDialogGroup;
	private static Combo initDialogCombo;
	private Label lblWhatIsYour;

	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Dialog inside a new Shell.
	*/
	public static void openInitDialog() {
		try {
			display = Display.getDefault();
			shell = new Shell(display);
			shell.setLocation(Main.getCentral_x()-500, Main.getCentral_y()-405);
			inst = new InitialDialog(shell, SWT.NULL);
			inst.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void closeInitDialog(){
		shell.close();
	}

	public InitialDialog(Shell parent, int style) {
		super(parent, style);
	}

	public void open() {
		try {
			Shell parent = getParent();
			dialogShell = new Shell(parent, SWT.TITLE | SWT.CLOSE | SWT.MIN);
			dialogShell.setSize(1000, 750);
			dialogShell.setBackgroundMode(SWT.INHERIT_DEFAULT);
			
			{
				//Register as a resource user - SWTResourceManager will
				//handle the obtaining and disposing of resources
				SWTResourceManager.registerResourceUser(dialogShell);
			}
			
			dialogShell.setText("Live 4 Music");
			
			Image backgroundImage = new Image(display,Main.relPath+"login.png");
			dialogShell.setBackgroundImage(backgroundImage);
			
			// set program icon
			Image progIcon = new Image(display,Main.relPath+"icon.png");
			dialogShell.setImage(progIcon);
			
			dialogShell.setLayout(new GridLayout(1, true));
			{
				initDialogGroup = new Composite(dialogShell, SWT.NONE);
				initDialogGroup.setLayout(null);
				initDialogGroup.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, true, 1, 1));
				initDialogGroup.setBackgroundMode(SWT.INHERIT_DEFAULT);
//				initDialogGroup.setText("Select store");
				initDialogGroup.setBounds(0, 0, 306, 413);
				initDialogGroup.setFont(Main.defaultFont);
				{
					initDialogCombo = new Combo(initDialogGroup, SWT.READ_ONLY);
					initDialogCombo.setBounds(49, 249, 172, 23);
					initDialogCombo.setFont(Main.defaultFont);
					initDialogCombo.setEnabled(false);
				}
				{
					initDialogButtonStart = new Button(initDialogGroup, SWT.CENTER);
					initDialogButtonStart.setText("START");
					initDialogButtonStart.setBounds(49, 282, 172, 50);
					initDialogButtonStart.setFont(Main.largeFont);
				}
				{
					lblWhatIsYour = new Label(initDialogGroup, SWT.NONE);
					lblWhatIsYour.setAlignment(SWT.CENTER);
					lblWhatIsYour.setFont(org.eclipse.wb.swt.SWTResourceManager.getFont("Arial", 15, SWT.BOLD));
					lblWhatIsYour.setForeground(org.eclipse.wb.swt.SWTResourceManager.getColor(255, 255, 255));
					lblWhatIsYour.setBounds(0, 218, 276, 30);
					lblWhatIsYour.setText("Select Your City");
				}
//				{
//					initDialogButtonExit = new Button(initDialogGroup, SWT.PUSH | SWT.CENTER);
//					initDialogButtonExit.setText("Exit");
//					initDialogButtonExit.setBounds(153, 282, 135, 33);
//					initDialogButtonExit.setFont(Main.defaultFont);
//				}
			}
			dialogShell.layout();			
			dialogShell.setLocation(getParent().toDisplay(0, 0));
			dialogShell.open();
			Display display = dialogShell.getDisplay();
			
			while (!dialogShell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Shell getDialogShell() {
		return dialogShell;
	}

	public static void setDialogShell(Shell dialogShell) {
		InitialDialog.dialogShell = dialogShell;
	}

//	public static Button getInitDialogButtonExit() {
//		return initDialogButtonExit;
//	}
//
//	public static void setInitDialogButtonExit(Button initDialogButtonExit) {
//		InitialDialog.initDialogButtonExit = initDialogButtonExit;
//	}

	public static Button getInitDialogButtonStart() {
		return initDialogButtonStart;
	}

	public static void setInitDialogButtonStart(Button initDialogButtonStart) {
		InitialDialog.initDialogButtonStart = initDialogButtonStart;
	}

	public static Composite getInitDialogGroup() {
		return initDialogGroup;
	}

	public static void setInitDialogGroup(Group initDialogGroup) {
		InitialDialog.initDialogGroup = initDialogGroup;
	}

	public static Combo getInitDialogCombo() {
		return initDialogCombo;
	}

	public static void setInitDialogCombo(Combo initDialogCombo) {
		InitialDialog.initDialogCombo = initDialogCombo;
	}

	public static Display getInitDisplay() {
		return display;
	}

	public static void setDisplay(Display display) {
		InitialDialog.display = display;
	}

	public static Display getDisplay() {
		return display;
	}

	public static InitialDialog getInst() {
		return inst;
	}

	public static Shell getShell() {
		return shell;
	}	
}
