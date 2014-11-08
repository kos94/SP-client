package sp_client;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import sp_entities.GroupStageMarks;
import sp_entities.GroupSubjectMarks;
import sp_entities.Semester;
import sp_entities.Semesters;
import sp_entities.StudentSemMarks;
import sp_entities.XMLSerializer;
import sp_server.GetSemesters;
import sp_server.Server;
import sp_server.ServerService;

public class MainModel extends Observable {
	private Server server;
	
	private Semesters semesters;
	private List<String> subjects;
	private List<String> groups;
	private GroupSubjectMarks groupSubjMarks;
	private GroupStageMarks groupStageMarks;
	private StudentSemMarks studentMarks;
	
	public enum MainEvent {
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
		server.login(1, "aaa");
		String sem = server.getSemesters(1);
		semesters = (Semesters)
				XMLSerializer.xmlToObject(sem, Semesters.class);
		setChanged();
		this.notifyObservers(MainEvent.SEMESTERS);
	}
	
	public Semesters getSemesters() { return semesters; }
}
