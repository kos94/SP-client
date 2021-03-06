package view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import model.MainModel;
import sp_entities.AuthData;
import controller.MainController;

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
		JLabel header = new JLabel("����");
		header.setFont(new Font(header.getFont().getName(), Font.PLAIN, 25));
		add(header, c);

		c.gridy++;
		c.gridwidth = 1;
		add(new JLabel("ID: "), c);
		c.gridx = 1;
		add(login, c);

		c.gridx = 0;
		c.gridy++;
		add(new JLabel("������: "), c);
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



public class MainView extends JFrame implements Observer {
	//TODO maybe delete (replace with switch (source)
	public static final String LOGIN_ACTION_COMMAND = "login";
	public static final String HISTORY_BUTTON_COMMAND = "historybutton";
	public static final String BACK_BUTTON_COMMAND = "back";
	private MainModel model;
	private MainController controller;
	private LoginPanel loginPanel;
	private MainPanel mainPanel;

	public MainView(MainModel m) {
		super("������ ������������");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		model = m;
		model.addObserver(this);
		controller = new MainController(model, this);

		KeyboardFocusManager.getCurrentKeyboardFocusManager()
				.addKeyEventDispatcher(controller);	
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
		boolean newEvent = model.isNewEvent();
		
		switch (event) {
		case AUTHORIZATION:
			this.getContentPane().removeAll();
			loginPanel = new LoginPanel(controller);
			this.add(loginPanel);
			this.pack();
			this.setLocationRelativeTo(null);
			this.setVisible(true);
			break;
		case AUTHORIZATION_FAIL:
			JOptionPane.showMessageDialog(this,
					"�������� ���������� �����/������", "������",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		case ROLES:
			AuthData data = model.getAuthData();
			if(newEvent && mainPanel == null) {
				this.getContentPane().removeAll();
				mainPanel = new MainPanel(model, controller);
				this.add(mainPanel);
				this.pack();
				this.setLocationRelativeTo(null);
			}
			mainPanel.showMainPage(data);
			break;
		case SEMESTERS:
			if(newEvent) {
				mainPanel.showHistoryPanel();
				mainPanel.addEventToHistory("������� ����");
			}
			mainPanel.setListData(model.getSemesters());
			break;
		case FLOW_OR_GROUP_MENU:
			mainPanel.setListData(model.getFlowOrGroupMenu());
			break;
		case GROUP_SUBJECTS:
			mainPanel.setListData(model.getSubjects());
			break;
		case GROUPS:
			mainPanel.setListData(model.getGroups());
			break;
		case GROUP_MENU:
			mainPanel.setListData(model.getGroupMenu());
			break;
		case MARKS:
			mainPanel.showMarks(model.getMarks());
			break;
		}
		
		if(mainPanel != null && mainPanel.isHistoryShowing()) {
			if(newEvent) {
				mainPanel.addEventToHistory(model.getUserChoice());
			} else {
				mainPanel.setHistoryPosition(model.getHistoryPosition());
			}
		}
	}

}
