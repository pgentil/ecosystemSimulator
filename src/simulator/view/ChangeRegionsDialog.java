package simulator.view;

import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.launcher.Main;
import simulator.model.EcoSysObserver;
import simulator.model.animals.AnimalInfo;
import simulator.model.regions.MapInfo;
import simulator.model.regions.RegionInfo;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import javax.swing.JPanel;
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
	int _status; //idkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk
	
	// TODO if necessary, add attributes here�
	
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
		
//		
		JPanel tablePanel = new JPanel();
		mainPanel.add(tablePanel);
		
		JPanel comboPanel = new JPanel();
		mainPanel.add(comboPanel);	
		
		JPanel buttonPanel = new JPanel();
		mainPanel.add(buttonPanel);	

		_regionsInfo = Main.getRegionFactory().get_info();
		_dataTableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 1;
			}
		};
		_dataTableModel.setColumnIdentifiers(_headers);
		// TODO create a JTable that uses _dataTableModel and add it to dialog
		
		JTable t1 = new JTable(_dataTableModel);
		t1.setPreferredSize(new Dimension(780, 100));
		tablePanel.add(t1);
		// _regionsModel is a combobox model that includes the region types
		_regionsModel = new DefaultComboBoxModel<>();
		
		// TODO add the description of all the regions to _regionsModel, using
		// the key �desc� or �type� of the JSONObject in _regionsInfo, since
		// these give us information about what the factory can create.
		
		for(JSONObject rI: _regionsInfo)
			_regionsModel.addElement(rI.getString("type")); 
		
		
		// TODO create a combobox que uses _regionsModel and add it to dialog.
		JLabel l1 = new JLabel("Region type:" );
		comboPanel.add(l1);
		JComboBox<String> regionsCB = new JComboBox<>(_regionsModel); 
		comboPanel.add(regionsCB);
        regionsCB.addActionListener(e -> updateDataTable(regionsCB.getSelectedIndex())); //???????????????????????????

		
		// TODO create four models of combobox for _fromRowModel, _fromRowModel,
		// _fromColModel y _toColModel.
		
		_fromRowModel = new DefaultComboBoxModel<>();
		_toRowModel = new DefaultComboBoxModel<>();
		_fromColModel = new DefaultComboBoxModel<>();
		_toColModel = new DefaultComboBoxModel<>();
		
		// TODO create 4 combobox that use these models and add them to dialog.
		JLabel l2 = new JLabel("Row from/to: " );
		comboPanel.add(l2);
		JComboBox<String> fromRowCB = new JComboBox<>(_fromRowModel); //no fucking clue if its right
		comboPanel.add(fromRowCB);
		JComboBox<String> toRowCB = new JComboBox<>(_toRowModel); //no fucking clue if its right
		comboPanel.add(toRowCB);
		JLabel l3 = new JLabel("Column from/to:" );
		comboPanel.add(l3);
		JComboBox<String> fromColCB = new JComboBox<>(_fromColModel); //no fucking clue if its right
		comboPanel.add(fromColCB);
		JComboBox<String> toColCB = new JComboBox<>(_toColModel); //no fucking clue if its right
		comboPanel.add(toColCB);

		
		// TODO create the OK and Cancel buttons and add them to the dialog.
		
		JButton okButton = new JButton("OK");
		buttonPanel.add(okButton);
		okButton.addActionListener(e -> {
			
			
			
			
			
			
			
			
	    });		
		
		
        JButton cancelButton = new JButton("Cancel");
		buttonPanel.add(cancelButton);
		cancelButton.addActionListener(e -> {
		        _status = 0; // Set _status to 0
		        setVisible(false); // Make the dialog invisible
		    });		

		
		
		setPreferredSize(new Dimension(800, 400)); // puedes usar otro tama�o
		pack();
		setResizable(false);
		setVisible(false);
	}
	private void updateDataTable(int selectedIndex) { //99% sure this is worng
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
	// TODO the rest of the methods go here...




	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		//onReset(time, map, animals);
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
//	    _fromRowModel.removeAllElements();
//	    _toRowModel.removeAllElements();
//	    _fromColModel.removeAllElements();
//	    _toColModel.removeAllElements();
//
//	    for (int i = 0; i < map.get_rows(); i++) {
//	        _fromRowModel.addElement(Integer.toString(i));
//	        _toRowModel.addElement(Integer.toString(i));
//	    }
//
//	    for (int j = 0; j < map.get_cols(); j++) {
//	        _fromColModel.addElement(Integer.toString(j));
//	        _toColModel.addElement(Integer.toString(j));
//	    }
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
