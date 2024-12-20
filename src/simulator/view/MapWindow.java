package simulator.view;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.EcoSysObserver;
import simulator.model.animals.AnimalInfo;
import simulator.model.regions.MapInfo;
import simulator.model.regions.RegionInfo;

public class MapWindow extends JFrame implements EcoSysObserver{

	 private Controller _ctrl;
	 private AbstractMapViewer _viewer;
	 private Frame _parent;
	 MapWindow(Frame parent, Controller ctrl) {
		 super("[MAP VIEWER]");
		 _ctrl = ctrl;
		 _parent = parent;
		 intiGUI();
		 _ctrl.addObserver(this);
		 
		 }
		 private void intiGUI() {
		 JPanel mainPanel = new JPanel(new BorderLayout());
		 // TODO set the contentPane to mainPanel
		 this.setContentPane(mainPanel);
		 // TODO create the viewer and add it to mainPanel (in the centre)
		 _viewer = new MapViewer();
		 mainPanel.add(_viewer, BorderLayout.CENTER);
		 // TODO in windowClosing method, remove ‘MapWindow.this’ from observers
		 addWindowListener(new WindowListener() {
	
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
	
			@Override
			public void windowClosing(WindowEvent e) {
				_ctrl.removeObserver(MapWindow.this);
				
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
		 
		 pack();
		 if (_parent != null)
		 setLocation(
		 _parent.getLocation().x + _parent.getWidth()/2 - getWidth()/2,
		 _parent.getLocation().y + _parent.getHeight()/2 - getHeight()/2);
		 setResizable(false);
		 setVisible(true);
	 }
	 // TODO otros métodos van aquí….
	
	
	
	
	
	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(() -> { _viewer.reset(time, map, animals); pack(); });
		
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		SwingUtilities.invokeLater(() -> { _viewer.reset(time, map, animals); pack(); });
		
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(() -> { _viewer.update(animals, time); });
		
	}

}
