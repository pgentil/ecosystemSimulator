package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.EcoSysObserver;
import simulator.model.animals.AnimalInfo;
import simulator.model.animals.State;
import simulator.model.regions.MapInfo;
import simulator.model.regions.RegionInfo;

public class SpeciesTableModel extends AbstractTableModel implements EcoSysObserver{
	// TODO define the necessary attributes
	Controller _ctrl;
	int _rows; 
	int _cols = State.values().length + 1;
	
	int normalSheep;
	int mateSheep;
	int hungerSheep;
	int dangerSheep;
	int deadSheep;
	
	int normalWolf;
	int mateWolf;
	int hungerWolf;
	int dangerWolf;
	int deadWolf;
	
	


	
	int[][] myArray = new int[_rows][_cols];
	 SpeciesTableModel(Controller ctrl) {
		 _ctrl = ctrl;
		 _ctrl.addObserver(this);
		 initArray();
		 
	 // TODO initialise the corresponding data structures
	 }
	 // TODO the rest of the methods go here...
	 
	 void initArray()
	 {
		 for (int i = 0; i < _rows; i++ )
			 for(int ii = 0; ii < _cols; ii++)
				 myArray[i][ii] = 0;
	 }
	 
	 void updateArray(List<AnimalInfo> animals)
	 {
		 for (AnimalInfo a: animals)
			 for(State s: State.values())
			 {
				 if(a.get_state() == s)
					 
			 }
				 
		 
	 }
	
	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
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
		
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getColumnCount() {		
		return State.values().length + 1;
	}

}
