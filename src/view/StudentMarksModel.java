package view;

import sp_entities.StudentSemMarks;
import sp_entities.SubjectMarks;

public class StudentMarksModel extends MarksTableModel {
	private StudentSemMarks marks;
	private final static String[] COL_NAMES = {"Предмет", "1-й модуль", "2-й модуль", "Итог"};
	public StudentMarksModel(StudentSemMarks marks) {
		this.marks = marks;
	}
	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public int getRowCount() {
		return marks.getSubjectsNumber() + 2;
	}

	@Override
	public Object getValueAt(int row, int col) {
		int nSubj = marks.getSubjectsNumber();
		SubjectMarks sm = marks.getSubjectMarks(row);
		if(row >= nSubj) {
			if(row == nSubj) {
				if (col == 0) return "Долгов: ";
				byte d = marks.getDebtCount(col - 1);
				return (d == -1)? "" : d; 
			} else if (row == nSubj + 1) {
				if( col == 0) return "Средний балл: ";
				float m = marks.getAvgMark(col - 1);
				return (m == -1.0f)? "" : m;
			}
		}
		if(col == 0) return sm.subj;
		int mark = sm.marks.get(col - 1);
		return (mark == -1)? "" : mark;
	}
	
	@Override
	public String getColumnName(int col) {
		return COL_NAMES[col];
	}
	
	@Override
	public boolean isNeedToHighlight(int row, int col) {
		return (row >= marks.getSubjectsNumber());
	}
}