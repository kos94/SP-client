package view;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import sp_entities.GroupStageMarks;
import sp_entities.GroupSubjectMarks;
import sp_entities.IMarks;
import sp_entities.StudentSemMarks;
import sp_entities.SubjectMarks;

class StudentMarksModel extends MarksTableModel {
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
	@Override
	public boolean isNeedToHighlight(int row, int col) {
		// TODO Auto-generated method stub
		return false;
	}
}

public class MarksTable extends JTable {
	private final Font boldFont = new Font("Verdana", Font.BOLD, 12);
	private final Font plainFont = new Font("Verdana", Font.PLAIN, 12);
	
	private MarksTableModel model;
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
		System.out.println("set content");
		if(m instanceof GroupSubjectMarks) {
			model = new SubjectMarksModel((GroupSubjectMarks)m);
		} else if(m instanceof GroupStageMarks) {
			model = new StageMarksModel((GroupStageMarks)m);
		} else if(m instanceof StudentSemMarks) {
			model = new StudentMarksModel((StudentSemMarks)m);
		}
		setModel(model);
		adjustColumnSizes(5);
	}
	
	@Override
	public boolean getScrollableTracksViewportWidth() {
        return getPreferredSize().width < getParent().getWidth();
    }
	
	@Override 
	public Component prepareRenderer(
			TableCellRenderer renderer, int row, int col) {
		Component c = super.prepareRenderer(renderer, row, col);
		if(model.isNeedToHighlight(row, col)) 
			c.setFont( boldFont );
		else 
			c.setFont( plainFont );
		return c;
	}
}
