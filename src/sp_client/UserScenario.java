package sp_client;
import java.util.List;
import java.util.Stack;

import sp_client.MainModel.MainEvent;
import sp_entities.GroupStageMarks;
import sp_entities.GroupSubjectMarks;
import sp_entities.Semester;
import sp_entities.Semesters;
import sp_entities.StudentSemMarks;
import sp_server.Server;

public abstract class UserScenario {
	protected Server server;
	protected String idSession;
	
	protected Stack<MainEvent> history;
	protected MainEvent curEvent;
	protected String curSemester;
	protected String curSubject;
	protected String curGroup;
	protected int curStage;
	
	protected Semesters semesters;
	protected List<String> subjects;
	protected List<String> groups;
	protected GroupSubjectMarks groupSubjMarks;
	protected GroupStageMarks groupStageMarks;
	protected StudentSemMarks studentMarks;	
	
	public UserScenario(Server server, String idSession) {
		this.server = server;
		this.idSession = idSession;
		history = new Stack<>();
		getSemesters();
	}
	
	public abstract void setListIndex(int ind);
	
	protected abstract void getSemesters();
	protected abstract void setSemester(Semester sem);
	protected abstract void setSubject(String subj);
	protected abstract void setGroup(String group);
	protected abstract void setGroupMenu(int index);
	
	public MainEvent getCurEvent() { return curEvent; }
	public void goBack(int historyPos) {
		int diff = history.size() - historyPos - 1;
		if(diff <= 0) return;
		while(--diff >= 0) {
			history.pop();
		}
		curEvent = history.peek();
		//TODO smth about choosing role
	}
	public int getHistoryPosition() { return history.size()-1; }
	protected void goToEvent(MainEvent e) { 
		curEvent = e;
		history.push(curEvent);
	}
}
