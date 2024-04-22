package simulator.view;

import java.util.HashMap;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.EcoSysObserver;
import simulator.model.animals.AnimalInfo;
import simulator.model.animals.Diet;
import simulator.model.regions.MapInfo;
import simulator.model.regions.MapInfo.RegionData;
import simulator.model.regions.RegionInfo;

public class RegionsTableModel extends AbstractTableModel implements EcoSysObserver{
	static final int NUMBER_OF_UNVARIABLE_DATA = 3;
	static final int COL_INDEX_OF_ROWS = 0;
	static final int COL_INDEX_OF_COLS = 1;
	static final int COL_INDEX_OF_REGION_DESCRIPTION = 2;
	// TODO define the necessary attributes
	Controller _ctrl;
	int _rows;
	int _cols;
	HashMap<String, Integer> rowIndex; //maps genetic code to index in table
	HashMap<Diet, Integer> colIndex;
	
	String[] columnName;
	
	int nextIndex = 0;
	
	Object[][] myArray;
	
	public RegionsTableModel(Controller ctrl) {
		this._cols = Diet.values().length + NUMBER_OF_UNVARIABLE_DATA;
		rowIndex = new HashMap<String, Integer>();
		colIndex = new HashMap<Diet, Integer>();
		initColumnNames();
		_ctrl = ctrl;
		_ctrl.addObserver(this);
	}
	
	private void initColumnNames() {
		 columnName = new String[_cols];
		 columnName[COL_INDEX_OF_ROWS] = "Row";
		 columnName[COL_INDEX_OF_COLS] = "Column";
		 columnName[COL_INDEX_OF_REGION_DESCRIPTION] = "Desc.";
		 
		 int i = NUMBER_OF_UNVARIABLE_DATA;
		 for (Diet s: Diet.values()) {
			 colIndex.put(s, i);
			 columnName[i] = s.name();
			 ++i;
		 }
	 }
	
	private void initArray(int rows, int cols)
	 {
		 rowIndex.clear();
		 nextIndex = 0;
		 myArray = new Object[rows * cols][Diet.values().length + NUMBER_OF_UNVARIABLE_DATA];
		 _rows = rows * cols;
		 
		 for (int i = 0; i < _rows; ++i) {
			 for (int j = 0; j < _cols; ++j) {
				 if (j != COL_INDEX_OF_REGION_DESCRIPTION) {
					 myArray[i][j] = 0;
				 }
			 }
		 }
	 }
	
	
	
	void updateArray(MapInfo map) {
		initArray(map.get_rows(), map.get_cols());
		for (RegionData r: map) {
			
			RegionInfo ri = r.r();
			myArray[nextIndex][COL_INDEX_OF_REGION_DESCRIPTION] = ri.toString();
			rowIndex.put(r.toString(), nextIndex);
			++nextIndex;
			int r_idx = rowIndex.get(r.toString());
			myArray[r_idx][COL_INDEX_OF_ROWS] = r.row();
			myArray[r_idx][COL_INDEX_OF_COLS] = r.col();
			for (AnimalInfo a: ri.getAnimalsInfo()) {
				int value = (Integer) myArray[r_idx][colIndex.get(a.get_diet())];
				++value;
				myArray[r_idx][colIndex.get(a.get_diet())] = value;
			}
		}
		fireTableDataChanged();
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return rowIndex.size();
	}

	@Override
	public int getColumnCount() {

		return Diet.values().length + NUMBER_OF_UNVARIABLE_DATA;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return myArray[rowIndex][columnIndex];
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		updateArray(map);
		
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		updateArray(map);
		
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		updateArray(map);
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		updateArray(map);
		
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		updateArray(map);
		
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
