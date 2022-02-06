package org.hinoob.rainbowy.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.hinoob.rainbowy.user.UserManager;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        UserManager.setUser(e.getPlayer());
    }
}
