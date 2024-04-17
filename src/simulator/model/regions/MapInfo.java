package simulator.model.regions;

import java.util.Iterator;

import simulator.model.JSONable;

public interface MapInfo extends JSONable, Iterable<MapInfo.RegionData>{
	public record RegionData(int row, int col, RegionInfo r){
		public String toString() {
			StringBuilder str = new StringBuilder();
			str.append(this.row);
			str.append(" ");
			str.append(this.col);
			return str.toString();
		}
	}
	public int get_cols();
	public int get_rows();
	public int get_width(); 
	public int get_height();
	//Decided to change the return value to double, as the division of columns / width or rows / height may not result in an integer number
	public double get_region_width();
	public double get_region_height();
}
