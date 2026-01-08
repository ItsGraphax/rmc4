package de.itsgraphax.rmc4;

import de.itsgraphax.rmc4.commands.give_custom.GiveCustomCommand;
import de.itsgraphax.rmc4.commands.give_token.GiveTokenCommand;
import de.itsgraphax.rmc4.listeners.ClickListener;
import de.itsgraphax.rmc4.listeners.CraftingListener;
import de.itsgraphax.rmc4.listeners.InvBlockListeners;
import de.itsgraphax.rmc4.listeners.JoinListener;
import de.itsgraphax.rmc4.recipes.RecipeManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Rmc4 extends JavaPlugin {
    private void registerListeners() {
        PluginManager pm = getServer().getPluginManager();

        // Register Item Block Listener
        pm.registerEvents(new InvBlockListeners(this), this);

        // Register Crafting Listener
        pm.registerEvents(new CraftingListener(this), this);

        // Register Click Listener
        pm.registerEvents(new ClickListener(this), this);

        pm.registerEvents(new JoinListener(this), this);
    }

    private void registerCommands() {
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            GiveCustomCommand.register(commands.registrar(), this);
            GiveTokenCommand.register(commands.registrar(), this);
        });
        getComponentLogger().info("Commands registered");
    }

    private void setupConfig() {
        saveResource("config.yml", false);

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
    }
}