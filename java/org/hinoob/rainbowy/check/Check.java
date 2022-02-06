package org.hinoob.rainbowy.check;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.hinoob.rainbowy.Rainbowy;
import org.hinoob.rainbowy.check.data.CheckInfo;
import org.hinoob.rainbowy.check.data.PunishType;
import org.hinoob.rainbowy.user.User;

public class Check {

    protected User user;

    private CheckInfo checkInfo;
    private boolean enabled = true;

    private int violations = 0;

    public Check(User user){
        this.user = user;

        if(getClass().isAnnotationPresent(CheckInfo.class)){
            this.checkInfo = getClass().getAnnotation(CheckInfo.class);
        }else{
            enabled = false;
            Rainbowy.getInstance().getLogger().info("Disabled '" + this.getClass().getSimpleName() + "'! (CheckInfo.class wasn't found)");
        }
    }

    protected void flag(String info){
        if(!enabled) return;

        ++violations;

        String message = ChatColor.translateAlternateColorCodes('&', "&8&l[&c<>&8&l]&7 &f&l%player%&7 flagged &c%check% &8(&7%info%&8)");
        message = message.replaceAll("%player%", user.getPlayer().getName());
        message = message.replaceAll("%check%", checkInfo.name());
        message = message.replaceAll("%info%", info);

        TextComponent textComponent = new TextComponent(message);
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&',
                "&7" + "Violations: &f" + violations + "/" + checkInfo.maximumViolations() + "\n" +
                        "&7" + "Punishable: &f" + (checkInfo.punishType() == PunishType.NONE ? "No" : "Yes")))
                .create()));

        for(Player player : Bukkit.getOnlinePlayers()){
            player.spigot().sendMessage(textComponent);
        }

        if(violations == checkInfo.maximumViolations() && checkInfo.punishType() != PunishType.NONE){
            switch (checkInfo.punishType()){
                case KICK:
                    Bukkit.getScheduler().runTask(Rainbowy.getInstance(), () -> {
                        user.getPlayer().kickPlayer(ChatColor.RED + "Cheat Detection");
                    });
                    break;
                case BAN:
                    Bukkit.getScheduler().runTask(Rainbowy.getInstance(), () -> {
                        if(Bukkit.getPluginManager().isPluginEnabled("LiteBans")){
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ipban -s " + user.getPlayer().getName() + " Cheat Detection");
                        }else{
                            Bukkit.getBanList(BanList.Type.IP).addBan(user.getPlayer().getName(), "Cheat Detection", null, "AntiCheat");
                        }
                    });
                    break;
            }
        }
    }
}
