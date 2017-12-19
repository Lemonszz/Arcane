package party.lemons.arcane.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import party.lemons.arcane.api.spell.Spell;
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
				Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);;
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
				bufferbuilder.pos((double)(xCoord + 0), (double)(yCoord + heightIn), (double)zLevel).tex((double)textureSprite.getMinU(), (double)textureSprite.getMaxV()).endVertex();
				bufferbuilder.pos((double)(xCoord + widthIn), (double)(yCoord + heightIn), (double)zLevel).tex((double)textureSprite.getMaxU(), (double)textureSprite.getMaxV()).endVertex();
				bufferbuilder.pos((double)(xCoord + widthIn), (double)(yCoord + 0), (double)zLevel).tex((double)textureSprite.getMaxU(), (double)textureSprite.getMinV()).endVertex();
				bufferbuilder.pos((double)(xCoord + 0), (double)(yCoord + 0), (double)zLevel).tex((double)textureSprite.getMinU(), (double)textureSprite.getMinV()).endVertex();
				tessellator.draw();
				break;
			case ITEMSTACK:
				Minecraft.getMinecraft().getRenderItem().zLevel = 1;
				Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(spell.getIconStack(), xCoord, yCoord);
				break;
		}
	}
}