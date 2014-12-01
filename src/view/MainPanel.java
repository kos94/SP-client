package view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import controller.MainController;
import model.MainModel;
import sp_entities.AuthData;
import sp_entities.IMarks;

public class MainPanel extends JPanel {
	private MainModel model;
	private MainController controller;
	public GroupLayout layout;
	
	public static final int PANEL_X = 800, PANEL_Y = 600;
	private static final int TOP_PANEL_Y = 100, BOTTOM_PANEL_Y = 50;
	private static final int TOP_HOR_MARGIN = 10 + PANEL_X/30;;
	private static final int SCROLL_HOR_MARGIN = TOP_HOR_MARGIN;
	private JPanel topPanel;
	private boolean historyShowing;
	private Image arrowImage;
	private int hSize;
	
	private MyList list;
	private JScrollPane mainScroll;
	private MarksTable marksTable;

	public MainPanel(MainModel model, MainController controller) {
		this.model = model;
		this.controller = controller;
		
		topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
		topPanel.setPreferredSize(new Dimension(PANEL_X, TOP_PANEL_Y));
		
		list = new MyList();
		list.setFixedCellHeight(PANEL_Y/15);
		list.addMouseListener(controller);
		
		mainScroll = new JScrollPane(list);
		mainScroll.setPreferredSize(
				new Dimension(PANEL_X - SCROLL_HOR_MARGIN*2, 
						PANEL_Y - TOP_PANEL_Y - BOTTOM_PANEL_Y));
				
		layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(topPanel, GroupLayout.PREFERRED_SIZE, 
						GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addComponent(mainScroll));
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(topPanel, 100, GroupLayout.PREFERRED_SIZE, 3000)
						.addComponent(mainScroll)));
			        		
		try {
			arrowImage = ImageIO.read(new File("history_arrow.png"))
					.getScaledInstance(20, 20, java.awt.Image.SCALE_AREA_AVERAGING);
		} catch (IOException e) {
			System.out.println("Error! Arrow icon not found");
		}
		
		marksTable = new MarksTable();
	}
	
	public boolean isHistoryShowing() { 
		return historyShowing;
	}
	
	public void showMainPage(AuthData data) {
		topPanel.removeAll();
		topPanel.repaint();
		hSize = 0;
		topPanel.add(new JLabel("Имя: " + data.getName(), SwingConstants.LEFT));
		String dep = data.getDepartment();
		if (dep != null) {
			topPanel.add(new JLabel("Кафедра: " + dep));
		}
		String group = data.getGroup();
		if (group != null) {
			topPanel.add(new JLabel("Группа: " + group));
		}

		list.listenToList(true);
		setListData(model.getRoles());
	}

	public void showHistoryPanel() {
		topPanel.removeAll();
		topPanel.setPreferredSize(new Dimension(PANEL_X, TOP_PANEL_Y));
		topPanel.revalidate();
		topPanel.repaint();
		historyShowing = true;
	}

	public void setListData(List<String> data) {		
		if(!mainScroll.getViewport().getView().equals(list)) {
			mainScroll.setViewportView(list);
		}
		list.setListData(data);
	}

	public void addEventToHistory(String eventName) {
		JButton button = new JButton(eventName);
		button.setActionCommand(MainView.HISTORY_BUTTON_COMMAND + (hSize++));
		button.addActionListener(controller);

		if(hSize > 1) {
			JLabel label = new JLabel(new ImageIcon(arrowImage));
			topPanel.add(label);
		}
		topPanel.add(button);
		topPanel.revalidate();
	}

	public void setHistoryPosition(int pos) {
		assert (pos < hSize);
		while (hSize > pos + 1) {
			topPanel.remove(2 * pos + 1);
			topPanel.remove(2 * pos + 1);
			hSize--;
		}
		topPanel.revalidate();
		topPanel.repaint();
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
