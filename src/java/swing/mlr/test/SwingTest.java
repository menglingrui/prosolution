package java.swing.mlr.test;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class SwingTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		  TableModel dataModel = new AbstractTableModel() {
	          public int getColumnCount() { return 10; }
	          public int getRowCount() { return 10;}
	          public Object getValueAt(int row, int col) { return new Integer(row*col); }
	      };
	      JTable table = new JTable(dataModel);
	      JScrollPane scrollpane = new JScrollPane(table);
	      
	      scrollpane.show();
	      

	}

}
