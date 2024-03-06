package simulator.model.regions;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;

import org.json.JSONArray;
import org.json.JSONObject;

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
		this._cellHeight = (double)height / rows;
		this._cellWidth = (double)width / cols;
		this._regions = new Region[cols][rows];
		for (int i = 0; i < cols; ++i) {
			for (int j = 0; j < rows; j++) {
				_regions[i][j] = new DefaultRegion();
			}
		}
		_animal_region = new HashMap<Animal, Region>();
	}


	/**
	 * Sets a region in the Matrix of region given a column and a row, replacing the old region and inheriting its list of animals.
	 * 
	 * */
	public void set_region(int row, int col, Region r) {
		assert(row >= 0 && row < _rows && col >= 0 && col < _cols); 
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
	 * Calculates the region where the animal should be given the animal's position. The returned list will have only two elements.
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
	 * Registers animal in its corresponding region given its position and includes it in the mapping between animals and regions.
	 * @param a - Animal instance
	 */
	public void register_animal(Animal a) {
		a.init(this);
		List<Integer> coords = getRegionColAndRow(a);
		
		int i = coords.get(0);
		int j = coords.get(1);
		assert(i >= 0 && i < _cols && j >= 0 && j < _rows);
		
		Region region = _regions[i][j]; 

		region.add_animal(a);
		_animal_region.put(a, region); 
	}

	/** 
	 * Unregisters animal from its region and the mapping between animals and regions.
	 * @param a - Animal instance
	 */
	public void unregister_animal(Animal a) {
		Region region = _animal_region.get(a);
		region.remove_animal(a);
		_animal_region.remove(a); 
	}
	
	/**
	 * Updates the region of an animal considering its position.
	 * @param a - Animal instance
	 */
	public void update_animal_region(Animal a) {
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
	public void update_all_regions(double dt) {
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
		Region actualRegion = _animal_region.get(a);
		
		return actualRegion.get_food(a, dt);
	}

	/**
	 * Checks if a certain coordinate is in a region given its column and row in the map.
	 * @param regionCol Column of the region
	 * @param regionRow Row of the region
	 * @param coords Coordinates given in the format of a Vector2D
	 * @return True if the coordinates are effectively inside the given region, false otherwise
	 */
	private boolean coordInRegion(int regionCol, int regionRow, Vector2D coords) {
		double x = coords.getX();
		double y = coords.getY();
		return (x >= regionCol * _cellWidth && x < (regionCol + 1) * _cellWidth && y >= regionRow * _cellHeight && y < (regionRow + 1) * _cellHeight);
	}
	
	/**
	 * Exhaustive method to check and return the regions that are within the range of sight given a position (coordinates in Vector2D format).
	 * It is an exhaustive method as it tries to cover the radius of the range of sight around the animal in the map with points, and traverses the regions to check if 
	 * they contain any of this points using the coordInRegion method.
	 * 
	 * @param range Range of sight (distance) of animal
	 * @param animalCoords Coordinates of the animal given in Vector2D 
	 * @return List of regions that are within range of sight of the animal
	 */
	private List<Region> getRegionsInSight(double range, final Vector2D animalCoords){
		final int accuracy1 = 12;
		final int accuracy2 = 4;
		List<Region> regionsInSight = new ArrayList<Region>();
		List<Vector2D> coordsInSight = new ArrayList<Vector2D>();
		
		for (int i = 0; i < accuracy1; ++i) {
			for (int j = 0; j < accuracy2; ++j) {
				coordsInSight.add(new Vector2D(animalCoords.getX() + Math.cos(i * 2 * Math.PI / accuracy1) * (range / accuracy2) * j, animalCoords.getY() + Math.sin(i * 2 * Math.PI / accuracy1) * (range / accuracy2) * j));
			}
			
		}
		coordsInSight.add(animalCoords);
		
		for (int i = 0; i < _cols; ++i) {
			for (int j = 0; j < _rows; ++j) {
				boolean included = false;
				int k = 0;
				
				while (k < accuracy1 * accuracy2 + 1 && !included) {
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
	
	/**
	 * Gets all the animals in a list given a list of regions.
	 * @param regions List of regions 
	 * @return List of animals 
	 */
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
		
		Predicate<Animal> inRange = animal -> animalPos.distanceTo(animal.get_position()) > sightOfRange;
		filter.and(inRange);
		animalsInRange.removeIf(filter);

		return animalsInRange;
	}
	
	public JSONObject as_JSON() {
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		
		for (int i = 0; i < _cols; ++i) {
			for (int j = 0; j < _rows; ++j) {
				JSONObject region = new JSONObject();
				region.put("row", j);
				region.put("col", i);
				region.put("data", _regions[i][j].as_JSON());
				ja.put(region);
			}
		}
		
		json.put("regions", ja);
		
		
		return json;
		
	}

}
