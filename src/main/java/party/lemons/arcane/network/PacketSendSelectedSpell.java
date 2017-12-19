package party.lemons.arcane.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
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
public class PacketSendSelectedSpell implements IMessage
{
	public PacketSendSelectedSpell(){}

	private int index;

	public PacketSendSelectedSpell(int index)
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

	public static class Handler implements IMessageHandler<PacketSendSelectedSpell, IMessage>
	{

		@Override
		public IMessage onMessage(PacketSendSelectedSpell message, MessageContext ctx)
		{
			ctx.getServerHandler().player.getServerWorld().addScheduledTask(() ->
			{
				if(message.index >= 0 && message.index <= 5)
				{
					PlayerData data = ctx.getServerHandler().player.getCapability(PlayerData.CAPABILITY, null);
					data.setSelectedIndex(message.index);
				}
			});

			return null;
		}
	}
}
