
package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import simulator.control.Controller;
import simulator.model.EcoSysObserver;
import simulator.model.animals.AnimalInfo;
import simulator.model.regions.MapInfo;
import simulator.model.regions.RegionInfo;

public class StatusBar extends JPanel implements EcoSysObserver{
	
	private Controller _ctrl;
	private JPanel _statusBar;
	int _totalAnimals = 0;
	double _time = 0;
	int _height = 0;
	int _width = 0;
	int _cols = 0;
	int _rows = 0;
	
	JLabel timeL = new JLabel();
	JLabel animalL = new JLabel();
	JLabel dimensionL = new JLabel();
	
	StatusBar(Controller ctrl) {
		_ctrl = ctrl;
		initGUI();
		_ctrl.addObserver(this);
	}
	
	private void initGUI() {
		 this.setLayout(new FlowLayout(FlowLayout.LEFT));
		 this.setBorder(BorderFactory.createBevelBorder(1));
		 
		 JSeparator s = new JSeparator(JSeparator.VERTICAL);
		 s.setPreferredSize(new Dimension(10, 20));
		 
		 JLabel timeL = new JLabel("Time: " +  _time);
		 this.add(timeL);
		 
		 this.add(s);
		 
		 JLabel animalL = new JLabel("Total Animals: " + _totalAnimals);
		 this.add(animalL);
		 
		 this.add(s);
		 
		 JLabel dimensionL = new JLabel("Dimension: " + _width + "x" + _height + " " + _cols + "x" + _rows); 
		 this.add(dimensionL);
		}

	
	void updateTimeMapAnimals(double time, MapInfo map, List<AnimalInfo> animals)
	{
		_time = time;
		_totalAnimals = animals.size();
		_height = map.get_height();
		_width = map.get_width();
		_cols = map.get_cols(); //idk if these are the cols and rows that they ask me
		_rows = map.get_rows();
		
		timeL.setText("Time: " +  _time);
		animalL.setText("Total Animals: " + _totalAnimals);
		dimensionL.setText("Dimension: " + _width + "x" + _height + " " + _cols + "x" + _rows);
	}
	
	

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		updateTimeMapAnimals(time, map, animals);
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		updateTimeMapAnimals(time, map, animals);

		
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		updateTimeMapAnimals(time, map, animals);

		
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		_height = map.get_height();
		_width = map.get_width();
		dimensionL.setText("Dimension: " + _width + "x" + _height + " " + _cols + "x" + _rows);


	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		updateTimeMapAnimals(time, map, animals);

		
	}

}
