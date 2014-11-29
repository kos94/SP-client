package view;

import javax.swing.table.AbstractTableModel;

import sp_entities.GroupSubjectMarks;
import sp_entities.StudentMarks;

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
				byte d = marks.getDebtCount(col-1);
				return (d == -1)? "" : d; 
			} else if (row == marksNum+1) {
				if( col == 0) return "Средний балл: ";
				float m = marks.getAvgMark(col-1);
				return (m == -1.0f)? "" : m;
			}
		}
		StudentMarks sm = marks.getStudentMark(row);
		if(col == 0) return sm.student;
		byte m = sm.marks.get(col-1);
		return (m == -1)? "" : m;
	}
	
	@Override
	public String getColumnName(int col) {
		return COL_NAMES[col];
	}
	@Override
	public boolean isNeedToHighlight(int row, int col) {
		return (row >= marks.getMarksNumber());
	}
}