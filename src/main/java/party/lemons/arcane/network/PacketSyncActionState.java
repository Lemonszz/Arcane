package party.lemons.arcane.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import party.lemons.arcane.api.action.ActionState;
import party.lemons.arcane.api.capability.PlayerData;

/**
 * Created by Sam on 11/12/2017.
 */
public class PacketSyncActionState implements IMessage
{
	public PacketSyncActionState(){}

	NBTTagCompound tags;

	public PacketSyncActionState(ActionState state)
	{
		tags = state.writeToNBT();
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		tags = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeTag(buf, tags);
	}

	public static class Handler implements IMessageHandler<PacketSyncActionState, IMessage>
	{

		@Override
		public IMessage onMessage(PacketSyncActionState message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(() ->
			{
				EntityPlayerSP cp = Minecraft.getMinecraft().player;
				if(cp.hasCapability(PlayerData.CAPABILITY, null))
				{
					PlayerData caster = cp.getCapability(PlayerData.CAPABILITY, null);
					caster.setActionStateFromLoad(new ActionState(message.tags));
				}
			});
			return null;
		}
	}
}
