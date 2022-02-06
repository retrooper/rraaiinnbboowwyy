package org.hinoob.rainbowy.util;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;

public class PacketUtil {

    public static boolean isValidFlyingPacket(PacketReceiveEvent packet){
        if(isFlying(packet)){
            WrapperPlayClientPlayerFlying wrapper = new WrapperPlayClientPlayerFlying(packet);

            if(wrapper.hasPositionChanged()){
                return !Double.isNaN(wrapper.getLocation().getX()) && !Double.isNaN(wrapper.getLocation().getY()) && !Double.isNaN(wrapper.getLocation().getZ()) && !(wrapper.getLocation().getX() > Double.MAX_VALUE) && !(wrapper.getLocation().getY() > Double.MAX_VALUE) && !(wrapper.getLocation().getZ() > Double.MAX_VALUE);
            }else{
                return true;
            }
        }else{
            return false;
        }
    }

    public static boolean isFlying(PacketReceiveEvent packet){
        return packet.getPacketType() == PacketType.Play.Client.PLAYER_FLYING ||
                packet.getPacketType() == PacketType.Play.Client.PLAYER_POSITION ||
                packet.getPacketType() == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION ||
                packet.getPacketType() == PacketType.Play.Client.PLAYER_ROTATION;
    }
}
