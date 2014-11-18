package sp_client;

import java.util.ArrayList;

import sp_client.MainModel.MainEvent;
import sp_entities.GroupStageMarks;
import sp_entities.GroupSubjectMarks;
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
		goToEvent(MainEvent.SEMESTERS);
	}
	@Override
	public void setListIndex(int ind) {
		assert(ind >= 0); //TODO delete or not delete
		switch(curEvent) {
		case SEMESTERS:
			assert(ind < semesters.getSemesters().size());
			Semester sem = semesters.getSemesters().get(ind);
			curSemester = XMLSerializer.objectToXML(sem);
			String group = server.getCuratorGroup(idSession, curSemester);
			groups = new ArrayList<>();
			groups.add(group);
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
				groupStageMarks = (GroupStageMarks) XMLSerializer
						.xmlToObject(xmlMarks, GroupStageMarks.class);
				goToEvent(MainEvent.GROUP_STAGE_MARKS);
			} else {
				subjects = server.getGroupSubjects(idSession, curSemester, curGroup);
				goToEvent(MainEvent.SUBJECTS);
			}
			break;
		case SUBJECTS:
			assert(ind < subjects.size());
			curSubject = subjects.get(ind);
			String xmlMarks = server.getSubjectMarks(idSession, curGroup, curSubject);
			groupSubjMarks = (GroupSubjectMarks) XMLSerializer
					.xmlToObject(xmlMarks, GroupSubjectMarks.class);
			goToEvent(MainEvent.GROUP_SUBJECT_MARKS);
			break;
		}
	}
}
