package net.bivrik.fancytoasts.mixin;

import net.bivrik.fancytoasts.FancyToasts;
import net.bivrik.fancytoasts.toast.FancyAdvancementToast;
import net.minecraft.advancement.Advancement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.toast.AdvancementToast;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayDeque;
import java.util.Deque;

@Mixin(ToastManager.class)
public abstract class ToastManagerMixin {
	@Unique
	private static final Deque<FancyAdvancementToast> ADVANCEMENT_TOASTS = new ArrayDeque<>();
	@Unique
	private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

	@Unique
	private FancyAdvancementToast currentAdvancementToast;
	@Unique
	private long startTime;

	@Inject(at = @At("HEAD"), method = "add", cancellable = true)
	private void onAddToast(Toast toast, CallbackInfo info) {
		if (toast instanceof AdvancementToast) {
			info.cancel();
			Advancement advancement = ((AdvancementToastMixinAccessor) toast).getAdvancement().value();
			FancyAdvancementToast fancyAdvancement = new FancyAdvancementToast(advancement, FancyToasts.CONFIG.animationType, FancyToasts.CONFIG.textureType);
			ADVANCEMENT_TOASTS.add(fancyAdvancement);
		}
	}

	@Inject(at = @At("TAIL"), method = "draw")
	private void onDraw(DrawContext context, CallbackInfo info) {
		if (CLIENT.options.hudHidden) {
			return;
		}

		if (currentAdvancementToast == null) {
			if (!ADVANCEMENT_TOASTS.isEmpty()) {
				setCurrentAdvancement();
			}

			return;
		}
		else {
			if (!updateCurrentAdvancement()) {
				return;
			}
		}

		int xPos = (context.getScaledWindowWidth() / 2) - currentAdvancementToast.getWidth() / 2;

		var matrix = context.getMatrices();
		matrix.push();
		matrix.translate(xPos, 20, 0);
		currentAdvancementToast.draw(context, CLIENT.textRenderer);
		matrix.pop();
	}

	@Unique
	private void setCurrentAdvancement() {
		currentAdvancementToast = ADVANCEMENT_TOASTS.getFirst();
		currentAdvancementToast.startSoundQueue(CLIENT.getSoundManager());

		ADVANCEMENT_TOASTS.removeFirst();
		startTime = Util.getMeasuringTimeMs();
	}

	@Unique
	private boolean updateCurrentAdvancement() {
		long time = Util.getMeasuringTimeMs() - startTime;
		currentAdvancementToast.update(time);

		if (!currentAdvancementToast.getVisibility()) {
			currentAdvancementToast = null;
			return false;
		}

		return true;
	}

	@Inject(at = @At("HEAD"), method = "clear")
	private void onClear(CallbackInfo info) {
		ADVANCEMENT_TOASTS.clear();
		currentAdvancementToast = null;
	}
}