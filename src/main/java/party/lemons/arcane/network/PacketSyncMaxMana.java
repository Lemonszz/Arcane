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
public class PacketSyncMaxMana implements IMessage
{
	public PacketSyncMaxMana(){}

	private float maxMana;

	public PacketSyncMaxMana(float maxMana)
	{
		this.maxMana = maxMana;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		maxMana = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeFloat(maxMana);
	}

	public static class Handler implements IMessageHandler<PacketSyncMaxMana, IMessage>
	{

		@Override
		public IMessage onMessage(PacketSyncMaxMana message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(() ->
			{
				EntityPlayerSP cp = Minecraft.getMinecraft().player;
				if(cp.hasCapability(PlayerData.CAPABILITY, null))
				{
					PlayerData caster = cp.getCapability(PlayerData.CAPABILITY, null);
					caster.setMaxMana(message.maxMana);
				}
			});
			return null;
		}
	}
}
