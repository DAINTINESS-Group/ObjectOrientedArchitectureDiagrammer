package controller.commands;

import model.VersionsManager;

public class ChangeVersionsStrategyCommand implements Command {
	private VersionsManager versionsManager;
	
	public ChangeVersionsStrategyCommand(VersionsManager versionsManager) {
		super();
		this.versionsManager = versionsManager;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		versionsManager.changeStrategy();
	}

}
