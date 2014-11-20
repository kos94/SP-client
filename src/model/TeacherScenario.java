package model;

import java.util.Collections;

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
		switch(curEvent) {
		case SEMESTERS:
			assert(ind < semesters.getSemesters().size());
			Semester sem = semesters.getSemesters().get(ind);
			curSemester = XMLSerializer.objectToXML(sem);
			subjects = server.getTeacherSubjects(idSession, curSemester);
			goToEvent(MainEvent.SUBJECTS);
			break;
		case SUBJECTS:
			assert(ind < subjects.size());
			curSubject = subjects.get(ind);
			groups = server.getTeacherGroups(idSession, curSemester, curSubject);
			goToEvent(MainEvent.GROUPS);
			break;
		case GROUPS:
			assert(ind < groups.size());
			curGroup = groups.get(ind);
			String xmlMarks = server.getSubjectMarks(idSession, curGroup, curSubject);
			marks = (GroupSubjectMarks) XMLSerializer
					.xmlToObject(xmlMarks, GroupSubjectMarks.class);
			goToEvent(MainEvent.MARKS);
		}
	}

}
