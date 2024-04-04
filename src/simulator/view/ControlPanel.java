package simulator.view;

import java.awt.BorderLayout;
import java.io.File;
import simulator.control.Controller;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

class ControlPanel extends JPanel {
	private Controller _ctrl;
	private ChangeRegionsDialog _changeRegionsDialog;
	private JToolBar _toolBar;
	private JFileChooser _fc;
	private boolean _stopped = true; // used in the run/stop buttons
	private JButton _quitButton;
	// TODO add more attributes here …
	private JButton _openButton;
	private JButton _viewerButton;
	private JButton _regionsButton;
	private JButton _runButton;
	private JButton _stopButton;



	
	ControlPanel(Controller ctrl) {
		_ctrl = ctrl;
		initGUI();
	}
	
	private void initGUI() {
		setLayout(new BorderLayout());
		_toolBar = new JToolBar();
		add(_toolBar, BorderLayout.PAGE_START);
		
		
		// TODO create the different buttons/attributes and add them to the toolbar.
		// Each of them should have a corresponding tooltip. You may use
		// _toolBar.addSeparator() to add the vertical-line separator between
		// those components that need it.
		
		// Quit Button
		_toolBar.add(Box.createGlue()); // this aligns the button to the right
		_toolBar.addSeparator();
		_quitButton = new JButton();
		_quitButton.setToolTipText("Quit");
		_quitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		_quitButton.addActionListener((e) -> ViewUtils.quit(this));
		_toolBar.add(_quitButton);
		
		// TODO Initialise _fc with a JfileChooser instance. In order for it
		// to open in the examples directory, you can use the following code:
		// _fc.setCurrentDirectory(new File(System.getProperty("user.dir")
		// + "/resources/examples"));
		_fc = new JFileChooser();
		_fc.setCurrentDirectory(new File(System.getProperty("user.dir") + "/resources/examples"));
		
		//Open Button
		_toolBar.add(Box.createGlue()); 
		_toolBar.addSeparator();
		_openButton = new JButton();
		_openButton.setToolTipText("Open");
		_openButton.setIcon(new ImageIcon("resources/icons/open.png"));
		_openButton.addActionListener((e) -> _fc.showOpenDialog(ViewUtils.getWindow(this)));
		//wtf: 
		//(2) when the user has selected a file, load it
		//and parse the file contents into a JSONObject, reset the simulator using _ctrl.reset(...) with
		//the corresponding parameters, and load the JSONObject created using _ctrl.load_data(...).
		
		//viewer Button
		_toolBar.add(Box.createGlue()); 
		_toolBar.addSeparator();
		_viewerButton = new JButton();
		_viewerButton.setToolTipText("View");
		_viewerButton.setIcon(new ImageIcon("resources/icons/viewer.png"));
		_viewerButton.addActionListener((e) -> MapWindow _mapWindow = new MapWindow()); //MapWindow(Frame parent, Controller ctrl)
		
		//regions Button
		_toolBar.add(Box.createGlue()); 
		_toolBar.addSeparator();
		_regionsButton = new JButton();
		_regionsButton.setToolTipText("Regions");
		_regionsButton.setIcon(new ImageIcon("resources/icons/regions.png"));
		_regionsButton.addActionListener((e) ->  _changeRegionsDialog.open(ViewUtils.getWindow(this)));
		
		//run button
		_toolBar.add(Box.createGlue()); 
		_toolBar.addSeparator();
		_runButton = new JButton();
		_runButton.setToolTipText("Run");
		_runButton.setIcon(new ImageIcon("resources/icons/run.png"));
		_runButton.addActionListener((e) -> 
		{
			// (1) disable all the buttons except the stop button ( ) and change the value of the attribute _stopped to false
			setButtonsExceptStop(false);
			_stopped = false;
			//(2) get the delta-time value from the corresponding JTextField
			
			//(3) call the run_sim method with the value of steps specified in the corresponding JSpinner:
			run_sim(0,0); //idk how to get the values from the Jspinner
		});
		
		//stop button
		_toolBar.add(Box.createGlue()); 
		_toolBar.addSeparator();
		_regionsButton = new JButton();
		_stopButton.setToolTipText("Stop");
		_stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
		_stopButton.addActionListener((e) ->  _stopped = true);
		

		
		
		
		
		
		// TODO Initialise _changeRegionsDialog with an instance of the
		// change-regions dialog.
	}
	// TODO The rest of the methods go here...
	
	private void run_sim(int n, double dt) {
		 if (n > 0 && !_stopped) {
		try {
		_ctrl.advance(dt); //new method when we merge
		 SwingUtilities.invokeLater(() -> run_sim(n - 1, dt));
		} catch (Exception e) {
		 // TODO pass the corresponding error message to
		 // ViewUtils.showErrorMsg
			ViewUtils.showErrorMsg(""); //we need to add viewUtil and the rest of the new classes
		 // TODO enable all the buttons
			setButtons(true);
			_stopped = true;
		}
		 } else {
				setButtons(true);
				_stopped = true;
		 }
		}
	
	private void setButtons(boolean set)
	{
		_quitButton.setEnabled(set);
		_openButton.setEnabled(set);
		_viewerButton.setEnabled(set);
		_regionsButton.setEnabled(set);
		_runButton.setEnabled(set);
		_stopButton.setEnabled(set);
	}
	private void setButtonsExceptStop(boolean set)
	{
		_quitButton.setEnabled(set);
		_openButton.setEnabled(set);
		_viewerButton.setEnabled(set);
		_regionsButton.setEnabled(set);
		_runButton.setEnabled(set);
	}
	
}
