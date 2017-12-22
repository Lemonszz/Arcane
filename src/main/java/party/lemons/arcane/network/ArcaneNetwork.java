package party.lemons.arcane.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import party.lemons.arcane.config.ArcaneConstants;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Sam on 11/12/2017.
 */
public class ArcaneNetwork
{
	public static final SimpleNetworkWrapper NETWORK_INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ArcaneConstants.MODID);

	public static void initNetwork()
	{
		int ind = 0;
		NETWORK_INSTANCE.registerMessage(PacketUnlockSpell.Handler.class, PacketUnlockSpell.class, ind++, Side.CLIENT);
		NETWORK_INSTANCE.registerMessage(PacketSyncSelectedIndex.Handler.class, PacketSyncSelectedIndex.class, ind++, Side.CLIENT);
		NETWORK_INSTANCE.registerMessage(PacketSyncSelectedSpell.Handler.class, PacketSyncSelectedSpell.class, ind++, Side.CLIENT);
		NETWORK_INSTANCE.registerMessage(PacketSendSelectedSpell.Handler.class, PacketSendSelectedSpell.class, ind++, Side.SERVER);
		NETWORK_INSTANCE.registerMessage(PacketSendChosenSpell.Handler.class, PacketSendChosenSpell.class, ind++, Side.SERVER);
		NETWORK_INSTANCE.registerMessage(PacketSendChosenNullSpell.Handler.class, PacketSendChosenNullSpell.class, ind++, Side.SERVER);
		NETWORK_INSTANCE.registerMessage(PacketSendCastSpell.Handler.class, PacketSendCastSpell.class, ind++, Side.SERVER);
		NETWORK_INSTANCE.registerMessage(PacketSendCastHold.Handler.class, PacketSendCastHold.class, ind++, Side.SERVER);
		NETWORK_INSTANCE.registerMessage(PacketSendCastRelease.Handler.class, PacketSendCastRelease.class, ind++, Side.SERVER);
		NETWORK_INSTANCE.registerMessage(PacketSendMouseOverEntity.Handler.class, PacketSendMouseOverEntity.class, ind++, Side.SERVER);
		NETWORK_INSTANCE.registerMessage(PacketSyncCurrentMana.Handler.class, PacketSyncCurrentMana.class, ind++, Side.CLIENT);
		NETWORK_INSTANCE.registerMessage(PacketSyncMaxMana.Handler.class, PacketSyncMaxMana.class, ind++, Side.CLIENT);
		NETWORK_INSTANCE.registerMessage(PacketClientDebugData.Handler.class, PacketClientDebugData.class, ind++, Side.CLIENT);
		NETWORK_INSTANCE.registerMessage(PacketSendAddPoint.Handler.class, PacketSendAddPoint.class, ind++, Side.SERVER);
		NETWORK_INSTANCE.registerMessage(PacketSyncStoredLevels.Handler.class, PacketSyncStoredLevels.class, ind++, Side.CLIENT);
		NETWORK_INSTANCE.registerMessage(PacketSendAddMaxMana.Handler.class, PacketSendAddMaxMana.class, ind++, Side.SERVER);
		NETWORK_INSTANCE.registerMessage(PacketSendUnlockSpell.Handler.class, PacketSendUnlockSpell.class, ind++, Side.SERVER);
		NETWORK_INSTANCE.registerMessage(PacketSyncRecallState.Handler.class, PacketSyncRecallState.class, ind++, Side.CLIENT);
	}
}
