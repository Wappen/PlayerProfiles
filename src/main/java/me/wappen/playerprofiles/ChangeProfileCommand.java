package me.wappen.playerprofiles;

import de.boiis.plugincore.BoiiCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChangeProfileCommand extends BoiiCommand {
    private final PlayerProfiles plugin;

    public ChangeProfileCommand(PlayerProfiles plugin) {
        super("profile");
        this.plugin = plugin;
    }

    @Override
    public boolean onPlayerExec(@NotNull Player player, @NotNull String[] args) {
        if (args.length > 1)
            return false;

        switch (args.length) {
            case 0:
                plugin.restoreProfile(player);
                break;
            case 1:
                plugin.createAltProfile(player, args[0]);
                break;
            default:
                return false;
        }

        Config cfg = PlayerProfiles.config();
        String msg = args.length == 0 ? cfg.rejoinMessageRestore : cfg.rejoinMessage.replace("%profile%", args[0]);
        player.kick(Component.text(msg));
        return true;
    }
}
