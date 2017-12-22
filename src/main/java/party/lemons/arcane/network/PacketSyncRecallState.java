package party.lemons.arcane.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import party.lemons.arcane.api.capability.PlayerData;
import party.lemons.arcane.spell.SpellRecall;

/**
 * Created by Sam on 11/12/2017.
 */
public class PacketSyncRecallState implements IMessage
{
	public PacketSyncRecallState(){}

	private int state;

	public PacketSyncRecallState(SpellRecall.RecallState state)
	{
		this.state = state.ordinal();
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		state = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(state);
	}

	public static class Handler implements IMessageHandler<PacketSyncRecallState, IMessage>
	{

		@Override
		public IMessage onMessage(PacketSyncRecallState message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(() ->
			{
				EntityPlayerSP cp = Minecraft.getMinecraft().player;
				if(cp.hasCapability(PlayerData.CAPABILITY, null))
				{
					PlayerData caster = cp.getCapability(PlayerData.CAPABILITY, null);
					caster.setRecallState(SpellRecall.RecallState.values()[message.state]);
				}
			});
			return null;
		}
	}
}
