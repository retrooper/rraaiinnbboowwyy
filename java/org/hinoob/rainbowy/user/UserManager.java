package org.hinoob.rainbowy.user;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class UserManager {

    private static Map<Player, User> userMap = new HashMap<>();

    public static User getUser(Player player){
        if(player == null) return null;

        return userMap.getOrDefault(player, null);
    }

    public static void setUser(Player player){
        userMap.put(player, new User(player));
    }
}
