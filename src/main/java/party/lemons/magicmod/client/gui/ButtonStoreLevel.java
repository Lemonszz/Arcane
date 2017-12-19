package party.lemons.magicmod.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import party.lemons.magicmod.config.ModConstants;

import java.util.ArrayList;

/**
 * Created by Sam on 17/12/2017.
 */
public class ButtonStoreLevel extends GuiButton
{
	private final ResourceLocation _guiTexture = new ResourceLocation(ModConstants.MODID + ":textures/gui/spellbook.png");
	public ButtonStoreLevel(int buttonId, int x, int y)
	{
		super(buttonId, x, y, "");

		this.width = 13;
		this.height = 13;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		this.hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;

		mc.getTextureManager().bindTexture(_guiTexture);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		int xx = 107;
		int yy = 219;
		if(hovered && enabled)
			yy = 241;
		else if(!enabled)
			yy = 230;
		drawTexturedModalRect(x + 1, y + 1, xx, yy, 13, 13 - 2);

	}

}
