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
public class PacketSendChosenNullSpell implements IMessage
{
	public PacketSendChosenNullSpell(){}

	private int index;

	public PacketSendChosenNullSpell(int index)
	{
		this.index = index;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		index = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(index);
	}

	public static class Handler implements IMessageHandler<PacketSendChosenNullSpell, IMessage>
	{

		@Override
		public IMessage onMessage(PacketSendChosenNullSpell message, MessageContext ctx)
		{
			EntityPlayerMP serverPlayer = ctx.getServerHandler().player;

			serverPlayer.getServerWorld().addScheduledTask(() ->
			{
				PlayerData caster = serverPlayer.getCapability(PlayerData.CAPABILITY, null);
				caster.setSelectedSpell(null, message.index);
			});
			return null;
		}
	}
}
