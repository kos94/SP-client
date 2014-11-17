package sp_client;

import javax.swing.*;
import javax.swing.text.*;

import sp_entities.AuthData;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;
import java.util.List;

class DigitsFieldDocument extends PlainDocument {
	private int limit;

	DigitsFieldDocument(int limit) {
		super();
		this.limit = limit;
	}

	public void insertString(int offset, String str, AttributeSet attr)
			throws BadLocationException {
		if (str == null)
			return;
		if (str.matches("[0-9]+") && (getLength() + str.length()) <= limit) {
			super.insertString(offset, str, attr);
		}
	}
}

class LoginPanel extends JPanel {
	private JTextField login;
	private JPasswordField password;
	private JButton loginButton;

	public LoginPanel(ActionListener controller) {
		super(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		login = new JTextField(15);
		login.setDocument(new DigitsFieldDocument(9));
		password = new JPasswordField(15);
		loginButton = new JButton("OK");

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.insets = new Insets(0, 5, 20, 0);
		JLabel header = new JLabel("Вход");
		header.setFont(new Font(header.getFont().getName(), Font.PLAIN, 25));
		add(header, c);

		c.gridy++;
		c.gridwidth = 1;
		add(new JLabel("ID: "), c);
		c.gridx = 1;
		add(login, c);

		c.gridx = 0;
		c.gridy++;
		add(new JLabel("Пароль: "), c);
		c.gridx = 1;
		add(password, c);

		c.insets = new Insets(0, 0, 0, 0);
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 2;

		loginButton.setActionCommand(MainView.LOGIN_ACTION_COMMAND);
		loginButton.addActionListener(controller);
		add(loginButton, c);
	}

	public String getLogin() {
		return login.getText();
	}

	public String getPassword() {
		return new String(password.getPassword());
	}
}

class MainPanel extends JPanel {
	private MainModel model;
	private MouseListener controller;
	private JList<String> mainList;
	private DefaultListModel<String> mainListModel;
	private JPanel topPanel;

	public MainPanel(MainModel model, MouseListener controller) {
		super();
		this.model = model;
		this.controller = controller;
		add(new JLabel("Main panel"));
		mainListModel = new DefaultListModel<String>();
	}

	public void showMainPage(AuthData data) {
		this.removeAll();

		topPanel = new JPanel();
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
		
		JScrollPane listScroller = new JScrollPane(mainList);
		listScroller.setPreferredSize(new Dimension(380, 200));
		
		setPreferredSize(new Dimension(400, 300));
		add(listScroller);
	}
	
	public void setListData(List<String> data) {
		mainListModel.removeAllElements();
		for(String s : data) {
			mainListModel.addElement(s);
		}
	}
}

public class MainView extends JFrame implements Observer {
	public static final String LOGIN_ACTION_COMMAND = "login";
	private MainModel model;
	private MainController controller;
	private LoginPanel loginPanel;
	private MainPanel mainPanel;

	public MainView(MainModel m) {
		super("Анализ успеваемости");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		model = m;
		model.addObserver(this);

		// ArrayList<String> values = new ArrayList<>();
		// for(int i=0; i<5; i++)
		// values.add("Семестр " + (i+1));
		// Object[] objData = values.toArray();
		// String[] stringData = Arrays.copyOf(objData, objData.length,
		// String[].class);
		// mainList = new JList<>(stringData);
		// mainList.setName("first list");
		//
		// JScrollPane listScroller = new JScrollPane(mainList);
		// listScroller.setPreferredSize(new Dimension(380, 280));
		// JPanel panel = new JPanel();
		// panel.setPreferredSize(new Dimension(400,300));
		// panel.add(listScroller);
		//
		// this.add(panel);
		// this.pack();
		controller = new MainController(model, this);
		// mainList.addMouseListener(controller);
		//
		// //TODO delete this
		// this.setVisible(true);
	}

	private void placeToCenter() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height
				/ 2 - this.getSize().height / 2);
	}

	public String getLogin() {
		return loginPanel.getLogin();
	}

	public String getPassword() {
		return loginPanel.getPassword();
	}

	@Override
	public void update(Observable o, Object arg) {
		MainModel.MainEvent event = (MainModel.MainEvent) arg;
		System.out.println("update on view" + event.name());
		switch (event) {
		case AUTHORIZATION:
			System.out.println("show dialog");
			this.getContentPane().removeAll();
			loginPanel = new LoginPanel(controller);
			this.add(loginPanel);
			this.pack();
			this.placeToCenter();
			this.setVisible(true);
			break;
		case AUTHORIZATION_FAIL:
			JOptionPane.showMessageDialog(this,
					"Неверная комбинация логин/пароль", "Ошибка",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		case ROLES:
			AuthData data = model.getAuthData();
			this.setVisible(false);
			this.getContentPane().removeAll();
			mainPanel = new MainPanel(model, controller);
			mainPanel.showMainPage(data);
			this.add(mainPanel);
			this.pack();
			this.placeToCenter();
			this.setVisible(true);
			break;
		case SEMESTERS:
			this.setVisible(true);
			mainPanel.setListData(model.getSemesters());
			break;
		case SUBJECTS:
			mainPanel.setListData(model.getSubjects());
			break;
		case GROUPS:
			mainPanel.setListData(model.getGroups());
			break;
		case GROUP_SUBJECT_MARKS:
			//TODO draw table
			model.getSubjectMarks().print();
			break;
		}
	}

}
