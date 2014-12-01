package view;

import java.awt.Color;
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

public class MarksTable extends JTable {
	private final Font boldFont = new Font("Verdana", Font.BOLD, 12);
	private final Font plainFont = new Font("Verdana", Font.PLAIN, 12);
	private final Color debtColor = new Color(255, 234, 225);
	private MarksTableModel model;
	public MarksTable() {
		super();
		getTableHeader().setReorderingAllowed(false);
		getTableHeader().setResizingAllowed(false);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}
	
	public void adjustColumnSizes(int margin) {
		JTable table = this;
        DefaultTableColumnModel colModel = 
        		(DefaultTableColumnModel) table.getColumnModel();
        
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
		if(model.isNeedToHighlight(row, col)) {
			c.setFont( boldFont );
			c.setBackground(Color.white);
		} else {
			c.setFont( plainFont );
			if(model.isDebt(row, col)) {
				c.setBackground(debtColor);
			} else {
				c.setBackground(Color.white);
			}
		}
		return c;
	}
}
