package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import simulator.control.Controller;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import org.json.JSONObject;
import org.json.JSONTokener;

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
	private JSpinner _steps;
	private JTextField _deltaField;



	
	ControlPanel(Controller ctrl) {
		_ctrl = ctrl;
		initGUI();
	}
	
	private void initGUI() {
		setLayout(new BorderLayout());
		_toolBar = new JToolBar();
		add(_toolBar, BorderLayout.PAGE_START);
		_fc = new JFileChooser();
		_fc.setCurrentDirectory(new File(System.getProperty("user.dir") + "/resources/examples"));
		_changeRegionsDialog = new ChangeRegionsDialog(_ctrl);
		
		//Open Button
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
			} //TODO else
		});
		_toolBar.add(_openButton, 0);
		
		//viewer Button
		_toolBar.add(Box.createGlue()); 
		_toolBar.addSeparator();
		_viewerButton = new JButton();
		_viewerButton.setToolTipText("View");
		_viewerButton.setIcon(new ImageIcon("resources/icons/viewer.png"));
		_toolBar.add(_viewerButton, 1);
		
//		_viewerButton.addActionListener((e) -> {
//		    MapWindow _mapWindow = new MapWindow(this, _ctrl);
//		});		
		
		
		//regions Button
		_toolBar.add(Box.createGlue()); 
		_regionsButton = new JButton();
		_regionsButton.setToolTipText("Regions");
		_regionsButton.setIcon(new ImageIcon("resources/icons/regions.png"));
		_toolBar.add(_regionsButton, 2);
		_regionsButton.addActionListener((e) ->  _changeRegionsDialog.open(ViewUtils.getWindow(this)));
		
		
		
		_steps = new JSpinner();
		_deltaField = new JTextField();
		
		_toolBar.add(Box.createGlue()); 
		_toolBar.addSeparator();
		_steps.setPreferredSize(new Dimension(90, 1));
		JLabel stepsLabel = new JLabel("Steps: ");
		_toolBar.add(stepsLabel,5);
		_toolBar.add(_steps, 6);
		
		_toolBar.add(Box.createGlue()); 
		_toolBar.addSeparator();
		_deltaField.setPreferredSize(new Dimension(70, 1));
		JLabel dTLabel = new JLabel(" Delta-Time: ");
		_toolBar.add(dTLabel,7);
		_toolBar.add(_deltaField, 8);
		
		//run button
		_toolBar.add(Box.createGlue()); 
		_toolBar.addSeparator();
		_runButton = new JButton();
		_runButton.setToolTipText("Run");
		_runButton.setIcon(new ImageIcon("resources/icons/run.png"));
		_runButton.addActionListener((e) -> 
		{
			int stepVlue = (int)_steps.getValue(); //buff idk
			double _deltaFieldVlue;
			String value = _deltaField.getText();
			if (!value.equals(""))
				_deltaFieldVlue = Double.parseDouble(_deltaField.getText());  //buff idk
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
		_toolBar.add(Box.createGlue()); // this aligns the button to the right
		_toolBar.addSeparator();
		_quitButton = new JButton();
		_quitButton.setToolTipText("Quit");
		_quitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		_quitButton.addActionListener((e) -> ViewUtils.quit(this));
		_toolBar.add(_quitButton);
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
			e.printStackTrace(System.out);
			ViewUtils.showErrorMsg(e.getMessage()); //we need to add viewUtil and the rest of the new classes
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
