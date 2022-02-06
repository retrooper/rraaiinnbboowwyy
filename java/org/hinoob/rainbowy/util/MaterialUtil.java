package org.hinoob.rainbowy.util;

import org.bukkit.Material;

public class MaterialUtil {

    public static boolean isWater(Material material){
        if(material == Material.WATER || material == Material.STATIONARY_WATER){
            return true;
        }

        return false;
    }

    public static boolean isLava(Material material){
        if(material == Material.LAVA || material == Material.STATIONARY_LAVA){
            return true;
        }

        return false;
    }

    public static boolean isClimbable(Material material){
        return material == Material.VINE || material == Material.LADDER;
    }
}
