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
	private List<Integer> getRegionColAndRow(Vector2D a) {
		Vector2D v = a;
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
		if(i >= 20 || j >= 15)
			System.out.println("");
		
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
		List<Integer> coords = getRegionColAndRow(a.get_position());
		
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
		List<Integer> coords = getRegionColAndRow(a.get_position());
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
	public double get_region_width() {
		return _cellWidth;
	}

	@Override
	public double get_region_height() {
		return _cellHeight;
	}

	
	@Override
	public double get_food(Animal a, double dt) {
		Region actualRegion = _animal_region.get(a);
		
		return actualRegion.get_food(a, dt);
	}
	
	
	/**
	 * Optimistic prediction of the regions that may be covered by the range of sight given some coordinates. It traverses the regions that surround the 
	 * coordinates. The amount of cells traversed depends on two variables, one being the range of sight of the animal and the other being
	 * the dimensions of the cells. The area traversed will always be symmetric in relation to the column and row that correspond to the coordinates.
	 * It will traverse the area delimited by these ranges: [x_coord - i - 1, x_coord + i + 1], [y_coord - j - 1, y_coord + j + 1], where i = div(range, _cellWidth)
	 * and j = div(range, _cellHeight), div(n, m) = [max(k) : 0 <= k and k is element of the integers : k * m < n]
	 * 
	 * @param range Range of sight (distance) of animal
	 * @param animalCoords Coordinates of the animal given in Vector2D 
	 * @return List of regions that are within range of sight of the animal
	 */
	private List<Region> getRegionsInSight(double range, final Vector2D animalCoords){
		List<Region> regionsInSight = new ArrayList<Region>();
		List<Integer> regionCoord = getRegionColAndRow(animalCoords);
		int regionCol = regionCoord.get(0);
		int regionRow = regionCoord.get(1);
		
		//how many rows and cols could the range of sight fully cover (optimistic range of sight) [animals that are included due to the optimistic prediction are excluded by the comparison of the actual distance to the animal]
		double rangeRow = range;
		int rows = 1; //starts from 1, as with any range of sight there could be at least 1 region, other than the actual one, that is being sighted. 
		while (rangeRow > _cellHeight) {
			rangeRow -= _cellHeight;
			++rows; //for every time the sight of range is still bigger than the region's dimension, one row more on top and bottom of the animal will be traversed
		}
		double rangeCol = range;
		int cols = 1; //idem
		while (rangeCol > _cellWidth) {
			rangeCol -= _cellWidth;
			++cols;//for every time the sight of range is still bigger than the region's dimension, one column more on left and right of the animal will be traversed
		}
		
		for (int i = regionCol - cols; i <= regionCol + cols; ++i) {
			for (int j = regionRow - rows; j <= regionRow + rows; ++j) {
				if (i >= 0 &&
					i < _cols &&
					j >= 0 &&
					j < _rows )
				{
					regionsInSight.add(_regions[i][j]);
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
	public List<Animal> get_animals_in_range(Animal e, Predicate<Animal> filter) { //Specified in AnimalMapView

		Vector2D animalPos = e.get_position();
		double sightOfRange = e.get_sight_range();
		List<Region> regionsInSight = getRegionsInSight(sightOfRange, animalPos); //Method returns an optimistic prediction of the regions in sight
		List<Animal> animalsInRange = getAnimalsInRegions(regionsInSight); //Method returns animals contained in the regions of regionsInSight
		
		Predicate<Animal> notInRange = animal -> animalPos.distanceTo(animal.get_position()) > sightOfRange; //If distance is greater than the range of sight
		animalsInRange.removeIf(notInRange); //removes if not in range of sight
		animalsInRange.removeIf(filter); //removes if [filter predicate] --> The filter predicate condition must be negated in order to remove all that do not satisfy the filter

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
