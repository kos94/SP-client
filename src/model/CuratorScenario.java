package model;

import java.util.ArrayList;

import model.MainModel.MainEvent;
import sp_entities.GroupStageMarks;
import sp_entities.GroupSubjectMarks;
import sp_entities.Semester;
import sp_entities.Semesters;
import sp_entities.XMLSerializer;
import sp_server.Server;

public class CuratorScenario extends UserScenario {
	public CuratorScenario(Server server, String idSession) {
		super(server, idSession);
	}
	
	@Override
	protected void getSemesters() {
		String semStr = server.getCuratorSemesters(idSession);
		semesters = (Semesters) XMLSerializer.xmlToObject(semStr, Semesters.class);
		goToEvent(MainEvent.SEMESTERS);
	}
	@Override
	public void setListIndex(int ind) {
		switch(curEvent) {
		case SEMESTERS:
			Semester sem = semesters.getSemesters().get(ind);
			curSemester = XMLSerializer.objectToXML(sem);
			String group = server.getCuratorGroup(idSession, curSemester);
			groups = new ArrayList<>();
			groups.add(group);
			goToEvent(MainEvent.GROUPS);
			break;
		case GROUPS:
			curGroup = groups.get(ind);
			goToEvent(MainEvent.GROUP_MENU);
			break;
		case GROUP_MENU:
			if(ind < 3) {
				curStage = ind + 1;
				String xmlMarks = server.getStageMarks(idSession, curGroup, curSemester, curStage);
				marks = (GroupStageMarks) XMLSerializer
						.xmlToObject(xmlMarks, GroupStageMarks.class);
				goToEvent(MainEvent.MARKS);
			} else {
				subjects = server.getGroupSubjects(idSession, curSemester, curGroup);
				goToEvent(MainEvent.GROUP_SUBJECTS);
			}
			break;
		case GROUP_SUBJECTS:
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
