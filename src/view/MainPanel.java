package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
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
	private static final int TOP_PANEL_Y = 100, BOTTOM_PANEL_Y = 50;
	private static final int TOP_HOR_MARGIN = 10 + PANEL_X/30;;
	private static final int SCROLL_HOR_MARGIN = TOP_HOR_MARGIN;
	private static final int BOTTOM_HOR_MARGIN = SCROLL_HOR_MARGIN;
	private JPanel topPanel;
	private JPanel historyPanel;
	private Image arrowImage;
	private int hSize;
	
	private MyList mainList;
	private JScrollPane mainScroll;
	private MarksTable marksTable;
	
	private JPanel bottomPanel;
	private JButton backButton;
	private JButton nextButton;

	public MainPanel(MainModel model, MainController controller) {
		super(new FlowLayout(FlowLayout.CENTER, 0, 0));
		this.model = model;
		this.controller = controller;
		hSize = 0;
		marksTable = new MarksTable();
		
		try {
			arrowImage = ImageIO.read(new File("history_arrow.png"))
					.getScaledInstance(20, 20, java.awt.Image.SCALE_AREA_AVERAGING);
		} catch (IOException e) {
			System.out.println("Error! Arrow icon not found");
		}
		File currentDir = new File("");
		System.out.println( currentDir.getAbsolutePath() );
		
		historyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
		historyPanel.setPreferredSize(
				new Dimension(PANEL_X - 2*TOP_HOR_MARGIN, TOP_PANEL_Y));
	}
	
	public void showMainPage(AuthData data) {
		this.removeAll();

		topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
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

		mainList = new MyList();
		mainList.setFixedCellHeight(PANEL_Y/15);
		mainList.addMouseListener(controller);
		mainScroll = new JScrollPane(mainList);
		mainScroll.setPreferredSize(
				new Dimension(PANEL_X - SCROLL_HOR_MARGIN*2, 
						PANEL_Y - TOP_PANEL_Y - BOTTOM_PANEL_Y));
		setListData(model.getRoles());
		this.add(mainScroll);
		
		bottomPanel = new JPanel(new GridBagLayout());
		bottomPanel.setPreferredSize(
				new Dimension(PANEL_X - 2*BOTTOM_HOR_MARGIN, BOTTOM_PANEL_Y));
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = c.gridy = 0;
		c.weightx = 1;
		backButton = new JButton("Назад");
		backButton.setActionCommand(MainView.BACK_BUTTON_COMMAND);
		backButton.addActionListener(controller);
		backButton.setEnabled(false);
		bottomPanel.add(backButton, c);
		
		c.gridx = 1;
		c.anchor = GridBagConstraints.LINE_END;
		nextButton = new JButton("Далее");
		nextButton.setVisible(false); //TODO DELETE OR SHOW
		bottomPanel.add(nextButton, c);
		
		this.add(bottomPanel);
		
		setPreferredSize(new Dimension(PANEL_X, PANEL_Y));
		
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
		mainList.setListData(data);
	}

	public void addEventToHistory(String eventName) {
		System.out.println("add event" + eventName);
		
		JButton button = new JButton(eventName);
		button.setActionCommand(MainView.HISTORY_BUTTON_COMMAND + (hSize++));
		button.addActionListener(controller);
		if(hSize > 1) {
			JLabel label = new JLabel(new ImageIcon(arrowImage));
			historyPanel.add(label);
			backButton.setEnabled(true);
		}
		historyPanel.add(button);
		historyPanel.revalidate();
		System.out.println("add event " + eventName + " : " + MainView.HISTORY_BUTTON_COMMAND + (hSize-1));
	}

	public void setHistoryPosition(int pos) {
		assert (pos < hSize);
		while (hSize > pos + 1) {
			historyPanel.remove(2*pos + 1);
			historyPanel.remove(2*pos + 1);
			hSize--;
		}
		if(hSize == 1) {
			backButton.setEnabled(false);
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
