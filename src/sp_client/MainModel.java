package sp_client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import sp_entities.AuthData;
import sp_entities.GroupStageMarks;
import sp_entities.GroupSubjectMarks;
import sp_entities.Semester;
import sp_entities.Semesters;
import sp_entities.StudentSemMarks;
import sp_entities.UserRole;
import sp_entities.XMLSerializer;
import sp_server.Server;
import sp_server.ServerService;

public class MainModel extends Observable {
	public enum MainEvent {
		AUTHORIZATION("�����������"),
		AUTHORIZATION_FAIL("������ �����������"),
		ROLES("����"),
		SEMESTERS("��������"),
		SUBJECTS("��������"), 
		GROUPS("������"), 
		GROUP_MENU("���� ������"),
		GROUP_SUBJECT_MARKS("������"),
		GROUP_STAGE_MARKS("������"),
		STUDENT_SEM_MARKS("������");
		
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
	
	public void tempStart() {
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
		System.out.println("model go back");
		newEvent = false;
		scenario.goBack(historyPos);
		curEvent = scenario.getCurEvent();
		setChanged();
		System.out.println("curEvent: " + scenario.getCurEvent());
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
	public GroupSubjectMarks getSubjectMarks() { return scenario.groupSubjMarks; }
}
