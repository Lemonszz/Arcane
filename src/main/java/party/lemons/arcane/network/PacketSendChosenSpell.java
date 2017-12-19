package party.lemons.arcane.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
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
public class PacketSendChosenSpell implements IMessage
{
	public PacketSendChosenSpell(){}

	private String spell;
	private int index;

	public PacketSendChosenSpell(String spell, int index)
	{
		this.spell = spell;
		this.index = index;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		spell = ByteBufUtils.readUTF8String(buf);
		index = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, spell);
		buf.writeInt(index);
	}

	public static class Handler implements IMessageHandler<PacketSendChosenSpell, IMessage>
	{

		@Override
		public IMessage onMessage(PacketSendChosenSpell message, MessageContext ctx)
		{
			EntityPlayerMP serverPlayer = ctx.getServerHandler().player;

			serverPlayer.getServerWorld().addScheduledTask(() ->
			{
				PlayerData caster = serverPlayer.getCapability(PlayerData.CAPABILITY, null);
				if(!message.spell.equals("none"))
				{
					Spell sp = SpellRegistry.SPELL_REGISTRY.getValue(new ResourceLocation(message.spell));
					if(caster.hasSpellUnlocked(sp))
					{
						caster.setSelectedSpell(sp, message.index);
					}
				}
			});
			return null;
		}
	}
}
