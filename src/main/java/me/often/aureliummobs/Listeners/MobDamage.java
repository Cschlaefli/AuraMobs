package me.often.aureliummobs.Listeners;

import me.often.aureliummobs.Entities.AureliumMob;
import me.often.aureliummobs.Main.Main;
import me.often.aureliummobs.Utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

public class MobDamage implements Listener {

    private Main plugin;

    public MobDamage(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onMobDamage(EntityDamageEvent e){

        if (!(e.getEntity() instanceof Monster m)){
            return;
        }

        if (!AureliumMob.isAureliumMob(m)){
            return;
        }

        int level = m.getPersistentDataContainer().get(Main.mobKey, PersistentDataType.INTEGER);
        double resHealth = BigDecimal.valueOf(m.getHealth() - e.getDamage()).setScale(2, RoundingMode.CEILING).doubleValue();
        try {
            m.setCustomName(ColorUtils.colorMessage(plugin.getConfigString("settings.name-format")
                    .replace("{mob}", plugin.getConfigString("mobs."+m.getType().name().toLowerCase()))
                    .replace("{lvl}", Integer.toString(level))
                    .replace("{health}", Double.toString(resHealth))
                    .replace("{maxhealth}", Double.toString(m.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()))
            ));
        } catch (NullPointerException ex){
            m.setCustomName(ColorUtils.colorMessage(plugin.getConfigString("settings.name-format")
                    .replace("{mob}", m.getType().name())
                    .replace("{lvl}", Integer.toString(level))
                    .replace("{health}", Double.toString(resHealth))
                    .replace("{maxhealth}", Double.toString(m.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()))
            ));
        }

    }

    @EventHandler(ignoreCancelled = true)
    public void onArrowHit(EntityDamageByEntityEvent e){

        if (!(e.getEntity() instanceof Projectile p)){
            return;
        }

        if (!(p.getShooter() instanceof Monster m)){
            return;
        }

        if (!AureliumMob.isAureliumMob(m)){
            return;
        }

        e.setDamage(m.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getValue());

    }

}
