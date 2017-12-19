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
public class PacketSyncCurrentMana implements IMessage
{
	public PacketSyncCurrentMana(){}

	private float maxMana;

	public PacketSyncCurrentMana(float mana)
	{
		this.maxMana = mana;
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

	public static class Handler implements IMessageHandler<PacketSyncCurrentMana, IMessage>
	{

		@Override
		public IMessage onMessage(PacketSyncCurrentMana message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(() ->
			{
				EntityPlayerSP cp = Minecraft.getMinecraft().player;
				if(cp.hasCapability(PlayerData.CAPABILITY, null))
				{
					PlayerData caster = cp.getCapability(PlayerData.CAPABILITY, null);
					caster.setMana(message.maxMana);
				}
			});
			return null;
		}
	}
}
