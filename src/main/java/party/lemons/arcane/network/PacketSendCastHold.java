package party.lemons.arcane.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import party.lemons.arcane.api.capability.PlayerData;
import party.lemons.arcane.api.spell.Spell;
import party.lemons.arcane.api.spell.SpellRegistry;

/**
 * Created by Sam on 11/12/2017.
 */
public class PacketSendCastHold implements IMessage
{
	public PacketSendCastHold(){}

	private String spell;

	public PacketSendCastHold(String spell)
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

	public static class Handler implements IMessageHandler<PacketSendCastHold, IMessage>
	{

		@Override
		public IMessage onMessage(PacketSendCastHold message, MessageContext ctx)
		{
			EntityPlayerMP serverPlayer = ctx.getServerHandler().player;

			serverPlayer.getServerWorld().addScheduledTask(() ->
			{
				PlayerData caster = serverPlayer.getCapability(PlayerData.CAPABILITY, null);
				Spell sp = SpellRegistry.SPELL_REGISTRY.getValue(new ResourceLocation(message.spell));
				caster.setHoldingCast(true);

				if(caster.getHoldTime() == -1)
				{
					caster.setHoldTime(serverPlayer.world.getWorldTime());
				}
			});
			return null;
		}
	}
}
