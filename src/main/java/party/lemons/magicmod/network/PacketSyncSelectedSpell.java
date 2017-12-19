package party.lemons.magicmod.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import party.lemons.magicmod.api.capability.PlayerData;
import party.lemons.magicmod.api.spell.Spell;
import party.lemons.magicmod.api.spell.SpellRegistry;

/**
 * Created by Sam on 11/12/2017.
 */
public class PacketSyncSelectedSpell implements IMessage
{
	public PacketSyncSelectedSpell(){}

	private String spell;
	private int index;

	public PacketSyncSelectedSpell(String spell, int index)
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

	public static class Handler implements IMessageHandler<PacketSyncSelectedSpell, IMessage>
	{

		@Override
		public IMessage onMessage(PacketSyncSelectedSpell message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(() ->
			{
				EntityPlayerSP cp = Minecraft.getMinecraft().player;
				if(cp.hasCapability(PlayerData.CAPABILITY, null))
				{
					PlayerData caster = cp.getCapability(PlayerData.CAPABILITY, null);
					Spell sp = SpellRegistry.SPELL_REGISTRY.getValue(new ResourceLocation(message.spell));
					caster.setSelectedSpell(sp, message.index);
				}
			});

			return null;
		}
	}
}
