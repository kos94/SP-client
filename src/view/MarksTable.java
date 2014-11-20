package view;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import sp_entities.GroupStageMarks;
import sp_entities.GroupSubjectMarks;
import sp_entities.IMarks;
import sp_entities.StudentMarks;
import sp_entities.StudentSemMarks;
import sp_entities.SubjectMarks;

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
		System.out.println("get column count: " + marks.getSubjects().size()+1);
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
		else {
			System.out.println("get value called, col: " + col);
			System.out.println("marks size: " + sm.marks.size());
			return sm.marks.get(col-1);
		}
	}
	
	@Override
	public String getColumnName(int col) {
		if(col == 0) return "Студент";
		return marks.getSubjects().get(col-1);
	}
}

class StudentMarksModel extends AbstractTableModel {
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
		return marks.getSubjectsNumber();
	}

	@Override
	public Object getValueAt(int row, int col) {
		SubjectMarks m = marks.getSubjectMarks(row);
		if(col == 0) return m.subj;
		else return m.marks.get(col-1);
	}
	
	@Override
	public String getColumnName(int col) {
		return COL_NAMES[col];
	}
}

public class MarksTable extends JTable {
	public MarksTable() {
		super();
		getTableHeader().setReorderingAllowed(false);
		getTableHeader().setResizingAllowed(false);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}
	
	public void adjustColumnSizes(int margin) {
		JTable table = this;
        DefaultTableColumnModel colModel = (DefaultTableColumnModel) table.getColumnModel();
        
        for(int i=0; i<colModel.getColumnCount(); i++) {
        	TableColumn col = colModel.getColumn(i);
        	int width;

            TableCellRenderer renderer = col.getHeaderRenderer();
            if (renderer == null) {
                renderer = table.getTableHeader().getDefaultRenderer();
            }
            Component comp = renderer.getTableCellRendererComponent(
                    table, col.getHeaderValue(), false, false, 0, 0);
            width = comp.getPreferredSize().width;

            for (int r = 0; r < table.getRowCount(); r++) {
                renderer = table.getCellRenderer(r, i);
                comp = renderer.getTableCellRendererComponent(
                        table, table.getValueAt(r, i), false, false, r, i);
                int currentWidth = comp.getPreferredSize().width;
                width = Math.max(width, currentWidth);
            }

            width += 2 * margin;
            col.setPreferredWidth(width);
        }
    }
	
	public void setContent(IMarks m) {
		if(m instanceof GroupSubjectMarks) {
			setModel(new SubjectMarksModel((GroupSubjectMarks)m));
		} else if(m instanceof GroupStageMarks) {
			setModel(new StageMarksModel((GroupStageMarks)m));
		} else if(m instanceof StudentSemMarks) {
			setModel(new StudentMarksModel((StudentSemMarks)m));
		}
		adjustColumnSizes(5);
	}
	
	@Override
	public boolean getScrollableTracksViewportWidth()
    {
        return getPreferredSize().width < getParent().getWidth();
    }
}
