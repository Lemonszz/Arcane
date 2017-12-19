package party.lemons.magicmod.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import party.lemons.magicmod.api.capability.PlayerData;

/**
 * Created by Sam on 11/12/2017.
 */
public class PacketUnlockSpell implements IMessage
{
	public PacketUnlockSpell(){}

	private String spell;

	public PacketUnlockSpell(String spell)
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

	public static class Handler implements IMessageHandler<PacketUnlockSpell, IMessage>
	{

		@Override
		public IMessage onMessage(PacketUnlockSpell message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(() ->
			{
				EntityPlayerSP cp = Minecraft.getMinecraft().player;
				if(cp.hasCapability(PlayerData.CAPABILITY, null))
				{
					PlayerData caster = cp.getCapability(PlayerData.CAPABILITY, null);
					caster.unlockSpell(message.spell);
				}

				PlayerData caster = cp.getCapability(PlayerData.CAPABILITY, null);
			});

			return null;
		}
	}
}
