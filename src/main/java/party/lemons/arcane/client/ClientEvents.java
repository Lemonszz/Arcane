package party.lemons.arcane.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import party.lemons.arcane.api.capability.PlayerData;
import party.lemons.arcane.api.spell.Spell;
import party.lemons.arcane.api.spell.SpellRegistry;
import party.lemons.arcane.api.spell.SpellUtil;
import party.lemons.arcane.client.gui.GuiManaBar;
import party.lemons.arcane.client.gui.GuiSpellBook;
import party.lemons.arcane.config.ArcaneConstants;
import party.lemons.arcane.network.ArcaneNetwork;
import party.lemons.arcane.network.PacketSendCastHold;
import party.lemons.arcane.network.PacketSendCastRelease;
import party.lemons.arcane.network.PacketSendCastSpell;

/**
 * Created by Sam on 10/12/2017.
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = ArcaneConstants.MODID)
public class ClientEvents
{
	public static TextureMap test;

	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;

		if(mc.world == null || mc.player == null)
			return;

		if(KeyBindings.keySpellbook.isPressed())
			mc.displayGuiScreen(new GuiSpellBook());

		PlayerData data = Minecraft.getMinecraft().player.getCapability(PlayerData.CAPABILITY, null);
		Spell sp = data.getSelectedSpells()[data.getSelectedIndex()];
		if(sp != null)
		{
			ClientUtil.getMouseOverExtended(sp.getSpellReach());
		}

		int maxDawTime = 5000;
		if(KeyBindings.keyCastSpell.isPressed())
		{
			if(sp != null && !data.isHoldingCast())
			{
				SpellUtil.castSpellPress(sp, player);
				ArcaneNetwork.NETWORK_INSTANCE.sendToServer(new PacketSendCastSpell(sp.getRegistryName().toString()));

				GuiManaBar.drawTime = maxDawTime;
			}
		}
		else if(KeyBindings.keyCastSpell.isKeyDown())
		{
			if(sp != null)
			{
				if(data.getHoldTime() == -1)
					data.setHoldTime(mc.world.getWorldTime());

				if(!data.isHoldingCast())
				{
					data.setHoldingCast(true);
					ArcaneNetwork.NETWORK_INSTANCE.sendToServer(new PacketSendCastHold(sp.getRegistryName().toString()));
				}
				GuiManaBar.drawTime = maxDawTime;
			}
		}

		if(data.isHoldingCast() && !KeyBindings.keyCastSpell.isKeyDown())
		{
			if(sp != null)
			{
				if(data.getHoldTime() == -1)
				{
					data.setHoldTime(-1);
					data.setHoldingCast(false);

					return;
				}
				long holdTime = mc.player.getEntityWorld().getTotalWorldTime() -  data.getHoldTime();
				SpellUtil.castSpellHoldRelease(sp, player, holdTime);
				ArcaneNetwork.NETWORK_INSTANCE.sendToServer(new PacketSendCastRelease(sp.getRegistryName().toString()));
				data.setHoldTime(-1);
				data.setHoldingCast(false);

				GuiManaBar.drawTime = maxDawTime;
			}
		}
	}

	@SubscribeEvent
	public static void onTextureStitchPre(TextureStitchEvent.Pre event)
	{
		for(Spell sp : SpellRegistry.SPELL_REGISTRY.getValues())
		{
			if(sp.getIconType() == Spell.IconType.TEXTURE)
			{
				TextureAtlasSprite spr = event.getMap().registerSprite(sp.getIconTexture());
				sp.setAtlasSprite(spr);
			}
		}
		test = event.getMap();
	}

	@SubscribeEvent
	public static void onHandRender(RenderHandEvent event)
	{
		PlayerData data = Minecraft.getMinecraft().player.getCapability(PlayerData.CAPABILITY, null);

		boolean showTime = Minecraft.getMinecraft().world.getTotalWorldTime() - data.getHoldTime() >= 2;
		if(data.isHoldingCast() && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0 && !Minecraft.getMinecraft().gameSettings.hideGUI && showTime)
		{
			event.setCanceled(true);
			Minecraft mc = Minecraft.getMinecraft();
			AbstractClientPlayer pla = mc.player;

			float e = (float) (((Math.sin((mc.world.getTotalWorldTime()) / 2) * (Math.PI / 20)))) - mc.player.swingProgress;
			mc.getItemRenderer().renderItemInFirstPerson(pla, event.getPartialTicks(), 0, EnumHand.MAIN_HAND, 0, ItemStack.EMPTY, e);
		}
	}
}
