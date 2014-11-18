package sp_client;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import sp_entities.AuthData;
import sp_entities.GroupStageMarks;
import sp_entities.GroupSubjectMarks;
import sp_entities.StudentSemMarks;

public class MainPanel extends JPanel {
	private MainModel model;
	private MainController controller;

	private static final int PANEL_X = 600, PANEL_Y = 400;
	private static final int TOP_PANEL_Y = 100;
	private JPanel topPanel;
	private JPanel historyPanel;
	private int hSize;
	private JList<String> mainList;
	private JScrollPane mainScroll;
	private DefaultListModel<String> mainListModel;
	private JTable marksTable;

	public MainPanel(MainModel model, MainController controller) {
		super();
		this.model = model;
		this.controller = controller;
		hSize = 0;
		mainListModel = new DefaultListModel<String>();
		marksTable = new MarksTable();
		
		historyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		historyPanel.setPreferredSize(new Dimension(PANEL_X, TOP_PANEL_Y));
	}
	
	public void showMainPage(AuthData data) {
		this.removeAll();

		topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		topPanel.setPreferredSize(new Dimension(PANEL_X, TOP_PANEL_Y));
		topPanel.add(new JLabel("Имя: " + data.getName(), SwingConstants.LEFT));
		String dep = data.getDepartment();
		if (dep != null) {
			topPanel.add(new JLabel("Кафедра: " + dep));
		}
		String group = data.getGroup();
		if (group != null) {
			topPanel.add(new JLabel("Группа: " + group));
		}

		this.add(topPanel);

		mainList = new JList<String>(mainListModel);
		mainList.addMouseListener(controller);
		mainScroll = new JScrollPane(mainList);
		mainScroll.setPreferredSize(new Dimension(PANEL_X, PANEL_Y
				- TOP_PANEL_Y));
		setListData(model.getRoles());

		setPreferredSize(new Dimension(PANEL_X, PANEL_Y));
		add(mainScroll);
		revalidate();
		repaint();
	}

	public void showHistoryPanel() {
		topPanel.removeAll();
		topPanel.add(historyPanel);
		topPanel.revalidate();
		topPanel.repaint();
	}

	public void setListData(List<String> data) {
		if(!mainScroll.getViewport().getView().equals(mainList)) {
			mainScroll.setViewportView(mainList);
		}
		
		mainListModel.removeAllElements();
		for (String s : data) {
			mainListModel.addElement(s);
		}
	}

	public void addEventToHistory(String eventName) {
		JButton button = new JButton(eventName);
		button.setActionCommand(MainView.HISTORY_BUTTON_COMMAND + (hSize++));
		button.addActionListener(controller);
		historyPanel.add(button);
		historyPanel.revalidate();
		System.out.println("add event " + eventName + " : " + MainView.HISTORY_BUTTON_COMMAND + (hSize-1));
	}

	public void setHistoryPosition(int pos) {
		assert (pos < hSize);
		while (hSize > pos + 1) {
			historyPanel.remove(pos + 1);
			hSize--;
		}
		historyPanel.revalidate();
		historyPanel.repaint();
	}

	public void showMarks(GroupSubjectMarks m) {
		((MarksTable)marksTable).setContent(m);
		placeMarksToScroll();
	}
	public void showMarks(GroupStageMarks m) {
		((MarksTable)marksTable).setContent(m);
		placeMarksToScroll();
	}
	public void showMarks(StudentSemMarks m) {
		((MarksTable)marksTable).setContent(m);
		placeMarksToScroll();
	}
	private void placeMarksToScroll() {
		mainScroll.setViewportView(marksTable);
		mainScroll.revalidate();
		mainScroll.repaint();
	}
}
