package love.dodo939;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;

import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class CommandToggle {
    @SuppressWarnings("resource")
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("OhNoCreeperClient")
                .then(CommandManager.literal("toggle")
                        .then(CommandManager.argument("enabled", BoolArgumentType.bool())
                                .executes(context -> {
                                    boolean enabled = BoolArgumentType.getBool(context, "enabled");
                                    OhNoCreeperClient.setAlertEnabled(enabled);
                                    MinecraftClient.getInstance().player.sendMessage(Text.of("Creeper alert " + (enabled ? "enabled" : "disabled")), false);
                                    return 1;
                                })))
                .then(CommandManager.literal("status")
                        .executes(context -> {
                            boolean enabled = OhNoCreeperClient.isAlertEnabled();
                            MinecraftClient.getInstance().player.sendMessage(Text.of("Creeper alert is " + (enabled ? "enabled" : "disabled")), false);
                            return 1;
                        })));
    }
}
