package simulator.view;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

public class InfoTable extends JPanel {
	
	String _title;
	TableModel _tableModel;
	 
	InfoTable(String title, TableModel tableModel) {
	_title = title;
	_tableModel = tableModel;
	initGUI();
	}

	private void initGUI() {
		 // TODO change the panel layout to BorderLayout()
		this.setLayout(new BorderLayout());
		 // TODO add a border with a title (using text _title) to the Jpanel
		this.setBorder(BorderFactory.createTitledBorder(_title));
		 // TODO add a JTable (with vertical scroll bar) that uses _tableModel
		JTable table = new JTable(_tableModel);
		JScrollPane sp = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane. HORIZONTAL_SCROLLBAR_NEVER);
		this.add(sp);
	}

}
