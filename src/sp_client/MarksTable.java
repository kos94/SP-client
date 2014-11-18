package sp_client;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import sp_entities.GroupStageMarks;
import sp_entities.GroupSubjectMarks;
import sp_entities.StudentMarks;
import sp_entities.StudentSemMarks;

class SubjectMarksModel extends AbstractTableModel {
	private GroupSubjectMarks marks;
	private final static String[] COL_NAMES = {"Студент", "1-й модуль", "2-й модуль", "Итог"};
	
	public SubjectMarksModel(GroupSubjectMarks marks) {
		this.marks = marks;
	}
	@Override
	public int getColumnCount() { return 4; }

	@Override
	public int getRowCount() {
		return marks.getMarksNumber();
	}

	@Override
	public Object getValueAt(int row, int col) {
		StudentMarks sm = marks.getStudentMark(row);
		if(col == 0) return sm.student;
		else return sm.marks.get(col-1);
	}
	
	@Override
	public String getColumnName(int col) {
		return COL_NAMES[col];
	}
}

class StageMarksModel extends AbstractTableModel {
	private GroupStageMarks marks;
	public StageMarksModel(GroupStageMarks marks) {
		this.marks = marks;
	}
	@Override
	public int getColumnCount() {
		return marks.getSubjects().size() + 1;
	}

	@Override
	public int getRowCount() {
		return marks.getStudentsNumber();
	}

	@Override
	public Object getValueAt(int row, int col) {
		StudentMarks sm = marks.getStudentMark(row);
		if(col == 0) return sm.student;
		else return sm.marks.get(col-1);
	}
	
	@Override
	public String getColumnName(int col) {
		if(col == 0) return "Студент";
		return marks.getSubjects().get(col-1);
	}
}

public class MarksTable extends JTable {
	public void setContent(GroupSubjectMarks m) {
		SubjectMarksModel model = new SubjectMarksModel(m);
		this.setModel(model);
	}
	public void setContent(GroupStageMarks m) {
		StageMarksModel model = new StageMarksModel(m);
		this.setModel(model);
	}
	public void setContent(StudentSemMarks m) {
		//TODO
//		this.setModel(model);
	}
}
