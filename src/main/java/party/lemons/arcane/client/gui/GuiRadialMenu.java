package party.lemons.arcane.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import party.lemons.arcane.api.capability.PlayerData;
import party.lemons.arcane.api.spell.Spell;
import party.lemons.arcane.client.KeyBindings;
import party.lemons.arcane.client.SpellUtilClient;
import party.lemons.arcane.config.ArcaneConstants;
import party.lemons.arcane.network.ArcaneNetwork;
import party.lemons.arcane.network.PacketSendSelectedSpell;

/**
 * Created by Sam on 13/12/2017.
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = ArcaneConstants.MODID)
public class GuiRadialMenu extends GuiScreen
{
	public static final GuiRadialMenu instance = new GuiRadialMenu();
	public static boolean on = false;
	private int mX, mY;

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button)
	{
		if(!on)
			return;

		ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
		int texWidth = 129;
		int texHeight= 60;

		int sX = (resolution.getScaledWidth() / 2) - texWidth / 2;
		int sY = (resolution.getScaledHeight() / 2) - texHeight / 2;
		GuiUtils.drawTexturedModalRect(sX, sY, 127, 196, texWidth, texHeight, 0);
		if(button == 0)
		{
			for(int i = 0; i < 6; i++)
			{
				int xx = sX + 10 + (i * 18);
				int yy = (resolution.getScaledHeight() / 2) - 8;
				if(mouseX >= xx && mouseX <= xx + 18)
				{
					if(mouseY >= yy && mouseY <=yy + 18)
					{
						PlayerData data = Minecraft.getMinecraft().player.getCapability(PlayerData.CAPABILITY, null);
						data.setSelectedIndex(i);
						ArcaneNetwork.NETWORK_INSTANCE.sendToServer(new PacketSendSelectedSpell(i));
					}
				}

			}
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float pT)
	{
		mX = mouseX;
		mY = mouseY;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@SubscribeEvent
	public static void onRenderTick(TickEvent.RenderTickEvent event)
	{
		if(event.phase != TickEvent.Phase.END)
			return;

		Minecraft mc = Minecraft.getMinecraft();
		if(!mc.isGamePaused())
		{
			if(GuiRadialMenu.on)
			{
				drawRadialMenu(instance.mX, instance.mY);
			}
		}
	}

	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event)
	{
		if(event.phase != TickEvent.Phase.END)
			return;

		Minecraft mc = Minecraft.getMinecraft();

		if(Keyboard.isKeyDown(KeyBindings.keyRadialMenu.getKeyCode()))
		{
			if(mc.world != null && mc.currentScreen == null && !mc.isGamePaused())
			{
				GuiRadialMenu.on = true;
				mc.displayGuiScreen(GuiRadialMenu.instance);
			}
		}
		else
		{
			GuiRadialMenu.on = false;
			if(mc.currentScreen == GuiRadialMenu.instance)
			{
				mc.displayGuiScreen(null);
			}
		}

		if(GuiRadialMenu.on)
		{
			for(int i = 0; i < 6; ++i)
			{
				if(Keyboard.isKeyDown(mc.gameSettings.keyBindsHotbar[i].getKeyCode()))
				{
					PlayerData data = mc.player.getCapability(PlayerData.CAPABILITY, null);
					data.setSelectedIndex(i);
					ArcaneNetwork.NETWORK_INSTANCE.sendToServer(new PacketSendSelectedSpell(i));
				}
			}
		}
	}
	private static final ResourceLocation _guiTexture = new ResourceLocation(ArcaneConstants.MODID ,"textures/gui/spellbook.png");

	public static void drawRadialMenu(int mouseX, int mouseY)
	{
		Tooltip tooltip = new Tooltip();

		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution resolution = new ScaledResolution(mc);
		PlayerData data = mc.player.getCapability(PlayerData.CAPABILITY, null);

		int radius = 50;
		int width = (radius * 2) + 24;

		int dX = (resolution.getScaledWidth() / 2) - (radius) - 24;
		int dY = (resolution.getScaledHeight() / 2)- (radius) - 24;

		Spell[] spells = data.getSelectedSpells();
		double step = (2 * Math.PI) / 6;
		double angle = -step;

		for(int i = 0; i < 6; i++)
		{
			double x = width / 2 + radius * Math.cos(angle);
			double y = width / 2 + radius * Math.sin(angle);

			x += dX;
			y += dY;

			mc.getTextureManager().bindTexture(_guiTexture);
			if(data.getSelectedIndex() == i)
				GuiUtils.drawTexturedModalRect((int)x, (int)y, 99, 195, 24, 25, 0);
			else
				GuiUtils.drawTexturedModalRect((int)x, (int)y, 0, 224, 24, 25, 0);
			if(spells[i] != null)
			{
				SpellUtilClient.drawSpell((int)x + 4, (int)y + 4, spells[i]);
			}
			mc.fontRenderer.drawStringWithShadow(String.valueOf(i + 1), (int)x, (int)y, 0xFFFFFF);

			if(mouseX >= x && mouseX <= x + 24 && mouseY >= y && mouseY <= y + 24)
			{
				data.setSelectedIndex(i);
				ArcaneNetwork.NETWORK_INSTANCE.sendToServer(new PacketSendSelectedSpell(i));


				Spell sp = data.getSelectedSpells()[i];
				if(sp != null)
				{
					tooltip = SpellUtilClient.createSpellTooltip(sp, data);
				}
			}
			angle += step;
		}

		tooltip.draw(10,10, mc.fontRenderer, mc.getRenderItem());
	}
}
