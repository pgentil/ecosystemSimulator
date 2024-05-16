package simulator.view;

import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JButton;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.json.JSONArray;
import org.json.JSONObject;
import simulator.control.Controller;
import simulator.launcher.Main;
import simulator.model.EcoSysObserver;
import simulator.model.animals.AnimalInfo;
import simulator.model.regions.MapInfo;
import simulator.model.regions.RegionInfo;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;


public class ChangeRegionsDialog extends JDialog implements EcoSysObserver{
	
	private DefaultComboBoxModel<String> _regionsModel;
	private DefaultComboBoxModel<String> _fromRowModel;
	private DefaultComboBoxModel<String> _toRowModel;
	private DefaultComboBoxModel<String> _fromColModel;
	private DefaultComboBoxModel<String> _toColModel;
	
	private DefaultTableModel _dataTableModel;
	
	private Controller _ctrl;
	private List<JSONObject> _regionsInfo;
	private String[] _headers = { "Key", "Value", "Description" };
	int _status; 
	
	ChangeRegionsDialog(Controller ctrl) {
		 super((Frame)null, true);
		_ctrl = ctrl;
		initGUI();
		_ctrl.addObserver(this);
	}
	private void initGUI() {
		setTitle("Change Regions");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);
		
		JPanel helpPanel = new JPanel();
		mainPanel.add(helpPanel);
		/**
		 * creates a textArea that outputs the needed text. In this case we made it uneditable as well as made the text wrap 
		 * once it reaches the end of the line, and set the font and size to 14. The size has 20 pixels less than that of the
		 * text panel so as not to make the text start so close to the border. 
		 */
		JTextArea textArea = new JTextArea();
		textArea.setOpaque(false);
		textArea.setText("Select a region type, the rows/cols "
				+ "interval, and provide values for the parameters in the value column "
				+ "(default values are used for parameters with no value)");
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setPreferredSize(new Dimension(780, 50));
        Font font = new Font("Arial", Font.PLAIN, 14);
        textArea.setFont(font);
        textArea.setEditable(false);
		helpPanel.add(textArea);
		mainPanel.add(Box.createRigidArea( new Dimension(780,10) ));
		
		/**
		 * Creates a table with only the first column editable 
		 */
		JPanel tablePanel = new JPanel();
		mainPanel.add(tablePanel);
		_dataTableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 1;
			}
			
			
		};
		_dataTableModel.setColumnIdentifiers(_headers);
	
		JTable t1 = new JTable(_dataTableModel);
		t1.setPreferredSize(new Dimension(780, 200));
		
		TableColumn firstColumn = t1.getColumn("Description");		
		firstColumn.setPreferredWidth(400);  // Preferred width
     
		JScrollPane tableScroll = new JScrollPane(t1);
		tableScroll.setPreferredSize(new Dimension (780, 200));
		t1.setFillsViewportHeight(true);
		tablePanel.add(tableScroll);
		
		
		
		
		_regionsInfo = Main.getRegionFactory().get_info();
		_regionsModel = new DefaultComboBoxModel<>();
		
		/**
		 * to add all the region types into the corresponding combo box
		 */
		for(JSONObject rI: _regionsInfo)
			_regionsModel.addElement(rI.getString("type")); 
		
		
		JPanel comboPanel = new JPanel();
		mainPanel.add(comboPanel);	
		
		JLabel l1 = new JLabel("Region type:" );
		comboPanel.add(l1);
		JComboBox<String> regionsCB = new JComboBox<>(_regionsModel); 
		comboPanel.add(regionsCB);
        regionsCB.addActionListener(e -> updateDataTable(regionsCB.getSelectedIndex())); 

		
		_fromRowModel = new DefaultComboBoxModel<>();
		_toRowModel = new DefaultComboBoxModel<>();
		_fromColModel = new DefaultComboBoxModel<>();
		_toColModel = new DefaultComboBoxModel<>();
		/**
		 * label l2 and l3 stand for label 2 and label 3, in eclipse the L(l) and 1(one) look very similar
		 */
		JLabel l2 = new JLabel("Row from/to: " );
		comboPanel.add(l2);
		/**
		 * creates the different comboboxes needed using the corresponding models 
		 */
		JComboBox<String> fromRowCB = new JComboBox<>(_fromRowModel); 
		comboPanel.add(fromRowCB);
		JComboBox<String> toRowCB = new JComboBox<>(_toRowModel); 
		comboPanel.add(toRowCB);
		JLabel l3 = new JLabel("Column from/to:" );
		comboPanel.add(l3);
		JComboBox<String> fromColCB = new JComboBox<>(_fromColModel);
		comboPanel.add(fromColCB);
		JComboBox<String> toColCB = new JComboBox<>(_toColModel);
		comboPanel.add(toColCB);

		JPanel buttonPanel = new JPanel();
		mainPanel.add(buttonPanel);	
		
		/*
		 * OK BUTTON: Gets the region_data from _dataTableModel and stores into a string. Gets region_type 
		 * from _regionsInfo using the regions combo box selection and stores it into a string. Creates the fields
		 * necessary for the JSON elements of a region, like the row array, the column array and the specification object.
		 * Fills the information into these JSON elements and then inserts these JSON elements into a JSONObject. Afterwards,
		 * it allocates the JSONObject into a JSONArray that will be mapped to the "regions" key. Then it calls the controller
		 * set_regions, sets the status attribute to 1 and sets the dialog's visibility to false.
		 * */
		JButton okButton = new JButton("OK");
		buttonPanel.add(okButton);
		okButton.addActionListener(e -> {
			JSONObject region_data = new JSONObject();
			for (int i = 0; i < this._dataTableModel.getRowCount(); ++i) {
				String value = (String) this._dataTableModel.getValueAt(i, 1);
				if (value != null) {
					region_data.put((String) this._dataTableModel.getValueAt(i,  0), value);
				}
			}
			JSONObject regionInfo = _regionsInfo.get(regionsCB.getSelectedIndex());
			String region_type = regionInfo.getString("type");
			JSONObject region = new JSONObject();
			JSONArray rowFromTo = new JSONArray();
			JSONArray colFromTo = new JSONArray();
			JSONObject spec = new JSONObject();
			JSONArray regionsArray = new JSONArray();
			
			Object row_from = fromRowCB.getSelectedItem();
			Object row_to = toRowCB.getSelectedItem();
			Object col_from = fromColCB.getSelectedItem();
			Object col_to = toColCB.getSelectedItem();
			
			rowFromTo.put(row_from);
			rowFromTo.put(row_to);
			colFromTo.put(col_from);
			colFromTo.put(col_to);
			region.put("row", rowFromTo);
			region.put("col", colFromTo);
			spec.put("type", region_type);
			spec.put("data", region_data);
			region.put("spec", spec);
			
			regionsArray.put(region);
			JSONObject regions = new JSONObject();
			regions.put("regions", regionsArray);
			_ctrl.set_regions(regions);		
			_status = 1;
			setVisible(false);
	    });		
		
		
        JButton cancelButton = new JButton("Cancel");
		buttonPanel.add(cancelButton);
		cancelButton.addActionListener(e -> {
		        _status = 0; 
		        setVisible(false); 
		    });		


		setPreferredSize(new Dimension(800, 400)); 
		pack();
		setResizable(false);
		setVisible(false);
	}
	/**
	 * given the selected index, grabs all the data from the regionsInfo and iterates through the different keys to add the
	 * key and its description to the first and third row of the table, while being able to edit the second row (its value)
	 * @param selectedIndex
	 */
	private void updateDataTable(int selectedIndex) { 
		if (selectedIndex != -1) {
            JSONObject regionInfo = _regionsInfo.get(selectedIndex);
            JSONObject data = regionInfo.getJSONObject("data");
            _dataTableModel.setRowCount(0);
            for (String key : data.keySet()) {
                String description = data.getString(key);
                _dataTableModel.addRow(new Object[]{key, "", description});
            }
        }
	}
	public void open(Frame parent) {
		setLocation(
				parent.getLocation().x + parent.getWidth() / 2 - getWidth() / 2,
				parent.getLocation().y + parent.getHeight() / 2 - getHeight() / 2);
		pack();
		setVisible(true);
	}

	/**
	 * updates the combo box indexes showing the current rows and cols 
	 * @param row
	 * @param col
	 */
	void setComboBox(int row, int col)
	{
		for(int i = 0; i < row; i++)
		{
			_fromRowModel.addElement(Integer.toString(i));
			_toRowModel.addElement(Integer.toString(i));
		}
		for(int i = 0; i < col; i++)
		{
			_fromColModel.addElement(Integer.toString(i));
			_toColModel.addElement(Integer.toString(i));
		}		
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		setComboBox(map.get_rows(), map.get_cols());
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {	
	    setComboBox(map.get_rows(), map.get_cols());

	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		// TODO Auto-generated method stub

	}

}
