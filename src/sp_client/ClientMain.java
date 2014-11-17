package sp_client;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import sp_entities.UserStatus;

public class ClientMain {
	public static void main(String[] args) {
		try {
			MainModel model = new MainModel();
			MainView view = new MainView(model);
			model.tempStart();
		} catch (Exception e) {
			//TODO show window with error
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			JOptionPane.showMessageDialog(frame, "Не удалось соединиться с сервером", "Ошибка",
				        JOptionPane.ERROR_MESSAGE);
			System.out.println("Problem with model loading");
			e.printStackTrace();
		}
	}
}
