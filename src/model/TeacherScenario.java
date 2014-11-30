package model;

import model.MainModel.MainEvent;
import sp_entities.*;
import sp_server.Server;

public class TeacherScenario extends UserScenario {

	public TeacherScenario(Server server, String idSession) {
		super(server, idSession);
		System.out.println("teacher scenario constructor");
	}

	protected void getSemesters() {
		String semStr = server.getTeacherSemesters(idSession);
		System.out.println(semStr);
		semesters = (Semesters) XMLSerializer.xmlToObject(semStr, Semesters.class);
		goToEvent(MainEvent.SEMESTERS);
	}

	@Override
	public void setListIndex(int ind) {
		assert(ind >= 0);
		switch(curEvent) {
		case SEMESTERS:
			assert(ind < semesters.getSemesters().size());
			Semester sem = semesters.getSemesters().get(ind);
			curSemester = XMLSerializer.objectToXML(sem);
			subjects = server.getTeacherSubjects(idSession, curSemester);
			goToEvent(MainEvent.GROUP_SUBJECTS);
			break;
		case GROUP_SUBJECTS:
			assert(ind < subjects.size());
			curSubject = subjects.get(ind);
			goToEvent(MainEvent.FLOW_OR_GROUP_MENU);
			break;
		case FLOW_OR_GROUP_MENU:
			assert(ind < 2);
			isFlow = (ind == 1);
			if(isFlow) {
				groups = server.getTeacherFlows(idSession, curSemester, curSubject);
			} else {
				groups = server.getTeacherGroups(idSession, curSemester, curSubject);
			}
			goToEvent(MainEvent.GROUPS);
			break;
		case GROUPS:
			assert(ind < groups.size());
			String xmlMarks;
			curGroup = groups.get(ind);
			if(isFlow) {
				xmlMarks = server.getFlowSubjectMarks(idSession, curGroup, curSubject);
			} else {
				xmlMarks = server.getSubjectMarks(idSession, curGroup, curSubject);
			}
			marks = (GroupSubjectMarks) XMLSerializer
					.xmlToObject(xmlMarks, GroupSubjectMarks.class);
			goToEvent(MainEvent.MARKS);
			break;
		}
	}

}
