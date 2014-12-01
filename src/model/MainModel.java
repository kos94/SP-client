package model;

import java.util.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import sp_entities.*;
import sp_server.Server;
import sp_server.ServerService;
import view.MainView;

public class MainModel extends Observable {
	public enum MainEvent {
		AUTHORIZATION("Авторизация"),
		AUTHORIZATION_FAIL("Ошибка авторизации"),
		ROLES("Роли"),
		SEMESTERS("Семестры"),
		FLOW_OR_GROUP_MENU("Группы/потоки"),
		GROUP_SUBJECTS("Предметы"), 
		GROUPS("Группы"), 
		GROUP_MENU("Меню группы"),
		MARKS("Оценки");
		
		private String displayedName;
		private MainEvent(String n) {
			displayedName = n;
		}
		public String toString() {
			return displayedName;
		}
	}
	
	private Server server;
	private AuthData authData;
	
	private String userChoice;
	private MainEvent curEvent;
	private boolean newEvent;
	private UserRole[] roles;
	private UserScenario scenario;
	
	public MainModel() throws Exception {
		ServerService service = new ServerService();
		server = service.getServerPort();
		newEvent = false;
	}
	
	public void start() {
		curEvent = MainEvent.AUTHORIZATION;
		setChanged();
		notifyObservers(curEvent);
	}
	
	public void login(int id, String password) {
		String authString = server.login(id, password);
		if(authString == null) {
			authData = null;
			curEvent = MainEvent.AUTHORIZATION_FAIL;
		} else {
			authData = (AuthData) XMLSerializer
					.xmlToObject(authString, AuthData.class);
			//save roles list
			UserRole role = authData.getStatus();
			if(role == UserRole.CURATOR) {
				roles = new UserRole[]{UserRole.TEACHER, role};
			} else {
				roles = new UserRole[]{role};
			}
			curEvent = MainEvent.ROLES;
		}
		newEvent = true;
		userChoice = "Главное меню";
		setChanged();
		notifyObservers(curEvent);
	}
	
	public void setListChoice(int index, String value) {
		userChoice = value;
		if(index < 0) return;
		if(curEvent == MainEvent.ROLES) {
			if(index >= roles.length) return; 
			String idS = authData.getIdSession();
			switch(roles[index]) {
			case TEACHER:
				scenario = new TeacherScenario(server, idS);
				break;
			case CURATOR:
				scenario = new CuratorScenario(server, idS);
				break;
			case DEPWORKER:
				scenario = new DepWorkerScenario(server, idS);
				break;
			case STUDENT:
				scenario = new StudentScenario(server, idS);
				break;
			}
		} else {
			scenario.setListIndex(index);
		}
		curEvent = scenario.getCurEvent();
		newEvent = true;
		setChanged();
		notifyObservers(curEvent);
	}

	public void goBack(int historyPos) {
		newEvent = false;
		scenario.goBack(historyPos);
		curEvent = scenario.getCurEvent();
		setChanged();
		notifyObservers(curEvent);
	}
	
	public void goBack() {
		if(scenario == null) return;
		newEvent = false;
		scenario.goBack();
		curEvent = scenario.getCurEvent();
		setChanged();
		notifyObservers(curEvent);
	}
	
	public boolean isNewEvent() { return newEvent; }
	public String getUserChoice() { return userChoice; }
	public int getHistoryPosition() { return scenario.getHistoryPosition(); }
	public AuthData getAuthData() { return authData; }
	public List<String> getRoles() {
		List<String> list = new ArrayList<>();
		for(int i=0; i<roles.length; i++)
			list.add( roles[i].getRusName() );
		return list;
	}
	public List<String> getSemesters() { 
		List<Semester> list = scenario.semesters.getSemesters(); 
		List<String> strings = new ArrayList<>();
		for(Semester s : list) {
			strings.add(s.toString());
		}
		return strings;
	}
	public List<String> getSubjects() { return scenario.subjects; }
	public List<String> getGroups() { return scenario.groups; }
	public List<String> getGroupMenu() {
		List<String> menu = new ArrayList<>();
		menu.add("Оценки за 1-й модуль");
		menu.add("Оценки за 2-й модуль");
		menu.add("Итоговые оценки");
		menu.add("Оценки по дисциплинам");
		return menu;
	}
	public IMarks getMarks() { return scenario.marks; }

	public List<String> getFlowOrGroupMenu() {
		List<String> menu = new ArrayList<>();
		menu.add("Группы");
		menu.add("Потоки");
		return menu;
	}
	
	public static void main(String[] args) {
		try {
			MainModel model = new MainModel();
			MainView view = new MainView(model);
			model.start();
		} catch (Exception e) {
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			JOptionPane.showMessageDialog(frame, "Не удалось соединиться с сервером", "Ошибка",
				        JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
}
