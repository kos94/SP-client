package sp_client;
import java.util.List;
import java.util.Stack;

import sp_client.MainModel.MainEvent;
import sp_entities.*;
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
	protected IMarks marks;
	
	public UserScenario(Server server, String idSession) {
		this.server = server;
		this.idSession = idSession;
		history = new Stack<>();
		history.push(MainEvent.ROLES);
		getSemesters();
	}
	
	public abstract void setListIndex(int ind);
	protected abstract void getSemesters();
	
	public MainEvent getCurEvent() { return curEvent; }
	public void goBack(int historyPos) {
		System.out.println("stack before come back: ");
		for(int i=0; i<history.size(); i++) {
			System.out.println(history.get(i));
		}
		System.out.println("want go back to " + history.get(historyPos));
		
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
