package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import simulator.control.Controller;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainWindow extends JFrame {
	
	private Controller _ctrl;
	private JPanel _control_panel;
	private JPanel _statusBar;
	public MainWindow(Controller ctrl) {
		super("[ECOSYSTEM SIMULATOR]");
		_ctrl = ctrl;
		initGUI();
	}
	private void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		setContentPane(mainPanel);
		//TODO create ControlPanel and add it in the PAGE_START section of mainPanel
		//TODO create StatusBar and add it in the PAGE_END section of mainPanel
		
		
		_control_panel = new ControlPanel(_ctrl);
		mainPanel.add(_control_panel, BorderLayout.PAGE_START);
		_statusBar = new StatusBar(_ctrl);
		
		mainPanel.add(_statusBar, BorderLayout.PAGE_END);
		//Definition of the tables panel (use a vertical BoxLayout)
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		mainPanel.add(contentPanel, BorderLayout.CENTER);
		//TODO create the species table and add it to the contentPanel.
		InfoTable speciesTable = new InfoTable("Species", new SpeciesTableModel(_ctrl));
		speciesTable.setPreferredSize(new Dimension(500, 250));
		mainPanel.add(speciesTable);
		
		
		//Use setPreferredSize(new Dimension(500, 250)) to fix its size
		//TODO create the regions table.
		//Use setPreferredSize(new Dimension(500, 250)) to fix its size
		//TODO call ViewUtils.quit(MainWindow.this) in the windowClosing method
//		addWindowListener(  ); TODO
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		pack();
		setVisible(true);
	}
}
