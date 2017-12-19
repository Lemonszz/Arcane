package party.lemons.magicmod.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.config.GuiUtils;
import party.lemons.magicmod.api.capability.PlayerData;
import party.lemons.magicmod.network.ModNetwork;
import party.lemons.magicmod.network.PacketSendMouseOverEntity;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sam on 10/12/2017.
 */
public class ClientUtil
{

	public static RayTraceResult getMouseOverExtended(float distance) {
		Minecraft mc = FMLClientHandler.instance().getClient();
		Entity theRenderViewEntity = mc.getRenderViewEntity();
		AxisAlignedBB theViewBoundingBox = new AxisAlignedBB(theRenderViewEntity.posX - 0.5D,
				theRenderViewEntity.posY - 0.0D, theRenderViewEntity.posZ - 0.5D, theRenderViewEntity.posX + 0.5D,
				theRenderViewEntity.posY + 1.5D, theRenderViewEntity.posZ + 0.5D);
		RayTraceResult returnMOP = null;

		if (mc.world != null)
		{
			double var2 = distance;
			returnMOP = theRenderViewEntity.rayTrace(var2, 0);
			double calcdist = var2;
			Vec3d pos = theRenderViewEntity.getPositionEyes(0);
			var2 = calcdist;
			if (returnMOP != null)
			{
				calcdist = returnMOP.hitVec.distanceTo(pos);
			}
			Vec3d lookvec = theRenderViewEntity.getLook(0);
			Vec3d var8 = pos.addVector(lookvec.x * var2, lookvec.y * var2, lookvec.z * var2);
			Entity pointedEntity = null;
			float var9 = 1.0F;
			List<Entity> list = mc.world.getEntitiesWithinAABBExcludingEntity(theRenderViewEntity, theViewBoundingBox
					.expand(lookvec.x * var2, lookvec.y * var2, lookvec.z * var2).expand(var9, var9, var9));
			double d = calcdist;
			for (Entity entity : list) {
				if (entity.canBeCollidedWith()) {
					float bordersize = entity.getCollisionBorderSize();
					AxisAlignedBB aabb = new AxisAlignedBB(entity.posX - entity.width / 2, entity.posY,
							entity.posZ - entity.width / 2, entity.posX + entity.width / 2, entity.posY + entity.height,
							entity.posZ + entity.width / 2);
					aabb.expand(bordersize, bordersize, bordersize);
					RayTraceResult mop0 = aabb.calculateIntercept(pos, var8);
					if (aabb.contains(pos)) {
						if (0.0D < d || d == 0.0D) {
							pointedEntity = entity;
							d = 0.0D;
						}
					} else if (mop0 != null) {
						double d1 = pos.distanceTo(mop0.hitVec);
						if (d1 < d || d == 0.0D) {
							pointedEntity = entity;
							d = d1;
						}
					}
				}
			}
			PlayerData data = mc.player.getCapability(PlayerData.CAPABILITY, null);
			if(data.getLookEntity() != pointedEntity)
			{
				data.setLookEntity(pointedEntity);
				int id = -1;
				if(pointedEntity != null)
					id = pointedEntity.getEntityId();

				ModNetwork.NETWORK_INSTANCE.sendToServer(new PacketSendMouseOverEntity(id));
			}
			if (pointedEntity != null && (d < calcdist || returnMOP == null))
			{
				returnMOP = new RayTraceResult(pointedEntity);
			}
		}
		return returnMOP;
	}

}
