package sp_client;
import java.util.List;

import sp_server.ServerService;
import sp_server.Server;

public class ClientMain {
	public static void main(String[] args) {
		ServerService service= new ServerService();
		Server server = service.getServerPort();
		System.out.println("!Login: " + server.login("1231231231211"));
		System.out.println("!Semesters: " + server.getSemesters());
		System.out.println("!Subjects: " + server.getSubjects());
		System.out.println("!Groups: " + server.getGroups());
		System.out.println("!Subject marks:");
		System.out.println(server.getSubjectMarks());
		System.out.println("!Stage marks:");
		System.out.println(server.getStageMarks());
		System.out.println("!Student marks:");
		System.out.println(server.getStudentMarks());
		System.out.print("!setGroup: " + server.setGroup(1) +", ");
		System.out.print("!setStage: " + server.setStage(0) +", ");
		System.out.print("!setSemester: " + server.setSemester(0));
	}
}
