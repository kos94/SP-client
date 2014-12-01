package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Collection;

import javax.swing.*;

public class MyList extends JList<String> {
	private int lastIndex;
	private DefaultListModel<String> mainListModel;
	private MouseMotionListener motionListener;
	
	class MyCellRenderer extends DefaultListCellRenderer {
		private Color hoverColor = new Color(222, 233, 243);
		
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {	
			Component row = super.getListCellRendererComponent(list, value,
					index, isSelected, cellHasFocus);
			if(index == lastIndex) row.setBackground(hoverColor);
			return row;
		}
		
	}
	
	public void listenToList() {
		addMouseMotionListener(motionListener);
	}
		
	public MyList() {
		super();
		
		lastIndex = -1;
		mainListModel = new DefaultListModel<String>();
		setModel(mainListModel);
		setFont(  new Font("Verdana", Font.PLAIN, 12) );
		
		
		motionListener = new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent ev) {}
			@Override
			public void mouseMoved(MouseEvent ev) {
				Point p = ev.getPoint();
				int index = locationToIndex(p);
				JList<String> list = (JList<String>)ev.getSource();
				if (getCellBounds(index, index).contains(p)) {
					if(index != lastIndex) {
						lastIndex = index;
						list.repaint();
					}
				} else {
					lastIndex = -1;
					list.repaint();
				}
			}
		};
		
		addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {}

			@Override
			public void mouseEntered(MouseEvent arg0) {}

			@Override
			public void mouseExited(MouseEvent arg0) {
				lastIndex = -1;
				((JList<?>)arg0.getSource()).repaint();
			}

			@Override
			public void mousePressed(MouseEvent arg0) {}

			@Override
			public void mouseReleased(MouseEvent arg0) {}
		});
		
		setCellRenderer(new MyCellRenderer());
	}
	
	public void setListData(Collection<String> data) {
		mainListModel.removeAllElements();
		for (String s : data) {
			mainListModel.addElement(s);
		}
	}
}
