package simulator.model.regions;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;

import simulator.misc.Vector2D;
import simulator.model.animals.Animal;

public class RegionManager implements AnimalMapView{
	private int _width;
	private int _height;
	private int _cols;
	private int _rows;
	private double _cellWidth;
	private double _cellHeight;
	private Region[][] _regions;
	private Map<Animal, Region> _animal_region;
	
	public RegionManager(int cols, int rows, int width, int height) {
		this._width = width;
		this._height = height;
		this._cols = cols;
		this._rows = rows;
		this._cellHeight = height / rows;
		this._cellWidth = width / cols;
		this._regions = new Region[cols][rows];
		for (int i = 0; i < cols; ++i) {
			for (int j = 0; j < rows; j++) {
				_regions[i][j] = new DefaultRegion();
			}
		}
		_animal_region = new HashMap<Animal, Region>();
	}
	
	
	/**
	 * Sets a region in the Matrix of region given a column and a row, replacing the old region and inheriting its list of animals
	 * 
	 * */
	void set_region(int row, int col, Region r) {
		assert(row >= 0 && row < _rows && col >= 0 && col < _cols); //FIXME
		Region original = _regions[col][row];
		List<Animal> list = r.animalList;
		
		list.addAll(original.getAnimals()); //adds elements of the old region to the new one
		for (Animal a: original.getAnimals()) {
			boolean check = false;
			check = _animal_region.replace(a, original, r);
			assert(check);
		}
		_regions[col][row] = r;
		
	}
	
	 void register_animal(Animal a) {
		 Vector2D v = a.get_position();
		 int i = 0;
		 int j = 0;
		 double x = v.getX(), y = v.getY();
		 
		 while (x >= _cellWidth) {
			 x -= _cellWidth;
			 i++;
		 }
		 while (y >= _cellHeight) {
			 y -= _cellHeight;
			 j++;
		 }
		 
		 Region region = _regions[i][j];
		 region.add_animal(a);
		 _animal_region.put(a, region); 
	 }
	
	@Override
	public int get_cols() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int get_rows() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int get_width() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int get_height() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int get_region_width() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int get_region_height() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double get_food(Animal a, double dt) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Animal> get_animals_in_range(Animal e, Predicate<Animal> filter) {
		// TODO Auto-generated method stub
		return null;
	}

}
