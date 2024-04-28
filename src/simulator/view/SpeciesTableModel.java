package simulator.view;

import java.util.HashMap;
import java.util.Map;
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
	final static int COL_INDEX_FOR_GENETIC_CODE = 0;
	Controller _ctrl; 
	int _cols = State.values().length + 1;
	
	
	String[] columnName;
	
	int nextIndex = 0;
	
	Map<String, Map<State, Integer>> info;
	HashMap<State, Integer> colIndex; //maps state value to the corresponding column
	
	Object[][] myArray;
	
	 SpeciesTableModel(Controller ctrl) {
		 colIndex = new HashMap<State, Integer>();
		 info = new HashMap<String, Map<State, Integer>>();
		 initColumnNames();
		 _ctrl = ctrl;
		 _ctrl.addObserver(this);
	 // TODO initialise the corresponding data structures
	 }
	 
	 private void initColumnNames() {
		 columnName = new String[_cols];
		 columnName[COL_INDEX_FOR_GENETIC_CODE] = "Species";
		 int i = COL_INDEX_FOR_GENETIC_CODE + 1;
		 for (State s: State.values()) {
			 colIndex.put(s, i);
			 columnName[i] = s.name();
			 ++i;
		 }
	 }
	 // TODO the rest of the methods go here...
	 
	 private void initInfo()
	 {
		 
		 info.clear();
	 }
	 
	 private void initAray() {
		 myArray = new Object[info.size()][_cols];
		 int i = 0;
		 for (String g: info.keySet()) {
			 myArray[i][0] = g;
			 for (State state: State.values()) {
				 Map<State, Integer> stateToValue = info.get(g);
				 int value = (stateToValue.containsKey(state) ? stateToValue.get(state) : 0);
				 myArray[i][colIndex.get(state)] = value;
			 }
			 ++i;
		 }
	 }
	 
	 void updateArray(List<AnimalInfo> animals)
	 {
		 initInfo();
		 for (AnimalInfo a: animals) {
			 String geneticCode = a.get_genetic_code();
			 if (!info.containsKey(geneticCode)) {
				 info.put(geneticCode, new HashMap<State, Integer>());
			 }
			 Map<State, Integer> stateToValue = info.get(geneticCode);
			 if (!stateToValue.containsKey(a.get_state())) {
				 stateToValue.put(a.get_state(), 0);
			 }
			 int value = stateToValue.get(a.get_state());
			 ++value;
			 stateToValue.replace(a.get_state(), value);
			 
			 initAray();
			 //myArray[rowIndex.get(geneticCode)][colIndex.get(a.get_state())] = value
		}
		fireTableDataChanged();
		 
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
		updateArray(animals);
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
		return info.size();
	}

	@Override
	public int getColumnCount() {		
		return State.values().length + 1;
	}
	
	@Override
	public String getColumnName(int col) {
		initColumnNames();
		return columnName[col];
	}
	
	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
	    myArray[rowIndex][columnIndex] = value;
	    fireTableCellUpdated(rowIndex, columnIndex);
	}

}
