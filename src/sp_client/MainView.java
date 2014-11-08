package sp_client;

import sp_entities.UserStatus;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

public class MainView extends JFrame implements Observer {
	private MainModel model;
	private MainController controller;
	private JList<String> mainList;
	
	public MainView(MainModel m, UserStatus status) {
		super("Анализ успеваемости");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		model = m;
		model.addObserver(this);
		
		ArrayList<String> values = new ArrayList<>();
		for(int i=0; i<5; i++) 
			values.add("Семестр " + (i+1));
		Object[] objData = values.toArray();
		String[] stringData = Arrays.copyOf(objData, objData.length, String[].class);
		mainList = new JList<>(stringData);
		mainList.setName("first list");
		
		JScrollPane listScroller = new JScrollPane(mainList);
		listScroller.setPreferredSize(new Dimension(380, 280));
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(400,300));
		panel.add(listScroller);
		
		this.add(panel);
		this.pack();
		this.setVisible(true);
		
		controller = new MainController(model, status);
		mainList.addMouseListener(controller);
	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println("update on view");
		MainModel.MainEvent event = (MainModel.MainEvent)arg;
		switch(event) {
		case SEMESTERS:
			model.getSemesters().print();
			break;
		}
	}
	
}
