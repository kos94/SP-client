package view;

import javax.swing.table.AbstractTableModel;

public abstract class MarksTableModel extends AbstractTableModel {
	public abstract boolean isNeedToHighlight(int row, int col);
}
