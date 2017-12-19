package party.lemons.arcane.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import party.lemons.arcane.api.spell.SpellPage;
import party.lemons.arcane.config.ArcaneConstants;

import java.util.ArrayList;

/**
 * Created by Sam on 11/12/2017.
 */
public class ButtonPageTab extends GuiButton
{
	private SpellPage page;
	private boolean selected;
	private final ResourceLocation _guiTexture = new ResourceLocation(ArcaneConstants.MODID + ":textures/gui/spellbook.png");

	public ButtonPageTab(int buttonId, int x, int y, SpellPage page)
	{
		super(buttonId, x, y,  31, 29,  "");
		this.page = page;
		selected = false;
	}

	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		if(this.visible)
		{
			mc.getTextureManager().bindTexture(_guiTexture);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = mouseX >= this.x - 15 && mouseY >= this.y && mouseX < this.x + this.width - 15 && mouseY < this.y + this.height;

			int xOffset = 0;

			if(hovered)
				xOffset += -4;

			if(selected)
				xOffset += -12;

			GuiUtils.drawTexturedModalRect(this.x + xOffset, this.y, 0, 195, 31, 29, -199);
			mc.getRenderItem().zLevel = -250;
			mc.getRenderItem().renderItemAndEffectIntoGUI(page.getDisplayStack(), (x + 6) + xOffset, y + 7);
			this.mouseDragged(mc, mouseX, mouseY);
		}
	}

	public void drawHover(Minecraft mc, int mouseX, int mouseY)
	{
		ArrayList<String> str = new ArrayList<>();
		str.add(page.getLocalizedName());
		ScaledResolution res = new ScaledResolution(mc);
		if(hovered)
			GuiUtils.drawHoveringText(str, mouseX, mouseY, res.getScaledWidth(), res.getScaledHeight(), 50, mc.fontRenderer);
	}

	public ButtonPageTab setSelected(boolean selected)
	{
		this.selected = selected;

		return this;
	}

	public SpellPage getPage()
	{
		return page;
	}
}
