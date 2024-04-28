package simulator.view;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import simulator.misc.Vector2D;
import simulator.model.animals.AnimalInfo;
import simulator.model.animals.State;
import simulator.model.regions.MapInfo;

@SuppressWarnings("serial")
public class MapViewer extends AbstractMapViewer {

	// Anchura/altura/ de la simulación -- se supone que siempre van a ser iguales
	// al tamaño del componente
	private int _width;
	private int _height;

	// Número de filas/columnas de la simulación
	private int _rows;
	private int _cols;

	// Anchura/altura de una región - Changes to double due to the same reason as in the first hand in of the assignment
	double _rwidth;
	double _rheight;

	// Mostramos sólo animales con este estado. Los posibles valores de _currState
	// son null, y los valores deAnimal.State.values(). Si es null mostramos todo.
	State _currState;
	int indexState;

	// En estos atributos guardamos la lista de animales y el tiempo que hemos
	// recibido la última vez para dibujarlos.
	volatile private Collection<AnimalInfo> _objs;
	volatile private Double _time;

	// Una clase auxilar para almacenar información sobre una especie
	private static class SpeciesInfo {
		private Integer _count;
		private Color _color;

		SpeciesInfo(Color color) {
			_count = 0;
			_color = color;
		}
		
		public void incrementCount() {
			++_count;
		}
		
		public void resetCount() {
			_count = 0;
		}
		
		public int getCount() {
			return _count;
		}
		
		public Color getColor() {
			return _color;
		}
	}

	// Un mapa para la información sobre las especies
	Map<String, SpeciesInfo> _kindsInfo = new HashMap<>();

	// El font que usamos para dibujar texto
	private Font _font = new Font("Arial", Font.BOLD, 12);

	// Indica si mostramos el texto la ayuda o no
	private boolean _showHelp;

	public MapViewer() {
		indexState = 0;
		initGUI();
	}

	private void initGUI() {

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (Character.toLowerCase(e.getKeyChar())) {
				case 'h':
					_showHelp = !_showHelp;
					repaint();
					break;
				case 's':
					if (indexState != State.values().length - 1) {
						_currState = State.values()[indexState]; //not breaking encapsulation
						++indexState;
					} else {
						_currState = null;
						indexState = 0;
					}
					repaint();
				default:
				}
			}

		});

		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent e) {
				requestFocus(); // Esto es necesario para capturar las teclas cuando el ratón está sobre este
								// componente.
			}
		});

		// Por defecto mostramos todos los animales
		_currState = null;

		// Por defecto mostramos el texto de ayuda
		_showHelp = true;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D gr = (Graphics2D) g;
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// Cambiar el font para dibujar texto
		g.setFont(_font);

		// Dibujar fondo blanco
		gr.setBackground(Color.WHITE);
		gr.clearRect(0, 0, _width, _height);

		// Dibujar los animales, el tiempo, etc.
		if (_objs != null)
			drawObjects(gr, _objs, _time);

		if (_showHelp) {
			Color auxColor = gr.getColor();
			gr.setColor(Color.RED);
			gr.drawString("h: toggle help", 10, 20);
			gr.drawString("s: show animals of a specific state", 10, 35);
			gr.setColor(auxColor);
			
		}
		// h: toggle help
		// s: show animals of a specific state

	}

	private boolean visible(AnimalInfo a) {
		return _currState == null || _currState == a.get_state();
	}

	/**
	 * Draws the cells, the animals and the information labels in the map.
	 * @param g Graphics of the component.
	 * @param animals Collection of animals in the simulator.
	 * @param time Time of the simulation that is running (used to display it in the map).
	 */
	private void drawObjects(Graphics2D g, Collection<AnimalInfo> animals, Double time) {
		
		//Gets the component color at the beginning of the method to restore it at the end
		Color c = g.getColor();
		//Draw cells
		for (int i = 0; i < _rows; ++i) {
			for (int j = 0; j < _cols; ++j) {
				drawCell(i, j, g); //draws the cell in the map
			}
		}

		//Draw animals
		for (String kind : _kindsInfo.keySet()) {
			SpeciesInfo auxSpeciesInfo = _kindsInfo.get(kind);
			auxSpeciesInfo.resetCount();
		}
		for (AnimalInfo a : animals) {

			// Si no es visible saltamos la iteración
			if (!visible(a))
				continue;

			// La información sobre la especie de 'a'
			SpeciesInfo esp_info = _kindsInfo.get(a.get_genetic_code());
			if (esp_info == null) {
				esp_info = new SpeciesInfo(ViewUtils.get_color(a.get_genetic_code()));
				_kindsInfo.put(a.get_genetic_code(), esp_info);
			}
			esp_info.incrementCount();
			Vector2D aux = a.get_position();
			int x = (int) Math.round(aux.getX());
			int y = (int) Math.round(aux.getY());
			int size = (int) Math.round(a.get_age() / 2 + 2);
			
			g.setColor(esp_info.getColor());
			g.fillRect(x, y, size, size);

		}
		
		//Draw information labels
		drawInfoRectangles(g, time);
		g.setColor(c);
		
	}
	
	/**
	 * Draws a single cell in the map as a rectangle using the Graphics object.
	 * @param row Row of the cell to be drawn.
	 * @param col Column of the cell to be drawn.
	 * @param g Graphics instance.
	 */
	private void drawCell(int row, int col, Graphics g) {
		int x = (int) Math.round(col * _rwidth);
		int y = (int) Math.round(row * _rheight);
		g.setColor(Color.LIGHT_GRAY);
		g.drawRect(x, y, (int)Math.round(_rwidth), (int)Math.round(_rheight));
		
	}
	
	/**
	 * Draws the information labels in the map.
	 * @param g Graphics instance.
	 * @param time Time of simulation running.
	 */
	private void drawInfoRectangles(Graphics2D g, Double time) {
		int posIndex = 0;
		final int DISTANCE_BETWEEN_RECTANGLES = 18;
		final int OFFSET = -5;
		
		//State tag
		g.setColor(Color.BLUE);
		if (_currState != null) {
			drawStringWithRect(g, 20, _height - DISTANCE_BETWEEN_RECTANGLES * posIndex + OFFSET, String.format("State: %s", _currState.toString()));
			++posIndex;
		}
		
		//Time tag
		g.setColor(Color.MAGENTA);
		drawStringWithRect(g, 20, _height - DISTANCE_BETWEEN_RECTANGLES * posIndex + OFFSET, String.format("Time: %.3f", time));
		++posIndex;
		
		//Species tag
		for (Entry<String, SpeciesInfo> e : _kindsInfo.entrySet()) {
			SpeciesInfo info = e.getValue();
			if (info.getCount() > 0) {
				g.setColor(info._color);
				drawStringWithRect(g, 20, _height - DISTANCE_BETWEEN_RECTANGLES * posIndex + OFFSET, String.format("%s: %d", e.getKey(), info.getCount()));
				++posIndex;
			}
		}
		
	}

	// Un método que dibujar un texto con un rectángulo
	void drawStringWithRect(Graphics2D g, int x, int y, String s) {
		Rectangle2D rect = g.getFontMetrics().getStringBounds(s, g);
		g.drawString(s, x, y);
		g.drawRect(x - 1, y - (int) rect.getHeight(), (int) rect.getWidth() + 1, (int) rect.getHeight() + 5);
	}

	@Override
	public void update(List<AnimalInfo> objs, Double time) {
		this._objs = objs;
		this._time = time;
		repaint();
		// repaint() para redibujar el componente.
	}

	@Override
	public void reset(double time, MapInfo map, List<AnimalInfo> animals) {
		_width = map.get_width();
		_height = map.get_height();
		_cols = map.get_cols();
		_rows = map.get_rows();
		_rwidth = map.get_region_width();
		_rheight = map.get_region_height();
		_currState = null;
		indexState = 0;
		

		// Esto cambia el tamaño del componente, y así cambia el tamaño de la ventana
		// porque en MapWindow llamamos a pack() después de llamar a reset
		setPreferredSize(new Dimension(map.get_width(), map.get_height()));

		// Dibuja el estado
		update(animals, time);
	}





}
