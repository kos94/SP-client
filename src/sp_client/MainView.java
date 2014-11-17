package sp_client;

import sp_entities.UserStatus;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

public class MainView extends JFrame implements Observer {
	public static final String LOGIN_ACTION_COMMAND = "login";
	private MainModel model;
	private MainController controller;
	private JList<String> mainList;
	private JTextField login;
	private JPasswordField password;
	private JButton loginButton;
	
	public MainView(MainModel m) {
		super("Анализ успеваемости");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		model = m;
		model.addObserver(this);
		
//		ArrayList<String> values = new ArrayList<>();
//		for(int i=0; i<5; i++) 
//			values.add("Семестр " + (i+1));
//		Object[] objData = values.toArray();
//		String[] stringData = Arrays.copyOf(objData, objData.length, String[].class);
//		mainList = new JList<>(stringData);
//		mainList.setName("first list");
//		
//		JScrollPane listScroller = new JScrollPane(mainList);
//		listScroller.setPreferredSize(new Dimension(380, 280));
//		JPanel panel = new JPanel();
//		panel.setPreferredSize(new Dimension(400,300));
//		panel.add(listScroller);
//		
//		this.add(panel);
//		this.pack();
		controller = new MainController(model, this);
//		mainList.addMouseListener(controller);
//		
//		//TODO delete this
//		this.setVisible(true);
	}

	private JPanel getLoginPanel() {
		GridBagConstraints c = new GridBagConstraints();
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		login = new JTextField(15);
		password = new JPasswordField(15);
		loginButton = new JButton("OK");
		
		c.gridx = 0; c.gridy = 0;
		c.gridwidth = 2;
		c.insets = new Insets(0,5,20,0);
		JLabel header = new JLabel("Вход");
		header.setFont(new Font(header.getFont().getName(), Font.PLAIN, 25));
		panel.add(header, c);
		
		c.gridy++;
		c.gridwidth = 1;
		panel.add(new JLabel("ID: "), c);
		c.gridx = 1;
		panel.add(login, c);
		
		c.gridx = 0; c.gridy++;
		panel.add(new JLabel("Пароль: "), c);
		c.gridx = 1;
		panel.add(password, c);
		
		c.insets = new Insets(0,0,0,0);
		c.gridx = 0; c.gridy++;
		c.gridwidth = 2;
		
		loginButton.setActionCommand(LOGIN_ACTION_COMMAND);
		loginButton.addActionListener(controller);
		panel.add(loginButton, c);
		return panel;
	}
	
	private void placeToCenter() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
	}
	
	public String getLogin() { 
		return login.getText();
	}
	
	public String getPassword() {
		return new String(password.getPassword());
	}
	
	@Override
	public void update(Observable o, Object arg) {
		System.out.println("update on view");
		MainModel.MainEvent event = (MainModel.MainEvent)arg;
		switch(event) {
		case AUTHORIZATION:
			System.out.println("show dialog");
			this.getContentPane().removeAll();
			this.add(getLoginPanel());
			this.pack();
			this.placeToCenter();
			this.setVisible(true);
			break;
		case SEMESTERS:
			this.setVisible(true);
			model.getSemesters().print();
			break;
		}
	}
	
	
}
