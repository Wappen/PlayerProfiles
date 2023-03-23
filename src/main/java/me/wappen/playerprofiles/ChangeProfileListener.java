package me.wappen.playerprofiles;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class ChangeProfileListener implements Listener {
    PlayerProfiles plugin;

    public ChangeProfileListener(PlayerProfiles plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLogin(AsyncPlayerPreLoginEvent event) {
        PlayerProfile altProfile = plugin.getAltProfile(event.getUniqueId());
        if (altProfile != null) {
            event.setPlayerProfile(altProfile);
        }
    }
}
