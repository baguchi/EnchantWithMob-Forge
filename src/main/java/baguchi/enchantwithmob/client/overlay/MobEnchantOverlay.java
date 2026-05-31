package baguchi.enchantwithmob.client.overlay;

import baguchi.enchantwithmob.EnchantConfig;
import baguchi.enchantwithmob.attachment.MobEnchantAttachment;
import baguchi.enchantwithmob.attachment.MobEnchantContent;
import baguchi.enchantwithmob.registry.ModAttachments;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.gui.GuiLayer;

public class MobEnchantOverlay implements GuiLayer {
    @Override
    public void render(GuiGraphicsExtractor guiGraphics, DeltaTracker partialTick) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.options.getCameraType().isMirrored() && !mc.options.hideGui) {
            if (EnchantConfig.CLIENT.showEnchantedMobHud.get() && mc.player != null) {
                MobEnchantAttachment attachment = mc.player.getData(ModAttachments.MOB_ENCHANTS);

                if (attachment.hasEnchant()) {
                        guiGraphics.text(mc.font, mc.player.getDisplayName(), (int) EnchantConfig.CLIENT.hudXPostion.getAsInt(), (int) EnchantConfig.CLIENT.hudYPostion.getAsInt(), -1);

                    for (MobEnchantContent mobEnchantContent : attachment.getMobEnchants()) {

                            ChatFormatting[] textformatting = new ChatFormatting[]{ChatFormatting.AQUA};

                        Component s = mobEnchantContent.getMobEnchant().value().getFullname(mobEnchantContent.getEnchantLevel());

                            int xOffset = 20 + EnchantConfig.CLIENT.hudXPostion.getAsInt();
                        int yOffset = attachment.getMobEnchants().indexOf(mobEnchantContent) * 10 + 10 + EnchantConfig.CLIENT.hudYPostion.getAsInt();

                            guiGraphics.text(mc.font, s, (int) (xOffset), (int) yOffset, -1);
                        }
                    }
            }
        } else {
            if (EnchantConfig.CLIENT.showEnchantedMobHud.get() && mc.crosshairPickEntity != null) {
                MobEnchantAttachment attachment = mc.crosshairPickEntity.getData(ModAttachments.MOB_ENCHANTS);

                if (attachment.hasEnchant()) {
                        guiGraphics.text(mc.font, mc.crosshairPickEntity.getDisplayName(), (int) EnchantConfig.CLIENT.hudXPostion.getAsInt(), (int) EnchantConfig.CLIENT.hudYPostion.getAsInt(), -1);

                    for (MobEnchantContent mobEnchantContent : attachment.getMobEnchants()) {
                            ChatFormatting[] textformatting = new ChatFormatting[]{ChatFormatting.AQUA};

                        Component s = mobEnchantContent.getMobEnchant().value().getFullname(mobEnchantContent.getEnchantLevel());

                            int xOffset = 20 + EnchantConfig.CLIENT.hudXPostion.getAsInt();
                        int yOffset = attachment.getMobEnchants().indexOf(mobEnchantContent) * 10 + 10 + EnchantConfig.CLIENT.hudYPostion.getAsInt();

                            guiGraphics.text(mc.font, s, (int) (xOffset), (int) yOffset, -1);
                        }
                }
            }
        }
	}
}
