package simulator.view;

import java.util.HashMap;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.EcoSysObserver;
import simulator.model.animals.AnimalInfo;
import simulator.model.animals.Diet;
import simulator.model.animals.State;
import simulator.model.regions.MapInfo;
import simulator.model.regions.MapInfo.RegionData;
import simulator.model.regions.RegionInfo;

public class RegionsTableModel extends AbstractTableModel implements EcoSysObserver{
	static final int NUMBER_OF_ROWS = 1000;
	// TODO define the necessary attributes
	Controller _ctrl;
	int _rows;
	int _cols;
	HashMap<String, Integer> rowIndex; //maps genetic code to index in table
	HashMap<Diet, Integer> colIndex;
	
	String[] columnName;
	
	int nextIndex = 0;
	
	Object[][] myArray = new Object[_rows][_cols];
	
	public RegionsTableModel() {
		this._rows = NUMBER_OF_ROWS;
		this._cols = Diet.values().length + 3;
		rowIndex = new HashMap<String, Integer>();
		colIndex = new HashMap<Diet, Integer>();
	}
	
	private void initColumnNames() {
		 columnName = new String[_cols];
		 columnName[0] = "Row";
		 columnName[1] = "Column";
		 columnName[2] = "Desc.";
		 
		 int i = 3;
		 for (Diet s: Diet.values()) {
			 colIndex.put(s, i);
			 columnName[i] = s.name();
			 ++i;
		 }
	 }
	
	void initArray()
	 {
		 for (int i = 0; i < _rows; i++ )
			 for(int ii = 0; ii < _cols; ii++)
				 if (ii != 2) {
					 myArray[i][ii] = 0;
				 }
	 }
	
	void updateArray(MapInfo map) {
		initArray();
		for (RegionData r: map) {
			
			for (AnimalInfo a: r.r().getAnimalsInfo()) {
				
			}
			
		}
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return rowIndex.size();
	}

	@Override
	public int getColumnCount() {

		return Diet.values().length + 3;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		// TODO Auto-generated method stub
		
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

}
