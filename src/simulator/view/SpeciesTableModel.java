package simulator.view;

import java.util.HashMap;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.EcoSysObserver;
import simulator.model.animals.AnimalInfo;
import simulator.model.animals.State;
import simulator.model.regions.MapInfo;
import simulator.model.regions.RegionInfo;

public class SpeciesTableModel extends AbstractTableModel implements EcoSysObserver{
	static final int NUMBER_OF_SPECIES = 2;
	// TODO define the necessary attributes
	Controller _ctrl;
	int _rows = NUMBER_OF_SPECIES; 
	int _cols = State.values().length + 1;
	
	String[] columnName;
	
	int nextIndex = 0;
	
	HashMap<String, Integer> rowIndex; //maps genetic code to index in table
	HashMap<State, Integer> colIndex; //maps state value to the corresponding column
	
	Object[][] myArray = new Object[_rows][_cols];
	
	 SpeciesTableModel(Controller ctrl) {
		 rowIndex = new HashMap<String, Integer>();
		 colIndex = new HashMap<State, Integer>();
		 initColumnNames();
		 _ctrl = ctrl;
		 _ctrl.addObserver(this);
		 initArray();
		 columnName = new String[_cols];
	 // TODO initialise the corresponding data structures
	 }
	 
	 private void initColumnNames() {
		 columnName = new String[_cols];
		 columnName[0] = "Species";
		 int i = 1;
		 for (State s: State.values()) {
			 colIndex.put(s, i);
			 columnName[i] = s.name();
			 ++i;
		 }
	 }
	 // TODO the rest of the methods go here...
	 
	 void initArray()
	 {
		 for (int i = 0; i < _rows; i++ )
			 for(int ii = 0; ii < _cols; ii++)
				 if (ii > 0) {
					 myArray[i][ii] = 1;
				 } else {
					 myArray[i][ii] = null;
				 }
	 }
	 
	 void updateArray(List<AnimalInfo> animals)
	 {
		 initArray();
		 for (AnimalInfo a: animals) {
			 String geneticCode = a.get_genetic_code();
			 if (!rowIndex.containsKey(geneticCode)) {
				 rowIndex.put(geneticCode, nextIndex);
			 }
			 rowIndex.get(geneticCode);
			 colIndex.get(a.get_state());
			 int value = (Integer)myArray[rowIndex.get(geneticCode)][colIndex.get(a.get_state())];
			 ++value;
			 setValueAt(value, rowIndex.get(geneticCode), colIndex.get(a.get_state()));
			 //myArray[rowIndex.get(geneticCode)][colIndex.get(a.get_state())] = value;
			 
		 }
				 
		 
	 }
	
	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		updateArray(animals);
		
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		updateArray(animals);
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		 String geneticCode = a.get_genetic_code();
		 if (!rowIndex.containsKey(geneticCode)) {
			 rowIndex.put(geneticCode, nextIndex);
		 }
		 int value = (Integer)myArray[rowIndex.get(geneticCode)][colIndex.get(a.get_state())];
		 ++value;
		 myArray[rowIndex.get(geneticCode)][colIndex.get(a.get_state())] = value;
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		
		
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		updateArray(animals);
		
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		return myArray[rowIndex][columnIndex];
	}

	@Override
	public int getRowCount() {
		return rowIndex.size();
	}

	@Override
	public int getColumnCount() {		
		return State.values().length + 1;
	}
	
	public String getColumnName(int col) {
		initColumnNames();
		return columnName[col]; }

}
