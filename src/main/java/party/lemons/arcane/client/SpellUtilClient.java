package party.lemons.arcane.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import party.lemons.arcane.api.capability.PlayerData;
import party.lemons.arcane.api.spell.Spell;
import party.lemons.arcane.client.gui.Tooltip;
import party.lemons.arcane.config.ArcaneConstants;

/**
 * Created by Sam on 12/12/2017.
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = ArcaneConstants.MODID)
public class SpellUtilClient
{

	public static void drawSpell(int xCoord, int yCoord, Spell spell)
	{
		switch(spell.getIconType())
		{
			case TEXTURE:
				Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
				GlStateManager.color(1,1,1);
				GlStateManager.enableBlend();
				GlStateManager.enableAlpha();
				TextureAtlasSprite textureSprite = spell.getAtlasSprite();
				int widthIn = textureSprite.getIconWidth();
				int heightIn = textureSprite.getIconHeight();
				int zLevel = 0;
				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder bufferbuilder = tessellator.getBuffer();
				bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
				bufferbuilder.pos((double)(xCoord), (double)(yCoord + heightIn), (double)zLevel).tex((double)textureSprite.getMinU(), (double)textureSprite.getMaxV()).endVertex();
				bufferbuilder.pos((double)(xCoord + widthIn), (double)(yCoord + heightIn), (double)zLevel).tex((double)textureSprite.getMaxU(), (double)textureSprite.getMaxV()).endVertex();
				bufferbuilder.pos((double)(xCoord + widthIn), (double)(yCoord), (double)zLevel).tex((double)textureSprite.getMaxU(), (double)textureSprite.getMinV()).endVertex();
				bufferbuilder.pos((double)(xCoord), (double)(yCoord), (double)zLevel).tex((double)textureSprite.getMinU(), (double)textureSprite.getMinV()).endVertex();
				tessellator.draw();
				break;
			case ITEMSTACK:
				Minecraft.getMinecraft().getRenderItem().zLevel = 1;
				Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(spell.getIconStack(), xCoord, yCoord);
				break;
		}
	}

	public static Tooltip createSpellTooltip(Spell sp)
	{
		return createSpellTooltip(sp, Minecraft.getMinecraft().player.getCapability(PlayerData.CAPABILITY, null));
	}

	public static Tooltip createSpellTooltip(Spell sp, PlayerData data)
	{
		Tooltip tooltip = new Tooltip();

		tooltip.add(TextFormatting.LIGHT_PURPLE + sp.getLocalizedName());
		tooltip.add(TextFormatting.BLUE + I18n.format("magic.manacost") + ": " + (int)sp.getCastMana());
		tooltip.add(I18n.format(sp.getUnlocalizedDescription()));
		if(!data.hasSpellUnlocked(sp))
			tooltip.add(TextFormatting.RED + I18n.format("magic.locked") + ": " + sp.getUnlockCost());
		if(Minecraft.getMinecraft().gameSettings.advancedItemTooltips)
			tooltip.add(TextFormatting.DARK_GRAY + sp.getRegistryName().toString());

		return tooltip;
	}
}