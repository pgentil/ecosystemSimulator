package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

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
		//SPECIES TABLE
		InfoTable speciesTable = new InfoTable("Species", new SpeciesTableModel(_ctrl));
		speciesTable.setPreferredSize(new Dimension(500, 250));
		contentPanel.add(speciesTable);
		//REGIONS TABLE
		InfoTable regionsTable = new InfoTable("Regions", new RegionsTableModel(_ctrl));
		regionsTable.setPreferredSize(new Dimension(500, 250));
		regionsTable.setLocation(speciesTable.getX() , speciesTable.getY() + 525);
		contentPanel.add(regionsTable);
//		
		//TODO call ViewUtils.quit(MainWindow.this) in the windowClosing method
		 addWindowListener(new WindowListener() {
				
				@Override
				public void windowOpened(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}
		
				@Override
				public void windowClosing(WindowEvent e) {
					ViewUtils.quit(MainWindow.this);					
				}
		
				@Override
				public void windowClosed(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}
		
				@Override
				public void windowIconified(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}
		
				@Override
				public void windowDeiconified(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}
		
				@Override
				public void windowActivated(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}
		
				@Override
				public void windowDeactivated(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}});
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		pack();
		setVisible(true);
	}
}
