package me.wappen.playerprofiles;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.destroystokyo.paper.profile.PlayerProfile;
import de.boiis.plugincore.BoiiPlugin;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerProfiles extends BoiiPlugin<Config> {

    private ProtocolManager protocolManager;
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
    public void onLoad() {
        super.onLoad();

        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        instance = this;

        protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.LOW, PacketType.Login.Server.SUCCESS) {
            @Override
            public void onPacketSending(PacketEvent event) {
                logger.info("Login Packet");

                PacketContainer packet = event.getPacket();

                if (packet.getGameProfiles().size() > 0) {
                    StructureModifier<WrappedGameProfile> gameProfiles = packet.getGameProfiles();
                    WrappedGameProfile profile = gameProfiles.read(0);
                    UUID originalUUID = profile.getUUID();

                    if (altProfiles.containsKey(originalUUID)) {
                        PlayerProfile altProfile = altProfiles.get(originalUUID);
                        WrappedGameProfile newProfile = new WrappedGameProfile(altProfile.getId(), altProfile.getName());
                        gameProfiles.write(0, newProfile);
                    }
                }
            }
        });

        initBoiiConfig(new Config());

        registerListener(new ChangeProfileListener(this));
        registerCommand(new ChangeProfileCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void createAltProfile(Player player, String name) {
        PlayerProfile origProfile = origProfiles.get(player.getUniqueId());

        if (origProfile == null) {
            origProfile = player.getPlayerProfile();
        }

        UUID newUUID = UUID.nameUUIDFromBytes(name.getBytes());
        String newName = String.format("%s [%s]", origProfile.getName(), name);

        PlayerProfile altProfile = getServer().createProfileExact(newUUID, newName);

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
}
