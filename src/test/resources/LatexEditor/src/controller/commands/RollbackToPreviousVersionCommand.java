package controller.commands;

import model.VersionsManager;

public class RollbackToPreviousVersionCommand implements Command {
	private VersionsManager versionsManager;
	
	
	public RollbackToPreviousVersionCommand(VersionsManager versionsManager) {
		this.versionsManager = versionsManager;
	}


	@Override
	public void execute() {
		// TODO Auto-generated method stub
		versionsManager.rollback();
	}

}
