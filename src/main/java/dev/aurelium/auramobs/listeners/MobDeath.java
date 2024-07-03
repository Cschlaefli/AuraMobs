package dev.aurelium.auramobs.listeners;

import dev.aurelium.auramobs.AuraMobs;
import dev.aurelium.auramobs.api.AuraMobsAPI;
import dev.aurelium.auramobs.util.MessageUtils;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class MobDeath implements Listener {

    private final AuraMobs plugin;

    public MobDeath(AuraMobs plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        LivingEntity entity = e.getEntity();

        if (!plugin.isAuraMob(entity)) {
            return;
        }

        entity.setCustomNameVisible(false);
        entity.setCustomName(null);
        // Set dropped exp accoring to xp_formula
        int mobLevel = AuraMobsAPI.getMobLevel(entity);
        int sourceXp = e.getDroppedExp();

        // Create and evaluate XP formula
        String defaultFormula = MessageUtils.setPlaceholders(null, plugin.optionString("mob_defaults.xp.formula"))
                .replace("{source_xp}", String.valueOf(sourceXp))
                .replace("{level}", String.valueOf(mobLevel));

        Expression formula = new ExpressionBuilder(defaultFormula).build();
        int modifiedXp = Math.max(1, (int) Math.round(formula.evaluate()));
        e.setDroppedExp(modifiedXp);

    }
}
