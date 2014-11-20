package view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import controller.MainController;
import model.MainModel;
import sp_entities.AuthData;
import sp_entities.GroupStageMarks;
import sp_entities.GroupSubjectMarks;
import sp_entities.IMarks;
import sp_entities.StudentSemMarks;

public class MainPanel extends JPanel {
	private MainModel model;
	private MainController controller;

	private static final int PANEL_X = 800, PANEL_Y = 600;
	private static final int TOP_PANEL_Y = 100;
	private JPanel topPanel;
	private JPanel historyPanel;
	private int hSize;
	private JList<String> mainList;
	private JScrollPane mainScroll;
	private DefaultListModel<String> mainListModel;
	private MarksTable marksTable;

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
		mainList.setFixedCellHeight(PANEL_Y/15);
		mainList.addMouseListener(controller);
		mainScroll = new JScrollPane(mainList);
		mainScroll.setPreferredSize(new Dimension(PANEL_X-10, PANEL_Y
				- TOP_PANEL_Y-10));
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
		System.out.println("set list data");
		if(!mainScroll.getViewport().getView().equals(mainList)) {
			System.out.println("switch main scroll to list");
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

	public void showMarks(IMarks m) {
		m.printMarks();
		System.out.println("show marks");
		marksTable.setContent(m);
		mainScroll.setViewportView(marksTable);
		mainScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		mainScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		mainScroll.revalidate();
		mainScroll.repaint();
	}
}
