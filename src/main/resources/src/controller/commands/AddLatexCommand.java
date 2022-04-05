package controller.commands;

import model.VersionsManager;

public class AddLatexCommand implements Command  {
	private VersionsManager versionsManager;
	
	
	public AddLatexCommand(VersionsManager versionsManager) {
		super();
		this.versionsManager = versionsManager;
	}


	@Override
	public void execute() {
		// TODO Auto-generated method stub
		versionsManager.saveContents();
	}

}
