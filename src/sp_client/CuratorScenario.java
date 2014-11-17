package sp_client;

import sp_client.MainModel.MainEvent;
import sp_entities.Semester;
import sp_entities.Semesters;
import sp_entities.XMLSerializer;
import sp_server.Server;

public class CuratorScenario extends UserScenario {
	public CuratorScenario(Server server, String idSession) {
		super(server, idSession);
		System.out.println("curator scenario constructor");
	}
	
	@Override
	protected void getSemesters() {
		String semStr = server.getCuratorSemesters(idSession);
		System.out.println(semStr);
		semesters = (Semesters) XMLSerializer.xmlToObject(semStr, Semesters.class);
		curEvent = MainEvent.SEMESTERS;
	}
	
	@Override
	protected void setSemester(Semester sem) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void setSubject(String subj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setGroup(String group) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setGroupMenu(int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setListIndex(int ind) {
	}

}
