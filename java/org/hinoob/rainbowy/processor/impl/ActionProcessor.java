package org.hinoob.rainbowy.processor.impl;

import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import org.hinoob.rainbowy.processor.Processor;
import org.hinoob.rainbowy.user.User;

public class ActionProcessor extends Processor {

    public ActionProcessor(User user){
        super(user);
    }

    public boolean sprinting, sneaking;

    public void handleAction(WrapperPlayClientEntityAction wrapper){
        switch (wrapper.getAction()){
            case START_SPRINTING:
                sprinting = true;
                break;
            case STOP_SPRINTING:
                sprinting = false;
                break;
            case START_SNEAKING:
                sneaking = true;
                break;
            case STOP_SNEAKING:
                sneaking = false;
                break;
        }
    }
}
