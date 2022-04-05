package model.strategies;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.Document;

public class StableVersionsStrategy implements VersionsStrategy{
	private String versionID = "";
	@Override
	public void putVersion(Document document) {
		// TODO Auto-generated method stub
		String filename = document.getVersionID() + ".tex";
		document.save(filename);
		versionID = document.getVersionID();
		
	}

	@Override
	public Document getVersion() {
		// TODO Auto-generated method stub
		if(versionID.equals(""))
			return null;
		
		String fileContents = "";
		try {
			Scanner scanner = new Scanner(new FileInputStream(versionID + ".tex"));
			while(scanner.hasNextLine()) {
				fileContents = fileContents + scanner.nextLine() + "\n";
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Document document = new Document();
		document.setContents(fileContents);
		return document;
	}

	@Override
	public void setEntireHistory(List<Document> documents) {
		// TODO Auto-generated method stub
		for(int i = 0; i < documents.size(); i++) {
			Document doc = documents.get(i);
			doc.save(doc.getVersionID() +".tex");
		}
		if(documents.size() > 0)
			versionID = documents.get(documents.size()-1).getVersionID();
		else
			versionID = "";
	}

	@Override
	public List<Document> getEntireHistory() {
		// TODO Auto-generated method stub
		List<Document> documents = new ArrayList<Document>();
		if(versionID.equals(""))
			return documents;
		int n = Integer.parseInt(versionID);
		for(int i = 0; i <= n; i++) {
			String fileContents = "";
			try {
				Scanner scanner = new Scanner(new FileInputStream(i + ".tex"));
				while(scanner.hasNextLine()) {
					fileContents = fileContents + scanner.nextLine() + "\n";
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Document document = new Document();
			document.setContents(fileContents);
			documents.add(document);
		}
		return documents;
	}

	@Override
	public void removeVersion() {
		// TODO Auto-generated method stub
		int n = Integer.parseInt(versionID);
		if(n == 0)
			versionID = "";
		else
			versionID = (n-1) + "";
		
	}
}
