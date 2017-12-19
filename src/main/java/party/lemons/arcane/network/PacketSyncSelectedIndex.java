package party.lemons.arcane.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import party.lemons.arcane.api.capability.PlayerData;

/**
 * Created by Sam on 11/12/2017.
 */
public class PacketSyncSelectedIndex implements IMessage
{
	public PacketSyncSelectedIndex(){}

	private int sel;

	public PacketSyncSelectedIndex(int ind)
	{
		this.sel = ind;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		sel = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(sel);
	}

	public static class Handler implements IMessageHandler<PacketSyncSelectedIndex, IMessage>
	{

		@Override
		public IMessage onMessage(PacketSyncSelectedIndex message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(() ->
			{
				EntityPlayerSP cp = Minecraft.getMinecraft().player;
				if(cp.hasCapability(PlayerData.CAPABILITY, null))
				{
					PlayerData caster = cp.getCapability(PlayerData.CAPABILITY, null);
					caster.setSelectedIndex(message.sel);
				}
			});
			return null;
		}
	}
}
