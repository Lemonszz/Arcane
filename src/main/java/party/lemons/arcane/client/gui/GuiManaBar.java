package party.lemons.arcane.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.GL11;
import party.lemons.arcane.api.capability.PlayerData;
import party.lemons.arcane.config.ArcaneConstants;

/**
 * Created by Sam on 15/12/2017.
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = ArcaneConstants.MODID)
public class GuiManaBar extends Gui
{
	static ResourceLocation bars = new ResourceLocation("minecraft:textures/gui/bars.png");
	static int height = 47;
	public static int drawTime = 0;
	public static boolean pos = false;

	@SubscribeEvent
	public static void onGuiDrawPre(RenderGameOverlayEvent.Pre event)
	{
		drawTime--;

		if(event.getType() == RenderGameOverlayEvent.ElementType.ARMOR  && Minecraft.getMinecraft().player.getTotalArmorValue() > 0)
		{
			height = GuiIngameForge.left_height;
			pos=  true;
		}
		if(event.getType() == RenderGameOverlayEvent.ElementType.AIR && Minecraft.getMinecraft().player.getAir() < 300)
		{
			height = GuiIngameForge.right_height;
			pos = true;
		}

		if(event.getType() != RenderGameOverlayEvent.ElementType.ALL)
			return;

		if(drawTime > 0 || GuiRadialMenu.on)
			drawBar(event.getResolution());
	}

	public static void drawBar(ScaledResolution resolution)
	{
		PlayerData data = Minecraft.getMinecraft().player.getCapability(PlayerData.CAPABILITY, null);
		if(!pos)
			height = 47;

		int left = resolution.getScaledWidth() / 2 - 91;
		int top = resolution.getScaledHeight() - height;


		short barWidth = 182;
		float pc = 0;
		if(data.getMana() > 0)
		{
			pc = (data.getMana() / data.getMaxMana());
		}
		int filled = (int) (barWidth * pc);
		GL11.glPushMatrix();
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		Minecraft.getMinecraft().getTextureManager().bindTexture(bars);
		GuiUtils.drawTexturedModalRect(left, top, 0, 10, 182, 5, 0);

		if (filled > 0)
		{
			GuiUtils.drawTexturedModalRect(left, top, 0, 15, filled, 5, 0);
		}
		GuiUtils.drawTexturedModalRect(left, top, 0, 115, 182, 5, 0);

		GL11.glPopMatrix();
		pos = false;
	}
}
