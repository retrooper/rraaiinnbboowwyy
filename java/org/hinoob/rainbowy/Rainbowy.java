package org.hinoob.rainbowy;

import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.plugin.java.JavaPlugin;
import org.hinoob.rainbowy.listener.PacketListener;
import org.hinoob.rainbowy.listener.PlayerListener;

public class Rainbowy extends JavaPlugin {

    private static Rainbowy INSTANCE;

    @Override
    public void onLoad() {
        INSTANCE = this;

        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().getSettings().debug(true);
        PacketEvents.getAPI().load();
    }
    
    @Override
    public void onEnable() {
        PacketEvents.getAPI().init();
        registerListeners();

        getLogger().info("Enabled");

    }

    @Override
    public void onDisable(){
        PacketEvents.getAPI().terminate();
    }

    private void registerListeners(){
        //PacketEvents.getAPI().getEventManager().registerListener(new PacketListener());
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

    public static Rainbowy getInstance() {
        return INSTANCE;
    }
}
