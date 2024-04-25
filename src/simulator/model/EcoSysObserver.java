package simulator.model;

import java.util.List;

import simulator.model.animals.AnimalInfo;
import simulator.model.regions.MapInfo;
import simulator.model.regions.RegionInfo;

public interface EcoSysObserver {
	void onRegister(double time, MapInfo map, List<AnimalInfo> animals);
	void onReset(double time, MapInfo map, List<AnimalInfo> animals);
	void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals,  AnimalInfo a);
	void onRegionSet(int row, int col, MapInfo map, RegionInfo r);
	void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt);
}
