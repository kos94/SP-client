package sp_client;

import sp_client.MainModel.MainEvent;
import sp_entities.*;
import sp_server.Server;

public class TeacherScenario extends UserScenario {

	public TeacherScenario(Server server, String idSession) {
		super(server, idSession);
		System.out.println("teacher scenario constructor");
	}

	@Override
	protected void getSemesters() {
		String semStr = server.getTeacherSemesters(idSession);
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
		switch(curEvent) {
		case SEMESTERS:
			if(ind >= semesters.getSemesters().size()) return;
			Semester sem = semesters.getSemesters().get(ind);
			curSemester = XMLSerializer.objectToXML(sem);
			subjects = server.getTeacherSubjects(idSession, curSemester);
			curEvent = MainEvent.SUBJECTS;
			break;
		case SUBJECTS:
			if(ind >= subjects.size()) return;
			curSubject = subjects.get(ind);
			groups = server.getTeacherGroups(idSession, curSemester, curSubject);
			curEvent = MainEvent.GROUPS;
			break;
		case GROUPS:
			if(ind >= groups.size()) return;
			curGroup = groups.get(ind);
			String xmlMarks = server.getSubjectMarks(idSession, curGroup, curSubject);
			groupSubjMarks = (GroupSubjectMarks) XMLSerializer
					.xmlToObject(xmlMarks, GroupSubjectMarks.class);
			curEvent = MainEvent.GROUP_SUBJECT_MARKS;
		}
	}

	

}
