package view;

import javax.swing.table.AbstractTableModel;

import sp_entities.IMarks;

public abstract class MarksTableModel extends AbstractTableModel {
	public static final int ABSENT = IMarks.ABSENT;
	public static final int[] MIN_MARK = IMarks.MIN_MARK;
	public abstract boolean isNeedToHighlight(int row, int col);
	public abstract boolean isDebt(int row, int col);
}
