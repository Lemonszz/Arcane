package party.lemons.magicmod.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import party.lemons.magicmod.api.capability.PlayerData;
import party.lemons.magicmod.api.spell.Spell;
import party.lemons.magicmod.api.spell.SpellRegistry;
import party.lemons.magicmod.api.spell.SpellUtil;

/**
 * Created by Sam on 11/12/2017.
 */
public class PacketSendCastRelease implements IMessage
{
	public PacketSendCastRelease(){}

	private String spell;

	public PacketSendCastRelease(String spell)
	{
		this.spell = spell;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		spell = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, spell);
	}

	public static class Handler implements IMessageHandler<PacketSendCastRelease, IMessage>
	{

		@Override
		public IMessage onMessage(PacketSendCastRelease message, MessageContext ctx)
		{
			EntityPlayerMP serverPlayer = ctx.getServerHandler().player;

			serverPlayer.getServerWorld().addScheduledTask(() ->
			{
				PlayerData caster = serverPlayer.getCapability(PlayerData.CAPABILITY, null);
				Spell sp = SpellRegistry.SPELL_REGISTRY.getValue(new ResourceLocation(message.spell));

				long holdTime = serverPlayer.getEntityWorld().getTotalWorldTime() -  caster.getHoldTime();
				if(sp != null && caster.hasSpellUnlocked(sp))
				{
					SpellUtil.castSpellHoldRelease(sp, serverPlayer, holdTime);
				}
				caster.setHoldTime(-1);
				caster.setHoldingCast(false);
			});
			return null;
		}
	}
}
