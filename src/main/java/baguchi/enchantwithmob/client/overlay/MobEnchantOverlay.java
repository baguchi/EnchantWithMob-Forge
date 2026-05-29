package baguchi.enchantwithmob.client.overlay;

import baguchi.enchantwithmob.EnchantConfig;
import baguchi.enchantwithmob.api.IEnchantCap;
import baguchi.enchantwithmob.capability.MobEnchantHandler;
import baguchi.enchantwithmob.mobenchant.MobEnchant;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.gui.GuiLayer;

public class MobEnchantOverlay implements GuiLayer {
    @Override
    public void render(GuiGraphicsExtractor guiGraphics, DeltaTracker partialTick) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.options.getCameraType().isMirrored() && !mc.options.hideGui) {
            if (EnchantConfig.CLIENT.showEnchantedMobHud.get() && mc.player != null) {
                if (mc.player instanceof IEnchantCap cap) {
                    if (cap.getEnchantCap().hasEnchant()) {
                        guiGraphics.text(mc.font, mc.player.getDisplayName(), (int) EnchantConfig.CLIENT.hudXPostion.getAsInt(), (int) EnchantConfig.CLIENT.hudYPostion.getAsInt(), -1);

                        for (MobEnchantHandler mobEnchantHandler : cap.getEnchantCap().getMobEnchants()) {

                            ChatFormatting[] textformatting = new ChatFormatting[]{ChatFormatting.AQUA};

                            Component s = mobEnchantHandler.getMobEnchant().value().getFullname(mobEnchantHandler.getEnchantLevel());

                            int xOffset = 20 + EnchantConfig.CLIENT.hudXPostion.getAsInt();
                            int yOffset = cap.getEnchantCap().getMobEnchants().indexOf(mobEnchantHandler) * 10 + 10 + EnchantConfig.CLIENT.hudYPostion.getAsInt();

                            guiGraphics.text(mc.font, s, (int) (xOffset), (int) yOffset, -1);
                        }
                    }
                }
            }
        } else {
            if (EnchantConfig.CLIENT.showEnchantedMobHud.get() && mc.crosshairPickEntity != null) {
                if (mc.crosshairPickEntity instanceof IEnchantCap cap) {
                    if (cap.getEnchantCap().hasEnchant()) {
                        guiGraphics.text(mc.font, mc.crosshairPickEntity.getDisplayName(), (int) EnchantConfig.CLIENT.hudXPostion.getAsInt(), (int) EnchantConfig.CLIENT.hudYPostion.getAsInt(), -1);

                        for (MobEnchantHandler mobEnchantHandler : cap.getEnchantCap().getMobEnchants()) {
                            Holder<MobEnchant> mobEnchant = mobEnchantHandler.getMobEnchant();
                            int mobEnchantLevel = mobEnchantHandler.getEnchantLevel();

                            ChatFormatting[] textformatting = new ChatFormatting[]{ChatFormatting.AQUA};

                            Component s = mobEnchantHandler.getMobEnchant().value().getFullname(mobEnchantHandler.getEnchantLevel());

                            int xOffset = 20 + EnchantConfig.CLIENT.hudXPostion.getAsInt();
                            int yOffset = cap.getEnchantCap().getMobEnchants().indexOf(mobEnchantHandler) * 10 + 10 + EnchantConfig.CLIENT.hudYPostion.getAsInt();

                            guiGraphics.text(mc.font, s, (int) (xOffset), (int) yOffset, -1);
                        }
                    }
                }
            }
        }
	}
}
