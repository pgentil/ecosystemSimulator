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
	
	/**
	 * Calculates the region where the animal should be given the animal's position. The returned list will have only two elements
	 * @param a - Animal instance
	 * @return Coordinates of the region where the animal should be. list[0] = col, list[1] = row
	 * 	 */
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

	/**	
	 * Registers animal in its corresponding region given its position and includes it in the mapping between animals and regions
	 * @param a - Animal instance
	 */
	void register_animal(Animal a) {
		List<Integer> coords = getRegionColAndRow(a);
		int i = coords.get(0);
		int j = coords.get(1);
		Region region = _regions[i][j];
		region.add_animal(a);
		_animal_region.put(a, region); 
		a.init(this);
	}

	/**
	 * Unregisters animal from its region and the mapping between animals and regions
	 * @param a - Animal instance
	 */
	void unregister_animal(Animal a) {
		List<Integer> coords = getRegionColAndRow(a);
		int i = coords.get(0);
		int j = coords.get(1); // 
		Region region = _regions[i][j];
		region.remove_animal(a);
		_animal_region.remove(a);
	}
	
	/**
	 * Updates the region of an animal considering its position.
	 * @param a - Animal instance
	 */
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
	
	/**
	 * Updates all regions. Calls update() for all regions in the region manager.
	 * @param dt
	 */
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
		return _rows;
	}

	@Override
	public int get_width() {
		return _width;
	}

	@Override
	public int get_height() {
		return _height;
	}

	@Override
	public int get_region_width() {
		return (int) _cellWidth;
	}

	@Override
	public int get_region_height() {
		return (int) _cellHeight;
	}

	
	@Override
	public double get_food(Animal a, double dt) {
		List<Integer> coords = getRegionColAndRow(a);
		int i = coords.get(0);
		int j = coords.get(1);
		Region actualRegion = _regions[i][j];
		
		return actualRegion.get_food(a, dt);
	}

	
	private boolean coordInRegion(int regionCol, int regionRow, Vector2D coords) {
		double x = coords.getX();
		double y = coords.getY();
		return (x >= regionCol * _cellWidth && x < (regionCol + 1) * _cellWidth && y >= regionRow * _cellHeight && y < (regionRow + 1) * _cellHeight);
	}
	
	private List<Region> getRegionsInSight(double range, final Vector2D animalCoords){
		final int accuracy = 20;
		List<Region> regionsInSight = new ArrayList<Region>();
		List<Vector2D> coordsInSight = new ArrayList<Vector2D>();
		
		for (int i = 0; i < accuracy; ++i) {
			coordsInSight.add(new Vector2D(animalCoords.getX() + Math.cos(i * 2 * Math.PI / 20) * range, animalCoords.getY() + Math.sin(i * 2 * Math.PI / 20) * range));
		}
		
		for (int i = 0; i < _cols; ++i) {
			for (int j = 0; j < _rows; ++j) {
				boolean included = false;
				int k = 0;
				
				while (k < accuracy && !included) {
					if (coordInRegion(i, j, coordsInSight.get(k))) {
						regionsInSight.add(_regions[i][j]);
						included = true;
					}
					++k;
				}
			}
		}
		
		return regionsInSight;
	}
	
	private List<Animal> getAnimalsInRegions(List<Region> regions){
		List<Animal> animals = new ArrayList<Animal>();
		
		for (Region r: regions) {
			animals.addAll(r.getAnimals());
		}
		
		return animals;
	}
	
	@Override
	public List<Animal> get_animals_in_range(Animal e, Predicate<Animal> filter) {
		Vector2D animalPos = e.get_position();
		double sightOfRange = e.get_sight_range();
		List<Region> regionsInSight = getRegionsInSight(sightOfRange, animalPos);
		List<Animal> animalsInRange = getAnimalsInRegions(regionsInSight);
		
		Predicate<Animal> inRange = animal -> animalPos.distanceTo(animal.get_position()) <= sightOfRange;
		animalsInRange.removeIf(!(inRange));
		
		
		
		
		
		return null;
	}

}
