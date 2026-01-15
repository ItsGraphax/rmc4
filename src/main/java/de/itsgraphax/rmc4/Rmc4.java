package de.itsgraphax.rmc4;

import de.itsgraphax.rmc4.commands.give_custom.GiveCustomCommand;
import de.itsgraphax.rmc4.commands.give_token.GiveTokenCommand;
import de.itsgraphax.rmc4.commands.unequip_token.UnequipTokenCommand;
import de.itsgraphax.rmc4.listeners.*;
import de.itsgraphax.rmc4.managers.PlayerUiManager;
import de.itsgraphax.rmc4.recipes.RecipeManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Rmc4 extends JavaPlugin {
    private void registerListeners() {
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new InvBlockListeners(this), this);

        pm.registerEvents(new CraftingListener(this), this);

        pm.registerEvents(new ClickListener(this), this);

        pm.registerEvents(new JoinListener(this), this);

        pm.registerEvents(new DeathListener(this), this);

        pm.registerEvents(new BlockBreakListener(this), this);
    }

    private void registerCommands() {
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            GiveCustomCommand.register(commands.registrar(), this);
            GiveTokenCommand.register(commands.registrar(), this);
            UnequipTokenCommand.register(commands.registrar(), this);
        });
    }

    private void setupConfig() {
        saveDefaultConfig();
    }

    private void startTasks() {
        PlayerUiManager.updateForAllTask(this);
    }

    @Override
    public void onEnable() {
        setupConfig();

        registerListeners();

        RecipeManager.registerAll(this);

        registerCommands();

        startTasks();

        getComponentLogger().info("RuggiMC S4 plugin initiated!");
    }
}