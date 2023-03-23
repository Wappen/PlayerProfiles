package me.wappen.playerprofiles;

import de.boiis.plugincore.BoiiConfig;

public class Config implements BoiiConfig {
    public String rejoinMessage = "§ePlease rejoin the server; your profile §6'%profile%'§e has been applied.";
    public String rejoinMessageRestore = "§ePlease rejoin the server; your original profile has been restored.";
    public String nameFormat = "%name%-%profile%";

    public boolean keepSkin = true;
}
