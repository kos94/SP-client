package sp_client;

import sp_client.MainModel.MainEvent;
import sp_entities.Semester;
import sp_entities.Semesters;
import sp_entities.XMLSerializer;
import sp_server.Server;

public class DepWorkerScenario extends UserScenario {

	public DepWorkerScenario(Server server, String idSession) {
		super(server, idSession);
		System.out.println("depworker scenario constructor");
	}
	
	@Override
	protected void getSemesters() {
		String semStr = server.getDepSemesters(idSession);
		System.out.println(semStr);
		semesters = (Semesters) XMLSerializer.xmlToObject(semStr, Semesters.class);
		curEvent = MainEvent.SEMESTERS;
	}

	@Override
	public void setListIndex(int ind) {
		// TODO Auto-generated method stub
		
	}
}
