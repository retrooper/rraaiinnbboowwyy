package org.hinoob.rainbowy.processor.impl;

import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerPositionAndLook;
import org.bukkit.util.Vector;
import org.hinoob.rainbowy.processor.Processor;
import org.hinoob.rainbowy.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovementProcessor extends Processor {

    public MovementProcessor(User user){
        super(user);
    }

    public double x, y, z;
    public double lastX, lastY, lastZ;
    public double deltaX, deltaY, deltaZ, lastDeltaX, lastDeltaY, lastDeltaZ;

    public boolean teleported;
    private final List<Vector> teleportProcessList = new ArrayList<>();
    private int teleportBufferTicks = 5;

    public boolean respawned;
    private int respawnBufferTicks = 5;

    public boolean lastLastClientGround, lastClientGround, clientGround;

    public void process(WrapperPlayClientPlayerFlying wrapper){
        lastLastClientGround = lastClientGround;
        lastClientGround = clientGround;
        clientGround = wrapper.isOnGround();

        if(teleported){
            if(teleportBufferTicks < 0){
                teleported = false;
            }else{
                teleportBufferTicks = teleportBufferTicks - 1;
            }
        }
        if(respawned && !user.getPlayer().isDead()){
            if(respawnBufferTicks < 0){
                respawned = false;
            }else{
                respawnBufferTicks = respawnBufferTicks - 1;
            }
        }

        if(wrapper.hasPositionChanged()){
            lastX = x;
            lastY = y;
            lastZ = z;

            x = wrapper.getLocation().getX();
            y = wrapper.getLocation().getY();
            z = wrapper.getLocation().getZ();

            lastDeltaX = deltaX;
            lastDeltaY = deltaY;
            lastDeltaZ = deltaZ;

            deltaX = x - lastX;
            deltaY = y - lastY;
            deltaZ = z - lastZ;

            if(wrapper.hasRotationChanged()){
                Optional<Vector> foundAny = teleportProcessList.stream().filter(p -> p.getX() == x && p.getY() == y && p.getZ() == z).findAny();

                if(foundAny.isPresent()){
                    teleportProcessList.removeIf(b -> b.equals(foundAny.get()));
                    teleportBufferTicks = 5;
                    teleported = true;
                }
            }
        }
    }

    public void handleTeleport(WrapperPlayServerPlayerPositionAndLook wrapper){
        teleportProcessList.add(new Vector(wrapper.getX(), wrapper.getY(), wrapper.getZ()));
    }
}
