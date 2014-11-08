package sp_client;

import java.util.Observer;

public abstract class MainObserver implements Observer{
	protected MainModel model;
	public MainObserver(MainModel model) {
		this.model = model;
	}
}
