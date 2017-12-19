package party.lemons.magicmod.client;

import com.google.common.base.MoreObjects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.GL11;
import party.lemons.magicmod.api.capability.PlayerData;
import party.lemons.magicmod.api.spell.Spell;
import party.lemons.magicmod.api.spell.SpellRegistry;
import party.lemons.magicmod.api.spell.SpellUtil;
import party.lemons.magicmod.client.gui.GuiManaBar;
import party.lemons.magicmod.client.gui.GuiSpellBook;
import party.lemons.magicmod.config.ModConstants;
import party.lemons.magicmod.network.ModNetwork;
import party.lemons.magicmod.network.PacketSendCastHold;
import party.lemons.magicmod.network.PacketSendCastRelease;
import party.lemons.magicmod.network.PacketSendCastSpell;

/**
 * Created by Sam on 10/12/2017.
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = ModConstants.MODID)
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
				ModNetwork.NETWORK_INSTANCE.sendToServer(new PacketSendCastSpell(sp.getRegistryName().toString()));

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
					ModNetwork.NETWORK_INSTANCE.sendToServer(new PacketSendCastHold(sp.getRegistryName().toString()));
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
				ModNetwork.NETWORK_INSTANCE.sendToServer(new PacketSendCastRelease(sp.getRegistryName().toString()));
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
