package me.wappen.playerprofiles;

import com.destroystokyo.paper.profile.PlayerProfile;
import de.boiis.plugincore.BoiiPlugin;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerProfiles extends BoiiPlugin<Config> {
    private final Map<UUID, PlayerProfile> altProfiles = new HashMap<>(); // Map real uuid to alt profile
    private final Map<UUID, PlayerProfile> origProfiles = new HashMap<>(); // Map alt uuid to real profile

    private static PlayerProfiles instance;

    public static PlayerProfiles getInstance() {
        return instance;
    }

    public static Config config() {
        return instance.getBoiiConfig();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        instance = this;

        initBoiiConfig(new Config());

        registerCommand(new PlayerProfilesCommand(this));
        registerCommand(new ChangeProfileCommand(this));

        registerListener(new ChangeProfileListener(this));
    }

    @Override
    public void onDisable() {

    }

    public void createAltProfile(Player player, String profileName) {
        PlayerProfile origProfile = origProfiles.get(player.getUniqueId());

        if (origProfile == null) {
            origProfile = player.getPlayerProfile();
        }

        UUID altUUID = profileUUID(origProfile.getId(), profileName);
        String altName = config().nameFormat
                .replace("%name%", origProfile.getName())
                .replace("%profile%", profileName);

        PlayerProfile altProfile = getServer().createProfileExact(altUUID, altName);

        if (config().keepSkin) {
            altProfile.setProperties(origProfile.getProperties());
        }

        altProfiles.put(origProfile.getId(), altProfile);
        origProfiles.put(altProfile.getId(), origProfile);
    }

    public void restoreProfile(Player player) {
        PlayerProfile origProfile = origProfiles.get(player.getUniqueId());

        if (origProfile != null) {
            PlayerProfile old = altProfiles.remove(origProfile.getId());
            origProfiles.remove(old.getId());
        }
    }

    public PlayerProfile getAltProfile(UUID uuid) {
        return altProfiles.get(uuid);
    }

    private UUID profileUUID(UUID origUUID, String profileName) {
        String str = "WappenProfile:" + profileName + "@" + origUUID.toString();
        return UUID.nameUUIDFromBytes(str.getBytes());
    }
}
