package simulator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.control.Controller;
import simulator.factories.*;
import simulator.misc.Utils;
import simulator.model.Simulator;
import simulator.model.animals.Animal;
import simulator.model.animals.SelectionStrategy;
import simulator.model.regions.Region;
import simulator.view.MainWindow;

public class Main {

	private enum ExecMode {
		BATCH("batch", "Batch mode"), GUI("gui", "Graphical User Interface mode");

		private String _tag;
		private String _desc;

		private ExecMode(String modeTag, String modeDesc) {
			_tag = modeTag;
			_desc = modeDesc;
		}

		public String get_tag() {
			return _tag;
		}

		public String get_desc() {
			return _desc;
		}
	}

	// default values for some parameters
	//
	private final static Double _default_time = 10.0; // in seconds
	private final static Double _default_delta_time = 0.03;

	// some attributes to stores values corresponding to command-line parameters
	//
	private static Double _time = null;
	private static String _in_file = null;
	private static String _out_file = null;
	private static boolean _sv = false;
	private static ExecMode _mode = ExecMode.BATCH;
	private static Double _delta_time = null;
	private static Factory<Animal> _animal_factory;
	private static Factory<Region> _region_factory;
	private static Factory<SelectionStrategy> _strategy_factory;
	
	

	private static void parse_args(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = build_options();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parse_help_option(line, cmdLineOptions);
			parse_mode_option(line);
			parse_in_file_option(line);
			parse_out_file_option(line);
			parse_time_option(line);
			parse_sv_option(line);
            parse_delta_time_option(line);


			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options build_options() {
		Options cmdLineOptions = new Options();

		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg()
				.desc("Mode of the game.").build());
		// help
		cmdLineOptions.addOption(Option.builder("h").longOpt("help")
				.desc("Print this message.").build());

		// input file
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg()
				.desc("A configuration file.").build());

		// steps
		cmdLineOptions.addOption(Option.builder("t").longOpt("time").hasArg()
				.desc("An real number representing the total simulation time in seconds. Default value: " + _default_time + ".").build());
		 cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg()
				 .desc("Output file, where output is written.").build());
		 cmdLineOptions.addOption(Option.builder("sv").longOpt("simple-viewer")
				 .desc("Show the viewer window in console mode.").build());
		 cmdLineOptions.addOption(Option.builder("dt").longOpt("delta-time").hasArg()
				 .desc("A double representing actual time, in seconds, per simulation step. Default value: " + _default_delta_time + ".").build());
		return cmdLineOptions;
	}

	private static void parse_help_option(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}
	
	private static void parse_out_file_option(CommandLine line) throws ParseException {
		_out_file = line.getOptionValue("o");
		if (_mode == ExecMode.BATCH && _out_file == null) {
			throw new ParseException("In batch mode an output configuration file is required");
		}
	}

	private static void parse_in_file_option(CommandLine line) throws ParseException {
		_in_file = line.getOptionValue("i");
		if (_mode == ExecMode.BATCH && _in_file == null) {
			throw new ParseException("In batch mode an input configuration file is required");
		}
	}

	private static void parse_time_option(CommandLine line) throws ParseException {
		String t = line.getOptionValue("t", _default_time.toString());
		try {
			_time = Double.parseDouble(t);
			if (_time < 0) {
				throw new Exception();
			}
		} catch (Exception e) {
			throw new ParseException("Invalid value for time: " + t);
		}
	}
	
	 private static void parse_delta_time_option(CommandLine line) throws ParseException {
	        String dt = line.getOptionValue("dt", _default_delta_time.toString());
	        try {
	            _delta_time = Double.parseDouble(dt);
	            assert (_delta_time > 0);
	        } catch (Exception e) {
	            throw new ParseException("Invalid value for delta time: " + dt);
	        }
	 }
	 
	 private static void parse_mode_option(CommandLine line) throws ParseException { //TODO
	        if (line.hasOption("m")) {
	        	String mode = line.getOptionValue("m");
	        	if (mode.equals("gui")) {
	        		_mode = ExecMode.GUI;
	        	} else {
	        		_mode = ExecMode.BATCH;
	        	}
	        	
	        }
	 }
	 
	 private static void parse_sv_option(CommandLine line){
			if (line.hasOption("sv")) {
				_sv = true;
			}
	}
	

	private static void init_factories() {
		List<Builder<SelectionStrategy>> selection_strategy_builders = new ArrayList<Builder<SelectionStrategy>>();
		selection_strategy_builders.add(new SelectFirstBuilder());
		selection_strategy_builders.add(new SelectClosestBuilder());
		selection_strategy_builders.add(new SelectYoungestBuilder());
		_strategy_factory = new BuilderBasedFactory<SelectionStrategy>(selection_strategy_builders);
		
		List<Builder<Animal>> animal_builders = new ArrayList<Builder<Animal>>();
		animal_builders.add(new SheepBuilder());
		animal_builders.add(new WolfBuilder());
		_animal_factory = new BuilderBasedFactory<Animal>(animal_builders);
		
		List<Builder<Region>> region_builders = new ArrayList<Builder<Region>>();
		region_builders.add(new DefaultRegionBuilder());
		region_builders.add(new DynamicSupplyRegionBuilder());
		_region_factory = new BuilderBasedFactory<Region>(region_builders);
	}

	private static JSONObject load_JSON_file(InputStream in) {
		return new JSONObject(new JSONTokener(in));
	}


	private static void start_batch_mode() throws Exception {
		InputStream is = new FileInputStream(new File(_in_file));
		JSONObject joFromFile = new JSONObject(new JSONTokener(is));
		OutputStream out = new FileOutputStream(_out_file);
		assert(joFromFile.has("cols") && joFromFile.has("rows") && joFromFile.has("width") && joFromFile.has("height"));
		Simulator sim = new Simulator(joFromFile.getInt("cols"),
				joFromFile.getInt("rows"),
				joFromFile.getInt("width"),
				joFromFile.getInt("height"),
				_animal_factory, _region_factory);
		Controller controller = new Controller(sim);
		controller.load_data(joFromFile);
		controller.run(_time, _delta_time, _sv, out);
		is.close();
	}

	private static void start_GUI_mode() throws Exception {
		int cols, rows, width, height;
		JSONObject joFromFile = null;
		if (_in_file != null) {
			InputStream is = new FileInputStream(new File(_in_file));
			joFromFile = new JSONObject(new JSONTokener(is));
			cols = joFromFile.getInt("cols");
			rows = joFromFile.getInt("rows");
			width = joFromFile.getInt("width");
			height = joFromFile.getInt("height");
			is.close();
		} else {
			cols = 20;
			rows = 15;
			width = 800;
			height = 600;
		}
		Simulator sim = new Simulator(cols, rows, width, height,_animal_factory, _region_factory);
		Controller controller = new Controller(sim);
		if (joFromFile != null) {
			controller.load_data(joFromFile);
		}
		SwingUtilities.invokeAndWait(() -> new MainWindow(controller));
		
//		throw new UnsupportedOperationException("GUI mode is not ready yet ..."); 	TODO
	}

	private static void start(String[] args) throws Exception {
		init_factories();
		parse_args(args);
		switch (_mode) {
		case BATCH:
			start_batch_mode();
			break;
		case GUI:
			start_GUI_mode();
			break;
		}
	}
	
	public static Factory<SelectionStrategy> getStrategyFactory(){
		return _strategy_factory;
	}
	
	public static Factory<Animal> getAnimalFactory(){
		return _animal_factory;
	}
	
	public static Factory<Region> getRegionFactory(){
		return _region_factory;
	}
	
	public static double getDeltaTime(){
		return _delta_time;
	}

	
	public static void main(String[] args) {
		Utils._rand.setSeed(2147483647l);
		try {
			start(args);
		} catch (Exception e) {
			System.err.println("Something went wrong ...");
			System.err.println();
			e.printStackTrace();
		}
	} 
	
	
}
