package org.hinoob.rainbowy.listener;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import org.bukkit.entity.Player;
import org.hinoob.rainbowy.user.User;
import org.hinoob.rainbowy.user.UserManager;
import org.hinoob.rainbowy.util.PacketUtil;

public class PacketListener extends PacketListenerAbstract {

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        User user = UserManager.getUser((Player) event.getPlayer());
        if(user == null) return;

        if(PacketUtil.isFlying(event) && !PacketUtil.isValidFlyingPacket(event)){
            // Don't let the AntiCheat to process the packet
            event.setCancelled(true);
            return;
        }

        //user.handlePacket(event);
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        User user = UserManager.getUser((Player) event.getPlayer());
        if(user == null) return;

        //user.handlePacket(event);
    }
}
