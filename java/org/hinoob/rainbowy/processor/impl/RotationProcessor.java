package org.hinoob.rainbowy.processor.impl;

import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import org.hinoob.rainbowy.processor.Processor;
import org.hinoob.rainbowy.user.User;

public class RotationProcessor extends Processor {

    public RotationProcessor(User user){
        super(user);
    }

    public float yaw, pitch, lastYaw, lastPitch;

    public void handleFlying(WrapperPlayClientPlayerFlying wrapper){
        if(wrapper.hasRotationChanged()){
            lastYaw = yaw;
            lastPitch = pitch;

            yaw = wrapper.getLocation().getYaw();
            pitch = wrapper.getLocation().getPitch();
        }
    }
}
