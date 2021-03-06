package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.Objects;

import javax.swing.*;

import main.ChargeTime;

/**
 * ChargeUI maintains the user interface for calculating charge times.
 * 
 * @author Kimberle McGill
 * @version 1.1.170314
 */
public class ChargeUI extends JFrame implements ActionListener, ItemListener {

	/** ID number used for object serialization. */
	private static final long serialVersionUID = 1L;
	/** Charge time being calculated */
	private ChargeTime charge;
	/** Start date */
	private String startDate = "";
	/** Start time */
	private String startTime = "";
	/** Stop date */
	private String stopDate = "";
	/** Stop time */
	private String stopTime = "";
	/** Main panel to display other panels */
	private JPanel panel;
	/** Panel for multiple day toggle button */
	private JPanel pnlToggle;
	/** Panel for start date/time */
	private JPanel pnlStart;
	/** Panel for start date */
	private JPanel pnlStartDate;
	/** Panel for start time */
	private JPanel pnlStartTime;
	/** Panel for stop date/time */
	private JPanel pnlStop;
	/** Panel for stop date */
	private JPanel pnlStopDate;
	/** Panel for stop time */
	private JPanel pnlStopTime;
	/** Panel for buttons */
	private JPanel pnlButtons;
	/** Panel for text area */
	private JPanel pnlArea;
	/** Label for start date */
	private JLabel lblStartDate;
	/** Label for start time */
	private JLabel lblStartTime;
	/** Label for stop date */
	private JLabel lblStopDate;
	/** Label for stop time */
	private JLabel lblStopTime;
	/** Text field for entering start date */
	private JTextField txtStartDate;
	/** Text field for entering start time */
	private JTextField txtStartTime;
	/** Text field for entering stop date */
	private JTextField txtStopDate;
	/** Text field for entering stop time */
	private JTextField txtStopTime;
	/** Text area for displaying charges */
	private JTextArea areaCharges;
	/** Button for calculating multiple days */
	private JCheckBox chkDays;
	/** Button for running the calculations */
	private JButton btnCalculate;
	/** Button for clearing the fields */
	private JButton btnNew;
	/** Button for quitting the program */
	private JButton btnQuit;
	/** Scroll pane for displaying charges */
	private JScrollPane chargesScrollPane;
	/** Dimension for size of toggle panel */
	private Dimension togglePanelSize = new Dimension(450, 30);
	/** Dimension for size of start and stop panels */
	private Dimension dateTimePanelSize = new Dimension(450, 50);
	/** Dimension for size of area panel */
	private Dimension areaPanelSize = new Dimension(250, 50);
	/** Dimension for size of button panel */
	private Dimension buttonPanelSize = new Dimension(450, 50);
	/** Dimension of rigid space the main panel */
	private Dimension panelBumper = new Dimension(0, 25);
	/** Dimension of rigid space between calculate and new buttons */
	private Dimension button1Bumper = new Dimension(50, 0);
	/** Dimension of rigid space between new and quit buttons */
	private Dimension button2Bumper = new Dimension(55, 0);
	/** Is the multi-day check mark selected? */
	private Boolean isChecked;
	/** Size of date / time text fields */
	private static final int TEXT_ENTRY = 8;
	/** Width of Panel */
	private static final int PANEL_WIDTH = 500;
	/** Height of Panel */
	private static final int PANEL_HEIGHT = 550;
	/** X location of panel */
	private static final int X_LOCATION = 500;
	/** Y location of Panel */
	private static final int Y_LOCATION = 100;
	/** Text area width */
	private static final int AREA_WIDTH = 50;
	/** Text area height */
	private static final int AREA_HEIGHT = 30;
	/** Length of date and time entries */
	private static final int PROPER_ENTRY_LENGTH = 4;
	/** Title of the application */
	private static final String APP_TITLE = "Perfusion Charges";
	/** Integer for the month of October (first double digit month) */
	private static final int OCT = 10;

	/**
	 * Creates a new ChargeUI.
	 */
	public ChargeUI() {
		super();

		// Set up general GUI info
		setSize(PANEL_WIDTH, PANEL_HEIGHT);
		setLocation(X_LOCATION, Y_LOCATION);
		setTitle(APP_TITLE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// set up main panel for displaying 4 smaller panels
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		// set toggle button panel
		pnlToggle = new JPanel();
		pnlToggle.setMaximumSize(togglePanelSize);
		chkDays = new JCheckBox("MULTI-DAY CALCULATION", false);
		isChecked = false;
		chkDays.addItemListener(this);
		pnlToggle.add(chkDays, BorderLayout.CENTER);

		// set start date panel
		pnlStartDate = new JPanel();
		lblStartDate = new JLabel("Start Date (MMdd): ");
		txtStartDate = new JTextField(TEXT_ENTRY);
		txtStartDate.setEnabled(false);
		txtStartDate.setBackground(Color.GRAY);
		pnlStartDate.add(lblStartDate);
		pnlStartDate.add(txtStartDate);
		// set start time panel
		pnlStartTime = new JPanel();
		lblStartTime = new JLabel("Start Time (HHmm): ");
		txtStartTime = new JTextField(TEXT_ENTRY);
		pnlStartTime.add(lblStartTime);
		pnlStartTime.add(txtStartTime);
		// set start panel
		pnlStart = new JPanel();
		pnlStart.setMaximumSize(dateTimePanelSize);
		pnlStart.setLayout(new BoxLayout(pnlStart, BoxLayout.X_AXIS));
		pnlStart.add(pnlStartDate);
		pnlStart.add(pnlStartTime);

		// set stop date panel
		pnlStopDate = new JPanel();
		lblStopDate = new JLabel("Stop Date (MMdd): ");
		txtStopDate = new JTextField(TEXT_ENTRY);
		txtStopDate.setBackground(Color.GRAY);
		txtStopDate.setEnabled(false);
		pnlStopDate.add(lblStopDate);
		pnlStopDate.add(txtStopDate);
		// set stop time panel
		pnlStopTime = new JPanel();
		lblStopTime = new JLabel("Stop Time (HHmm): ");
		txtStopTime = new JTextField(TEXT_ENTRY);
		pnlStopTime.add(lblStopTime);
		pnlStopTime.add(txtStopTime);
		// set stop panel
		pnlStop = new JPanel();
		pnlStop.setLayout(new BoxLayout(pnlStop, BoxLayout.X_AXIS));
		pnlStop.setMaximumSize(dateTimePanelSize);
		pnlStop.add(pnlStopDate);
		pnlStop.add(pnlStopTime);

		// set area panel
		pnlArea = new JPanel();
		pnlArea.setPreferredSize(areaPanelSize);
		areaCharges = new JTextArea(AREA_WIDTH, AREA_HEIGHT);
		areaCharges.setText("");
		chargesScrollPane = new JScrollPane(areaCharges);
		chargesScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		areaCharges.setEditable(false);
		pnlArea.add(chargesScrollPane, BorderLayout.CENTER);

		// set button panel
		pnlButtons = new JPanel();
		pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.X_AXIS));
		pnlButtons.setPreferredSize(buttonPanelSize);
		btnCalculate = new JButton("CALCULATE");
		btnCalculate.addActionListener(this);
		btnNew = new JButton("NEW CALCULATION");
		btnNew.addActionListener(this);
		btnQuit = new JButton("QUIT");
		btnQuit.addActionListener(this);
		pnlButtons.add(btnCalculate);
		pnlButtons.add(Box.createRigidArea(button1Bumper));
		pnlButtons.add(btnNew);
		pnlButtons.add(Box.createRigidArea(button2Bumper));
		pnlButtons.add(btnQuit);
		
		// add separate panels to main panel
		panel.add(pnlToggle);
		panel.add(Box.createRigidArea(panelBumper));
		panel.add(pnlStart);
		panel.add(pnlStop);
		panel.add(Box.createRigidArea(panelBumper));
		panel.add(pnlButtons);
		panel.add(Box.createRigidArea(panelBumper));
		panel.add(pnlArea);
		panel.add(Box.createRigidArea(panelBumper));

		// make exiting with x (windows) or red stop (mac) clean
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// code for detecting hitting the Enter key
		handleEnterKey();

		// Add panel to the container
		Container c = getContentPane();
		c.add(panel, BorderLayout.CENTER);

		// Set the GUI visible
		setVisible(true);
	}
	
	/**
	 * Item listener specific for the check box component.
	 * 
	 * @param e 
	 *            state of check box
	 */
	public void itemStateChanged(ItemEvent e) {
		if(e.getStateChange() == ItemEvent.SELECTED) {
			txtStartDate.setEnabled(true);
			txtStartDate.setText("");
			txtStartDate.setBackground(Color.WHITE);
			txtStopDate.setEnabled(true);
			txtStopDate.setText("");
			txtStopDate.setBackground(Color.WHITE);
			isChecked = true;
		}
		else if(e.getStateChange() == ItemEvent.DESELECTED){
			txtStartDate.setEnabled(false);
			txtStartDate.setText("");
			txtStartDate.setBackground(Color.GRAY);
			txtStopDate.setEnabled(false);
			txtStopDate.setText("");
			txtStopDate.setBackground(Color.GRAY);
			isChecked = false;
		}
	}

	/**
	 * Performs an action based on the given ActionEvent.
	 * 
	 * @param e
	 *            user event that triggers an action.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnQuit) {
			System.exit(0);
		} else if (e.getSource() == btnCalculate) {
			calculateCharge();
		} else if (e.getSource() == btnNew) {
			txtStartDate.setText("");
			txtStartTime.setText("");
			txtStopDate.setText("");
			txtStopTime.setText("");
			areaCharges.setText("");
			startTime = "";
			stopTime = "";
			charge = null;
		}
	}

	/**
	 * Handles using the Enter key on different components.
	 */
	private void handleEnterKey() {
		txtStartDate.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {

					// if not empty
					// if properly formatted
					if (!txtStartDate.getText().isEmpty() && isProperFormat(txtStartDate.getText().trim())) {

						// get the entry
						startDate = txtStartDate.getText().trim();

						if (!stopDate.isEmpty() && !startTime.isEmpty() && !stopTime.isEmpty()) {

							calculateCharge();

							// move to new calculation button
							btnNew.requestFocus();
						} else {

							// move to next field
							txtStartTime.requestFocus();
						}
					} else if (!txtStartDate.getText().isEmpty()) {
						JOptionPane.showMessageDialog(panel, "Invalid Date Format!\nMust be MMdd", "INPUT ERROR",
								JOptionPane.WARNING_MESSAGE);
						txtStartDate.setText("");
					} else if (txtStartDate.getText().isEmpty()) {
						JOptionPane.showMessageDialog(panel,  "Please enter the start date", "INPUT ERROR",
								JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		txtStartTime.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {

					// if not empty
					// if properly formatted
					if (!txtStartTime.getText().isEmpty() && isProperFormat(txtStartTime.getText().trim())) {

						// get the entry
						startTime = txtStartTime.getText().trim();

						if (!stopTime.isEmpty()) {

							calculateCharge();

							// move to new calculation button
							btnNew.requestFocus();
						} else if (!txtStartTime.getText().isEmpty()) {

							// move to next field
							txtStopDate.requestFocus();
						}
					} else if (!txtStartTime.getText().isEmpty()) {
						JOptionPane.showMessageDialog(panel, "Invalid Time Format!\nMust be HHmm", "INPUT ERROR",
								JOptionPane.WARNING_MESSAGE);
						txtStartTime.setText("");
					} else if (txtStartTime.getText().isEmpty()) {
						JOptionPane.showMessageDialog(panel,  "Please enter the start time", "INPUT ERROR",
								JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		txtStopDate.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {

					// if not empty
					// if properly formatted
					if (!txtStopDate.getText().isEmpty() && isProperFormat(txtStopDate.getText().trim())) {

						// get entry
						stopDate = txtStopDate.getText().trim();

						if (!startDate.isEmpty() && !startTime.isEmpty() && !stopTime.isEmpty()) {

							calculateCharge();

							// move to new calculation button
							btnNew.requestFocus();
						} else {

							// move to next field
							txtStopTime.requestFocus();
						}
					} else if (!txtStopDate.getText().isEmpty()) {
						JOptionPane.showMessageDialog(panel, "Invalid Date Format!\nMust be MMdd", "INPUT ERROR",
								JOptionPane.WARNING_MESSAGE);
						txtStopDate.setText("");
					} else if (txtStopDate.getText().isEmpty()) {
						JOptionPane.showMessageDialog(panel,  "Please enter the stop date", "INPUT ERROR",
								JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		txtStopTime.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
                    
					// if not empty
					// if properly formatted
					if (!txtStopTime.getText().isEmpty() && isProperFormat(txtStopTime.getText().trim())) {

						// get entry
						stopTime = txtStopTime.getText().trim();

						// if all entries are made, make calculation
						if (isFilledOut()) {

							calculateCharge();

							// move to new calculation button
							btnNew.requestFocus();

						} else {
							if (txtStartDate.getText().isEmpty()) {
								txtStartDate.requestFocus();
							} else if (txtStartTime.getText().isEmpty()) {
								txtStartTime.requestFocus();
							} else {
								txtStopDate.requestFocus();
							}
						}

					} else if (!txtStopTime.getText().isEmpty()) {
						JOptionPane.showMessageDialog(panel, "Invalid Time Format!\nMust be HHmm", "INPUT ERROR",
								JOptionPane.WARNING_MESSAGE);
						txtStopTime.setText("");
					} else if (txtStopTime.getText().isEmpty()) {
						JOptionPane.showMessageDialog(panel,  "Please enter the stop time", "INPUT ERROR",
								JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		btnCalculate.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					calculateCharge();
					btnNew.requestFocus();
				}
			}
		});
		btnNew.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					txtStartDate.setText("");
					txtStartTime.setText("");
					txtStopDate.setText("");
					txtStopTime.setText("");
					areaCharges.setText("");
					startDate = "";
					startTime = "";
					stopDate = "";
					stopTime = "";
					charge = null;
				}
			}
		});
		btnQuit.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					System.exit(0);
				}
			}
		});
	}

	/**
	 * Creates a new ChargeTime with the given dates and times, and displays the
	 * result in the text area.
	 */
	private void calculateCharge() {
		try {
			if (isFilledOut()) {
				// multi day?
				if (isChecked) {
					startDate = txtStartDate.getText().trim();
					stopDate = txtStopDate.getText().trim();
					// did the user enter the same dates?
					if (Objects.equals(startDate, stopDate)) {
						isChecked = false;
						JOptionPane.showMessageDialog(panel, "Days entered are the SAME", "Warning!", 
								JOptionPane.WARNING_MESSAGE);
					}
				// if not, use today's date
				} else {
					String today;
					// add a leading zero for months less then October
					if (LocalDate.now().getMonthValue() < OCT) {
						today = "0" + LocalDate.now().getMonthValue();
					} else {
						today = "" + LocalDate.now().getMonthValue();
					}
					// add a leading zero for days less than 10
					if (LocalDate.now().getDayOfMonth() < 10) {
						today += "0" + LocalDate.now().getDayOfMonth();
					} else {
						today += LocalDate.now().getDayOfMonth();
					}
					startDate = today;
					stopDate = today;
				}
				startTime = txtStartTime.getText().trim();
				stopTime = txtStopTime.getText().trim();
			} else {
				JOptionPane.showMessageDialog(panel, "Please provide ALL dates and times!", "INPUT ERROR",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			String chargeStart = startDate + " " + startTime;
			String chargeEnd = stopDate + " " + stopTime;
			this.charge = new ChargeTime(chargeStart, chargeEnd, isChecked);
			this.areaCharges.setText(charge.getCharges());
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(panel, e.getMessage(), "COMPUTATIONAL ERROR", JOptionPane.WARNING_MESSAGE);
			txtStartDate.setText("");
			txtStartTime.setText("");
			txtStopDate.setText("");
			txtStopTime.setText("");
			areaCharges.setText("");
			startDate = "";
			startTime = "";
			stopDate = "";
			stopTime = "";
			charge = null;
			chkDays.requestFocus();
		}
	}

	/**
	 * Checks to be sure the entered dates/times are in the proper format.
	 */
	private boolean isProperFormat(String entry) {
		if (entry.length() == PROPER_ENTRY_LENGTH) {
			for (int i = 0; i < entry.length(); i++) {
				if (!Character.isDigit(entry.charAt(i))) {
					return false;
				}
			}
		} else {
			return false;
		}

		return true;
	}

	/**
	 * Main method which starts the user interface.
	 * 
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		new ChargeUI();
	}

	/**
	 * Checks if the proper fields have entries.
	 */
	private boolean isFilledOut() {
		if (isChecked) {
			if (txtStartDate.getText().isEmpty() || txtStartTime.getText().isEmpty() || txtStopDate.getText().isEmpty()
					|| txtStopTime.getText().isEmpty()) {
				return false;
			}
		} else {
			if (txtStartTime.getText().isEmpty() || txtStopTime.getText().isEmpty()) {
				return false;
			}
		}
		
		return true;
	}
}
