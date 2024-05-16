package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Locale;

import simulator.control.Controller;
import simulator.launcher.Main;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import org.json.JSONObject;
import org.json.JSONTokener;


class ControlPanel extends JPanel{
	private Controller _ctrl;
	private ChangeRegionsDialog _changeRegionsDialog;
	private JToolBar _toolBar;
	private JFileChooser _fc;
	private boolean _stopped = true; // used in the run/stop buttons
	private JButton _quitButton;
	private JButton _openButton;
	private JButton _viewerButton;
	private JButton _regionsButton;
	private JButton _runButton;
	private JButton _stopButton;
	private JSpinner _steps;
	private JTextField _deltaField;



	
	ControlPanel(Controller ctrl) {
		_ctrl = ctrl;
		initGUI();
	}
	
	private void initGUI() {
		JPanel auxPanel;
		
		setLayout(new BorderLayout());
		_toolBar = new JToolBar();
		add(_toolBar, BorderLayout.PAGE_START);
		_fc = new JFileChooser();
		_fc.setCurrentDirectory(new File(System.getProperty("user.dir") + "/resources/examples"));
		_changeRegionsDialog = new ChangeRegionsDialog(_ctrl);
		
		//Open Button
		/**
		 * Creates a button that when clicked opens the chosen files and if correctly opened, loads the data 
		 * from the chosen file.
		 */
		_toolBar.add(Box.createGlue()); 
		_toolBar.addSeparator();
		_openButton = new JButton();
		_openButton.setToolTipText("Open");
		_openButton.setIcon(new ImageIcon("resources/icons/open.png"));
		_openButton.addActionListener((e) -> {
			int returnVal = _fc.showOpenDialog(ViewUtils.getWindow(this));
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = _fc.getSelectedFile();
				FileInputStream is = null;
				try {
					is = new FileInputStream(file);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				JSONObject joFromFile = new JSONObject(new JSONTokener(is));
				assert(joFromFile.has("cols") && joFromFile.has("rows") && joFromFile.has("width") && joFromFile.has("height"));
				_ctrl.reset(joFromFile.getInt("cols"),
						joFromFile.getInt("rows"),
						joFromFile.getInt("width"),
						joFromFile.getInt("height"));
				_ctrl.load_data(joFromFile);
			} 
		});
		_toolBar.add(_openButton, 0);
		
		//viewer Button
		_toolBar.add(Box.createGlue()); 
		_toolBar.addSeparator();
		_viewerButton = new JButton();
		_viewerButton.setToolTipText("View");
		_viewerButton.setIcon(new ImageIcon("resources/icons/viewer.png"));
		_toolBar.add(_viewerButton, 1);
		_viewerButton.addActionListener((e) -> {
			JFrame frame = new JFrame();
			frame.pack();
		    MapWindow _mapWindow = new MapWindow(frame, _ctrl);
		});		
		
		
		//regions Button
		_toolBar.add(Box.createGlue()); 
		_regionsButton = new JButton();
		_regionsButton.setToolTipText("Regions");
		_regionsButton.setIcon(new ImageIcon("resources/icons/regions.png"));
		_toolBar.add(_regionsButton, 2);
		_regionsButton.addActionListener((e) ->  _changeRegionsDialog.open(ViewUtils.getWindow(this)));
		
		/**
		 * creates the jSpinner and JtextField for the steps and delta time using a spinner, textfield and 2 labels
		 * their default value is set to 10000 and 0.03 correspondingly. An aux panel was created to help with the 
		 * formatting
		 */
		
		_steps = new JSpinner();
		_deltaField = new JTextField();
		
		_toolBar.add(Box.createGlue()); 
		_toolBar.addSeparator();
		auxPanel = new JPanel();
		auxPanel.add(_steps);
		auxPanel.setOpaque(false);
		_steps.setValue(10000);
		_steps.setPreferredSize(new Dimension(90, 30)); //(new Dimension(90, 50));
		JLabel stepsLabel = new JLabel("Steps: ");
		_toolBar.add(stepsLabel,5);
		_toolBar.add(auxPanel, 6);
		
		_toolBar.add(Box.createGlue()); 
		_toolBar.addSeparator();
		auxPanel = new JPanel();
		auxPanel.add(_deltaField);
		auxPanel.setOpaque(false);
		_deltaField.setPreferredSize(new Dimension(90, 30));
		_deltaField.setText(String.format(Locale.US, "%.2f", Main.getDeltaTime()));
		JLabel dTLabel = new JLabel(" Delta-Time: ");
		_toolBar.add(dTLabel,7);
		_toolBar.add(auxPanel, 8);
		
		//run button
		/**
		 * gets the needed information from the Jspinner (steps) and textFiels (data time) and calls the runSim method with them
		 */
		_toolBar.add(Box.createGlue()); 
		_toolBar.addSeparator();
		_runButton = new JButton();
		_runButton.setToolTipText("Run");
		_runButton.setIcon(new ImageIcon("resources/icons/run.png"));
		_runButton.addActionListener((e) -> 
		{
			int stepVlue = (int)_steps.getValue();
			double _deltaFieldVlue;
			String value = _deltaField.getText();
			if (!value.equals(""))
				_deltaFieldVlue = Double.parseDouble(_deltaField.getText()); 
			else {
				_deltaFieldVlue = 0;
			}
			setButtonsExceptStop(false);
			_stopped = false;			
			run_sim(stepVlue,_deltaFieldVlue); 
		});
		_toolBar.add(_runButton, 3);

		
		//stop button
		_toolBar.add(Box.createGlue()); 
		_stopButton = new JButton();
		_stopButton.setToolTipText("Stop");
		_stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
		_stopButton.addActionListener((e) -> {
			_stopped = true;
			});
		_toolBar.add(_stopButton, 4);
		
		// Quit Button
		_toolBar.add(Box.createGlue()); 
		_toolBar.addSeparator();
		_quitButton = new JButton();
		_quitButton.setToolTipText("Quit");
		_quitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		_quitButton.addActionListener((e) -> ViewUtils.quit(this));
		_toolBar.add(_quitButton);
	}
	
	private void run_sim(int n, double dt) {
		 if (n > 0 && !_stopped) {
		try {
		_ctrl.advance(dt); 
		Thread.sleep((long) (dt * 1000));
		 SwingUtilities.invokeLater(() -> run_sim(n - 1, dt));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			ViewUtils.showErrorMsg(e.getMessage()); 
			setButtons(true);
			_stopped = true;
		}
		 } else {
				setButtons(true);
				_stopped = true;
		 }
		}
	/**
	 * sets all buttons to whatever the boolean "set" is 
	 * @param set
	 */
	
	private void setButtons(boolean set)
	{
		_quitButton.setEnabled(set);
		_openButton.setEnabled(set);
		_viewerButton.setEnabled(set);
		_regionsButton.setEnabled(set);
		_runButton.setEnabled(set);
		_stopButton.setEnabled(set);
	}
	/**
	 * sets all buttons except the stop button to whatever the boolean "set" is 
	 * @param set
	 */
	private void setButtonsExceptStop(boolean set)
	{
		_quitButton.setEnabled(set);
		_openButton.setEnabled(set);
		_viewerButton.setEnabled(set);
		_regionsButton.setEnabled(set);
		_runButton.setEnabled(set);
	}
	
}
