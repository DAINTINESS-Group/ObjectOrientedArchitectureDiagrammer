package model.strategies;

import java.util.ArrayList;
import java.util.List;

import model.Document;

public class VolatileVersionsStrategy implements VersionsStrategy {
	private ArrayList<Document> history;
	
	public VolatileVersionsStrategy() {
		super();
		history = new ArrayList<Document>();
	}

	@Override
	public void putVersion(Document document) {
		// TODO Auto-generated method stub
		Document doc = document.clone();
		history.add(doc);
	}

	@Override
	public Document getVersion() {
		// TODO Auto-generated method stub
		if(history.size() == 0)
			return null;
		return history.get(history.size() - 1);
	}

	@Override
	public void setEntireHistory(List<Document> documents) {
		// TODO Auto-generated method stub
		history.clear();
		history.addAll(documents);
	}

	@Override
	public List<Document> getEntireHistory() {
		// TODO Auto-generated method stub
		return history;
	}

	@Override
	public void removeVersion() {
		// TODO Auto-generated method stub
		history.remove(history.size() - 1);
	}

}
