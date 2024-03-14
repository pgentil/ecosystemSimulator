package simulator.model.regions;

import java.util.List;

import simulator.model.JSONable;
import simulator.model.animals.AnimalInfo;

public interface RegionInfo extends JSONable{
	public List<AnimalInfo> getAnimalsInfo();
}
