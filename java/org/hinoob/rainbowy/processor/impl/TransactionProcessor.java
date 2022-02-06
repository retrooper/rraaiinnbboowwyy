package org.hinoob.rainbowy.processor.impl;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientWindowConfirmation;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowConfirmation;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.hinoob.rainbowy.Rainbowy;
import org.hinoob.rainbowy.processor.Processor;
import org.hinoob.rainbowy.user.User;

import java.util.ArrayDeque;
import java.util.Queue;

public class TransactionProcessor extends Processor {

    private BukkitTask taskId;
    private Queue<Runnable> runnables = new ArrayDeque<>();
    private int ticks = 0;

    public TransactionProcessor(User user){
        super(user);

        start();
    }

    private void start(){
        taskId = Bukkit.getScheduler().runTaskTimer(Rainbowy.getInstance(), new Runnable() {
            @Override
            public void run() {
                if(user.getPlayer() == null || !user.getPlayer().isOnline()){
                    // tf are we sending the packets to
                    Bukkit.getScheduler().cancelTask(taskId.getTaskId());
                    return;
                }

                ++ticks;
                if(ticks > 15){
                    PacketEvents.getAPI().getPlayerManager().sendPacket(user.getPlayer(), new WrapperPlayServerWindowConfirmation((byte) 0, (short)-100, false));
                }
            }
        }, 0, 1);
    }

    public void handleIncomingTrans(WrapperPlayClientWindowConfirmation wrapper){
        if(runnables.size() > 0){
            runnables.poll().run();
        }
    }

    public void addTask(Runnable runnable){
        runnables.add(runnable);
    }
}
