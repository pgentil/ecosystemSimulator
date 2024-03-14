package simulator.factories;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class BuilderBasedFactory<T> implements Factory<T>{

	private Map<String, Builder<T>> _builders;
	private List<JSONObject> _builders_info; 

	public BuilderBasedFactory() {
		// Create a HashMap for _builders, and a LinkedList _builders_info  // ...   
		_builders = new HashMap<String, Builder<T>>();
		_builders_info = new LinkedList<JSONObject>();
	}   

	public BuilderBasedFactory(List<Builder<T>> builders) {
		this();  
		for (Builder<T> b : builders) {
			add_builder(b);
		}
	}   

	public void add_builder(Builder<T> b) {
		_builders.put(b.get_type_tag(), b);
		_builders_info.add(b.get_info());
		// add an entry "b.getTag() |-> b" to _builders.  
		// ...  
		// add b.get_info() to _buildersInfo  
		// ...   
	}   
	
	private T find(String tag_type, JSONObject data) {
		Builder<T> b = _builders.get(tag_type);
		return (b == null ? null : b.create_instance(data));
	}
	
	@Override
	public T create_instance(JSONObject info) {
		if (info == null)
			throw new IllegalArgumentException("'info' cannot be null");
		
		T instance = find(info.getString("type"), info.has("data") ? info.getJSONObject("data") : new JSONObject());
		if (instance == null) {
			throw new IllegalArgumentException("Unrecognized 'info':"                                                           
					+ info.toString());
		}
		
		return instance;
	}
	
	@Override
	public List<JSONObject> get_info() {
		return Collections.unmodifiableList(_builders_info);
	}





}
