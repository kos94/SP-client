package sp_client;

import java.util.ArrayList;

import sp_client.MainModel.MainEvent;
import sp_entities.GroupStageMarks;
import sp_entities.GroupSubjectMarks;
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
		switch(curEvent) {
		case SEMESTERS:
			assert(ind < semesters.getSemesters().size());
			Semester sem = semesters.getSemesters().get(ind);
			curSemester = XMLSerializer.objectToXML(sem);
			groups = server.getDepGroups(idSession, curSemester);
			goToEvent(MainEvent.GROUPS);
			break;
		case GROUPS:
			assert(ind < groups.size());
			curGroup = groups.get(ind);
			goToEvent(MainEvent.GROUP_MENU);
			break;
		case GROUP_MENU:
			assert(ind < 4);
			if(ind < 3) {
				curStage = ind + 1;
				String xmlMarks = server.getStageMarks(idSession, curGroup, curSemester, curStage);
				System.out.println(idSession + " " + curGroup + " " + curSemester + " " + curStage);
				System.out.println("STAGE MARKS: " + xmlMarks);
				marks = (GroupStageMarks) XMLSerializer
						.xmlToObject(xmlMarks, GroupStageMarks.class);
				goToEvent(MainEvent.MARKS);
			} else {
				subjects = server.getGroupSubjects(idSession, curSemester, curGroup);
				goToEvent(MainEvent.SUBJECTS);
			}
			break;
		case SUBJECTS:
			assert(ind < subjects.size());
			curSubject = subjects.get(ind);
			String xmlMarks = server.getSubjectMarks(idSession, curGroup, curSubject);
			marks = (GroupSubjectMarks) XMLSerializer
					.xmlToObject(xmlMarks, GroupSubjectMarks.class);
			goToEvent(MainEvent.MARKS);
			break;
		}
	}
}
