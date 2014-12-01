package controller;

import java.awt.KeyEventDispatcher;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JList;

import model.MainModel;
import view.MainView;

public class MainController implements ActionListener, MouseListener, Observer, KeyEventDispatcher {
	private final MainModel model;
	private final MainView view;
	
	public MainController(MainModel model, MainView view) {
		this.model = model;
		this.view = view;
		model.addObserver(this);
	}
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getClickCount() != 1) return;
		Object source = e.getSource();
		
		if(source instanceof JList<?>) {
			JList<?> list = (JList<?>)e.getSource();
			int index = list.getSelectedIndex();
			String value = (String) list.getSelectedValue();
			model.setListChoice(index, value);
		}
	}
	
	@Override
	public void update(Observable o, Object obj) {}

	@Override
	public void actionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub
		String command = evt.getActionCommand();
		if(command.equals(MainView.LOGIN_ACTION_COMMAND)) {
			String loginStr = view.getLogin();
			String pass = view.getPassword();
			if(pass.length() == 0 || loginStr.length() == 0) 
				return;
			int id = Integer.parseInt(loginStr);
			model.login(id, pass);
		} else if(command.equals(MainView.BACK_BUTTON_COMMAND)) {
			model.goBack();
		} else if(command.matches("(" + MainView.HISTORY_BUTTON_COMMAND + ")[0-9]+")) {
			String strNumber = command.substring(
					MainView.HISTORY_BUTTON_COMMAND.length());
			int number = Integer.parseInt(strNumber);
			model.goBack(number);
		}
	}
	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		if (e.getID() != KeyEvent.KEY_PRESSED)
            return false;
		int code = e.getKeyCode();
		if(code == KeyEvent.VK_BACK_SPACE) {
			model.goBack();
		}
		return false;
	}
}
