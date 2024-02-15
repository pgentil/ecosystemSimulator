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
	
	private List<Integer> getRegionColAndRow(Animal a) {
		Vector2D v = a.get_position();
		int i = 0;
		int j = 0;
		double x = v.getX(), y = v.getY();

		while (x >= _cellWidth) { //To calculate the column the animal is in
			x -= _cellWidth;
			i++;
		}
		while (y >= _cellHeight) { //To calculate the row the animal is in
			y -= _cellHeight;
			j++;
		}
		
		ArrayList<Integer> r = new ArrayList<Integer>();
		r.add(i);
		r.add(j);
		
		return r; //returns an array of two integers, representing the column and the row of the region where the animal resides
		
		
	}

	void register_animal(Animal a) {
		List<Integer> coords = getRegionColAndRow(a);
		int i = coords.get(0);
		int j = coords.get(1);
		Region region = _regions[i][j];
		region.add_animal(a);
		_animal_region.put(a, region); 
		a.init(this);
	}

	void unregister_animal(Animal a) {
		List<Integer> coords = getRegionColAndRow(a);
		int i = coords.get(0);
		int j = coords.get(1); // 
		Region region = _regions[i][j];
		region.remove_animal(a);
		_animal_region.remove(a);
	}
	
	void update_animal_region(Animal a) {
		List<Integer> coords = getRegionColAndRow(a);
		int i = coords.get(0);
		int j = coords.get(1);
		Region registeredRegion = _animal_region.get(a);
		Region actualRegion = _regions[i][j];
		
		assert(registeredRegion != null);
		if (registeredRegion != actualRegion) {
			registeredRegion.remove_animal(a);
			actualRegion.add_animal(a);
			_animal_region.replace(a, registeredRegion, actualRegion);
		}
	}
	
	void update_all_regions(double dt) {
		for (int i = 0; i < _cols; ++i) {
			for (int j = 0; j < _rows; ++j) {
				_regions[i][j].update(dt);
			}
		}
	}
	
	@Override
	public int get_cols() {
		return _cols;
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
		List<Integer> coords = getRegionColAndRow(a);
		int i = coords.get(0);
		int j = coords.get(1);
		Region actualRegion = _regions[i][j];
		
		return actualRegion.get_food(a, dt);
	}

	@Override
	public List<Animal> get_animals_in_range(Animal e, Predicate<Animal> filter) {
		double range = e.get_sight_range();
		return null;
	}

}
