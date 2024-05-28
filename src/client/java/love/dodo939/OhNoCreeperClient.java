package love.dodo939;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

@SuppressWarnings("deprecation")
public class OhNoCreeperClient implements ClientModInitializer {
	private static boolean alertEnabled = true;
	private static long lastAlertTime = 0;
    private static final long ALERT_INTERVAL = 500;
    private static final SoundEvent CREEPER_ALERT_SOUND = SoundEvents.BLOCK_NOTE_BLOCK_PLING.value();

	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && alertEnabled) {
                checkForCreepers(client);
            }
        });

		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            CommandToggle.register(dispatcher);
        });
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
	}

	private void checkForCreepers(MinecraftClient client) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAlertTime < ALERT_INTERVAL) {
            return;
        }

        boolean creeperNearby = false;
        for (Entity entity : client.world.getEntities()) {
            if (entity.getType() == EntityType.CREEPER && client.player.distanceTo(entity) <= 15) {
                creeperNearby = true;
                break;
            }
        }

        if (creeperNearby) {
            sendWarning(client);
            lastAlertTime = currentTime;
        }
    }

	private void sendWarning(MinecraftClient client) {
        client.player.sendMessage(Text.of("\u00a7cWarning: Creeper nearby!"), true);
        client.world.playSound(client.player.getX(), client.player.getY(), client.player.getZ(),
                CREEPER_ALERT_SOUND, SoundCategory.PLAYERS, 1.0F, 1.0F, false);
    }

	public static void setAlertEnabled(boolean enabled) {
        alertEnabled = enabled;
    }

    public static boolean isAlertEnabled() {
        return alertEnabled;
    }
}