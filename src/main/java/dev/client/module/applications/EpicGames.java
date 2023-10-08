package dev.client.module.applications;

import java.io.File;

public class EpicGames {
    private final File epic;
    public EpicGames(){
        epic = new File(System.getenv("LOCALAPPDATA") + "/EpicGamesLauncher/Saved/Config/Windows/GameUserSettings.ini");
    }

    public File getEpic() {
        return epic;
    }
}
