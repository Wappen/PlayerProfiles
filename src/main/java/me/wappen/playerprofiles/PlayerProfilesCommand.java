package me.wappen.playerprofiles;

import de.boiis.plugincore.BoiiCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class PlayerProfilesCommand extends BoiiCommand {
    private final PlayerProfiles plugin;

    public PlayerProfilesCommand(PlayerProfiles plugin) {
        super("playerprofiles", Collections.singletonList(new ReloadCommand(plugin)));
        this.plugin = plugin;
    }

    private static class ReloadCommand extends BoiiCommand {
        private final PlayerProfiles plugin;

        public ReloadCommand(PlayerProfiles plugin) {
            super("reload");
            this.plugin = plugin;
        }

        @Override
        public boolean onExec(@NotNull CommandSender sender, @NotNull String[] args) {
            plugin.reloadBoiiConfig();
            sender.sendMessage("§aPlayerProfiles reload complete.");
            return true;
        }
    }

    @Override
    public boolean onExec(@NotNull CommandSender sender, @NotNull String[] args) {
        sender.sendMessage("§e" + plugin.getHeader());
        return true;
    }
}
