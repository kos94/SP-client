package view;

import sp_entities.GroupStageMarks;
import sp_entities.StudentMarks;

public class StageMarksModel extends MarksTableModel {
	private GroupStageMarks marks;
	public StageMarksModel(GroupStageMarks marks) {
		this.marks = marks;
	}
	@Override
	public int getColumnCount() {
		return marks.getSubjects().size() + 3;
	}

	@Override
	public int getRowCount() {
		return marks.getStudentsNumber() + 2;
	}

	@Override
	public Object getValueAt(int row, int col) {
		int nSubj = marks.getSubjects().size();
		int nStud = marks.getStudentsNumber();
		StudentMarks sm = marks.getStudentMark(row);
		if(col >= nSubj + 1 && row >= nStud) return "";
		if(col == 0) {
			if(row == nStud) return "������: ";
			if(row == nStud + 1) return "������� ����: ";
			return sm.student;
		} else {
			if(row == nStud) {
				return marks.getSubjDebts().get(col-1);
			}
			if(row == nStud + 1) {
				return marks.getSubjAvg().get(col-1);
			}
			if(col == nSubj + 1) {
				return marks.getStudentDebts().get(row);
			}
			if(col == nSubj + 2) {
				return marks.getStudentAvg().get(row);
			}
			return sm.marks.get(col-1);
		}
	}
	
	@Override
	public String getColumnName(int col) {
		int nSubj = marks.getSubjects().size();
		if(col == 0) return "�������";
		if(col == nSubj + 1) return "������";
		if(col == nSubj + 2) return "������� ����";
		return marks.getSubjects().get(col-1);
	}
	@Override
	public boolean isNeedToHighlight(int row, int col) {
		int nSubj = marks.getSubjects().size();
		int nStud = marks.getStudentsNumber();
		return (row >= nStud || col >= nSubj + 1);
	}
}
