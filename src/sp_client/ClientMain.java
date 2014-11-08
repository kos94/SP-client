package sp_client;
import java.util.List;

import sp_entities.UserStatus;

public class ClientMain {
	public static void main(String[] args) {
		try {
			MainModel model = new MainModel();
			MainView view = new MainView(model, UserStatus.TEACHER);
			model.tempStart();
		} catch (Exception e) {
			System.out.println("Problem with model loading");
			e.printStackTrace();
		}
	}
}
