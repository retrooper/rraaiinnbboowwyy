package org.hinoob.rainbowy.user;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientWindowConfirmation;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerPositionAndLook;
import com.google.common.collect.Lists;
import org.bukkit.entity.Player;
import org.hinoob.rainbowy.check.Check;
import org.hinoob.rainbowy.check.data.CheckInfo;
import org.hinoob.rainbowy.processor.impl.*;
import org.hinoob.rainbowy.util.PacketUtil;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.nio.channels.Channel;
import java.util.*;

public class User {

    private Player player;
    private long joined;

    private List<Integer> preTrans = new ArrayList<>();

    private List<Check> checks = new ArrayList<>();
    private Map<Check, List<Method>> checkMethods = new HashMap<>();

    public MovementProcessor movementProcessor;
    public BlockProcessor blockProcessor;
    public TransactionProcessor transactionProcessor;
    public RotationProcessor rotationProcessor;
    public ActionProcessor actionProcessor;

    public User(Player player){
        this.player = player;
        this.joined = System.currentTimeMillis();

        loadChecks();
        loadProcessors();
    }

    public Player getPlayer() {
        return player;
    }

    public long getJoined() {
        return joined;
    }

    private void loadChecks(){
        try {
            for(Class<?> checkClazz : new Reflections("org.hinoob.rainbowy.check.impl").getSubTypesOf(Check.class)){
                if(checkClazz.isAnnotationPresent(CheckInfo.class)){
                    Check check = (Check) checkClazz.getConstructor(User.class).newInstance(this);
                    checks.add(check);

                    checkMethods.put(check, Lists.newArrayList());
                }
            }

            for(Check check : checkMethods.keySet()){
                List<Method> methods = new ArrayList<>();

                for(Method method : check.getClass().getDeclaredMethods()){
                    if(method.getParameterCount() == 1){
                        if(method.getParameters()[0].getType().equals(PacketPlaySendEvent.class) || method.getParameters()[0].getType().equals(PacketPlayReceiveEvent.class)){
                            methods.add(method);
                        }
                    }
                }

                checkMethods.put(check, methods);
            }
        }catch(Exception ignored){
        }
    }

    private void loadProcessors(){
        movementProcessor = new MovementProcessor(this);
        blockProcessor = new BlockProcessor(this);
        transactionProcessor = new TransactionProcessor(this);
        rotationProcessor = new RotationProcessor(this);
        actionProcessor = new ActionProcessor(this);
    }

    public void handlePacket(PacketReceiveEvent packet){
        if(PacketUtil.isFlying(packet)) {
            movementProcessor.process(new WrapperPlayClientPlayerFlying(packet));
            rotationProcessor.handleFlying(new WrapperPlayClientPlayerFlying(packet));
        }else if(packet.getPacketType() == PacketType.Play.Client.WINDOW_CONFIRMATION){
            transactionProcessor.handleIncomingTrans(new WrapperPlayClientWindowConfirmation(packet));
        }else if(packet.getPacketType() == PacketType.Play.Client.ENTITY_ACTION){
            actionProcessor.handleAction(new WrapperPlayClientEntityAction(packet));
        }

        for(Check check : checkMethods.keySet()){
            List<Method> methods = checkMethods.get(check);

            try {
                for(Method method : methods){
                    if(method.getParameterCount() == 1 && method.getParameters()[0].getType().equals(PacketPlayReceiveEvent.class)){
                        method.invoke(check, packet);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void handlePacket(PacketSendEvent packet){
        if(packet.getPacketType() == PacketType.Play.Server.PLAYER_POSITION_AND_LOOK){
            movementProcessor.handleTeleport(new WrapperPlayServerPlayerPositionAndLook(packet));
        }else if(packet.getPacketType() == PacketType.Play.Server.CHUNK_DATA || packet.getPacketType() == PacketType.Play.Server.MAP_CHUNK_BULK || packet.getPacketType() == PacketType.Play.Server.BLOCK_CHANGE) {
            blockProcessor.handle(packet);
        }

        for(Check check : checkMethods.keySet()){
            List<Method> methods = checkMethods.get(check);

            try {
                for(Method method : methods){
                    if(method.getParameterCount() == 1 && method.getParameters()[0].getType().equals(PacketPlaySendEvent.class)){
                        method.invoke(check, packet);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
