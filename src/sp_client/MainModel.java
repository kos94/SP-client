package sp_client;

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
import sp_entities.UserStatus;
import sp_entities.XMLSerializer;
import sp_server.Server;
import sp_server.ServerService;

public class MainModel extends Observable {
	private Server server;
	
	private Semester curSemester;
	private String curSubject;
	private String curGroup;
	private int curStage;
	private UserStatus curRole;
	
	private String loginMessage;
	private Semesters semesters;
	private List<String> subjects;
	private List<String> groups;
	private GroupSubjectMarks groupSubjMarks;
	private GroupStageMarks groupStageMarks;
	private StudentSemMarks studentMarks;
	
	public enum MainEvent {
		AUTHORIZATION,
		SEMESTERS,
		SUBJECTS, 
		GROUPS, 
		GROUP_MENU,
		GROUP_SUBJECT_MARKS,
		GROUP_STAGE_MARKS,
		STUDENT_SEM_MARKS;
	}
	
	public MainModel() throws Exception {
		ServerService service = new ServerService();
		server = service.getServerPort();
	}
	
	public void tempStart() {
		setChanged();
		this.notifyObservers(MainEvent.AUTHORIZATION);
//		String data = server.login(1, "aaa");
//		System.out.println(data);
//		AuthData authData = (AuthData)XMLSerializer.xmlToObject(data, AuthData.class);
//		String idSession = authData.getIdSession();
//		
//		String sem = server.getTeacherSemesters(idSession);
//		semesters = (Semesters)
//				XMLSerializer.xmlToObject(sem, Semesters.class);

	}
	
	public void login(int id, String password) {
		String s = server.login(id, password);
		System.out.println(s);
	}
	
	public void badLogin(String message) {
		loginMessage = message;
	}
	
	public Semesters getSemesters() { return semesters; }
}
