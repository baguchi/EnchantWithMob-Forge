package baguchi.enchantwithmob.client.overlay;

import baguchi.enchantwithmob.EnchantConfig;
import baguchi.enchantwithmob.api.IEnchantCap;
import baguchi.enchantwithmob.capability.MobEnchantHandler;
import baguchi.enchantwithmob.mobenchant.MobEnchant;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.gui.GuiLayer;

public class MobEnchantOverlay implements GuiLayer {
    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker partialTick) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.options.getCameraType().isMirrored()) {
            if (EnchantConfig.CLIENT.showEnchantedMobHud.get() && mc.player != null) {
                if (mc.player instanceof IEnchantCap cap) {
                    if (cap.getEnchantCap().hasEnchant()) {
                        guiGraphics.drawString(mc.font, mc.player.getDisplayName(), (int) EnchantConfig.CLIENT.hudXPostion.getAsInt(), (int) EnchantConfig.CLIENT.hudYPostion.getAsInt(), 0xe0e0e0);

                        for (MobEnchantHandler mobEnchantHandler : cap.getEnchantCap().getMobEnchants()) {

                            ChatFormatting[] textformatting = new ChatFormatting[]{ChatFormatting.AQUA};

                            Component s = mobEnchantHandler.getMobEnchant().value().getFullname(mobEnchantHandler.getEnchantLevel());

                            int xOffset = 20;
                            int yOffset = cap.getEnchantCap().getMobEnchants().indexOf(mobEnchantHandler) * 10 + 10 + EnchantConfig.CLIENT.hudYPostion.getAsInt();

                            guiGraphics.drawString(mc.font, s, (int) (xOffset), (int) yOffset, -1);
                        }
                    }
                }
            }
        } else {
            if (EnchantConfig.CLIENT.showEnchantedMobHud.get() && mc.crosshairPickEntity != null) {
                if (mc.crosshairPickEntity instanceof IEnchantCap cap) {
                    if (cap.getEnchantCap().hasEnchant()) {
                        guiGraphics.drawString(mc.font, mc.crosshairPickEntity.getDisplayName(), (int) 20, (int) 50, 0xe0e0e0);

                        for (MobEnchantHandler mobEnchantHandler : cap.getEnchantCap().getMobEnchants()) {
                            Holder<MobEnchant> mobEnchant = mobEnchantHandler.getMobEnchant();
                            int mobEnchantLevel = mobEnchantHandler.getEnchantLevel();

                            ChatFormatting[] textformatting = new ChatFormatting[]{ChatFormatting.AQUA};

                            Component s = mobEnchantHandler.getMobEnchant().value().getFullname(mobEnchantHandler.getEnchantLevel());

                            int xOffset = 20;
                            int yOffset = cap.getEnchantCap().getMobEnchants().indexOf(mobEnchantHandler) * 10 + 60;

                            guiGraphics.drawString(mc.font, s, (int) (xOffset), (int) yOffset, -1);
                        }
                    }
                }
            }
        }
	}
}
