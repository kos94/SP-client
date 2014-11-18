package sp_client;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.*;

import sp_entities.AuthData;
import sp_entities.GroupSubjectMarks;

public class MainPanel extends JPanel {
	private MainModel model;
	private MainController controller;
	
	private static final int PANEL_X = 600, PANEL_Y = 400;
	private static final int TOP_PANEL_Y = 100;
	private JPanel topPanel;
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
		setListData(model.getRoles());
		mainList.addMouseListener(controller);
		
		mainScroll = new JScrollPane(mainList);
		mainScroll.setPreferredSize(new Dimension(PANEL_X, PANEL_Y-TOP_PANEL_Y));
		
		setPreferredSize(new Dimension(PANEL_X, PANEL_Y));
		add(mainScroll);
	}
	
	public void showHistoryPanel() {
		topPanel.removeAll();
		topPanel.revalidate();
		topPanel.repaint();
	}
	
	public void setListData(List<String> data) {
		mainListModel.removeAllElements();
		for(String s : data) {
			mainListModel.addElement(s);
		}
	}
	
	public void addEventToHistory(String eventName) {
		JButton button = new JButton(eventName);
		button.setActionCommand(MainView.HISTORY_BUTTON_COMMAND + (hSize++));
		button.addActionListener(controller);
		topPanel.add(button);
		topPanel.revalidate();
	}
	
	public void setHistoryPosition(int pos) {
		assert(pos < hSize);
		while(hSize > pos+1) {
			topPanel.remove(pos+1);
			hSize--;
		}
		topPanel.revalidate();
		topPanel.repaint();
	}
	
	public void showMarks(GroupSubjectMarks m) {
		marksTable = new JTable(5,5);
		mainScroll.setVisible(false);
		add(marksTable);
		revalidate();
		repaint();
	}
}
