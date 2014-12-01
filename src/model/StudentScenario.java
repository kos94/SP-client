package model;

import model.MainModel.MainEvent;
import sp_entities.Semester;
import sp_entities.Semesters;
import sp_entities.StudentSemMarks;
import sp_entities.XMLSerializer;
import sp_server.Server;

public class StudentScenario extends UserScenario {

	public StudentScenario(Server server, String idSession) {
		super(server, idSession);
	}
	
	@Override
	protected void getSemesters() {
		String semStr = server.getStudentSemesters(idSession);
		semesters = (Semesters) XMLSerializer.xmlToObject(semStr, Semesters.class);
		goToEvent(MainEvent.SEMESTERS);
	}

	@Override
	public void setListIndex(int ind) {
		if(curEvent == MainEvent.SEMESTERS) {
			assert(ind < semesters.getSemesters().size());
			Semester sem = semesters.getSemesters().get(ind);
			curSemester = XMLSerializer.objectToXML(sem);
			String xmlMarks = server.getStudentMarks(idSession, curSemester);
			marks = (StudentSemMarks) XMLSerializer
					.xmlToObject(xmlMarks, StudentSemMarks.class);
			goToEvent(MainEvent.MARKS);
		}
	}
}
