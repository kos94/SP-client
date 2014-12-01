package view;

import javax.swing.table.AbstractTableModel;

import sp_entities.GroupSubjectMarks;
import sp_entities.GroupSubjectMarks.StudentSubjMarks;;

public class SubjectMarksModel extends MarksTableModel {
	private GroupSubjectMarks marks;
	private final static String[] COL_NAMES = {"Студент", "1-й модуль", "2-й модуль", "Итог"};
	
	public SubjectMarksModel(GroupSubjectMarks marks) {
		this.marks = marks;
	}
	@Override
	public int getColumnCount() { return 4; }

	@Override
	public int getRowCount() {
		return marks.getMarksNumber() + 2;
	}

	@Override
	public Object getValueAt(int row, int col) {
		int marksNum = marks.getMarksNumber();
		if(row > marksNum - 1) {
			if(row == marksNum) {
				if (col == 0) return "Долгов: ";
				byte d = marks.getDebtCount(col - 1);
				return (d == ABSENT)? "" : d; 
			} else if (row == marksNum+1) {
				if( col == 0) return "Средний балл: ";
				float m = marks.getAvgMark(col - 1);
				return (m == ABSENT)? "" : m;
			}
		}
		StudentSubjMarks sm = marks.getStudentMark(row);
		if(col == 0) return sm.student;
		byte m = sm.marks[col - 1];
		return (m == ABSENT)? "" : m;
	}
	
	@Override
	public String getColumnName(int col) {
		return COL_NAMES[col];
	}
	@Override
	public boolean isNeedToHighlight(int row, int col) {
		return (row >= marks.getMarksNumber());
	}
	@Override
	public boolean isDebt(int row, int col) {
		if(col == 0 || isNeedToHighlight(row, col)) return false;
		int m = marks.getStudentMark(row).marks[col - 1];
		return (m != ABSENT && m < MIN_MARK[col - 1]);
	}
}