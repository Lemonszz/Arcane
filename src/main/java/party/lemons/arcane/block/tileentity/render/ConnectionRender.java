package party.lemons.arcane.block.tileentity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import party.lemons.arcane.block.tileentity.TileEntityManaNode;
import party.lemons.arcane.config.ArcaneConstants;

/**
 * Created by Sam on 3/01/2018.
 */
public class ConnectionRender extends TileEntitySpecialRenderer<TileEntityManaNode>
{
	public static final ResourceLocation BEAM_TEXTURES = new ResourceLocation(ArcaneConstants.MODID,"textures/misc/connection_beam.png");
	public static final ResourceLocation BEAM_TEXTURES_ALPAH = new ResourceLocation(ArcaneConstants.MODID,"textures/misc/connection_beam_al.png");

	@Override
	public void render(TileEntityManaNode te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		GlStateManager.pushMatrix();
		BlockPos pos1 = te.getPos();

		EntityPlayer player = Minecraft.getMinecraft().player;
		double tx = -(player.prevPosX + (player.posX - player.prevPosX) * partialTicks);
		double ty = -(player.prevPosY + (player.posY - player.prevPosY) * partialTicks);
		double tz = -(player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks);
		GlStateManager.translate(tx, ty, tz);

		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
		int time = (int) te.getWorld().getTotalWorldTime();

		for(BlockPos pos2 : te.getChildren())
		{
			this.bindTexture(BEAM_TEXTURES);
			renderBeam(partialTicks, pos1.getX() + 0.5, pos1.getY() + 0.5, pos1.getZ() + 0.5, time, pos2.getX() + 0.5, (pos2.getY() + 0.5) + (Math.sin((float)time / 1.8) / 20F), pos2.getZ() + 0.5, 0.2F, 128 ,3, 0.01F );
			this.bindTexture(BEAM_TEXTURES_ALPAH);
			renderBeam(partialTicks, pos1.getX() + 0.5, pos1.getY() + 0.5, pos1.getZ() + 0.5, time, pos2.getX() + 0.5, pos2.getY() + 0.5 , pos2.getZ() + 0.5, 0.5F, (int) (40 + (Math.sin(time / 2) * 10)), 8, 0.003F);
		}

		GlStateManager.popMatrix();

	}
	public static void renderBeam(float partialTIcks, double x1, double y1, double z1, int ticks, double x2, double y2, double z2, float size, int alpha, int sides, float speed)
	{
		float f = (float)(x2 - x1);
		float f1 = (float)(y2 - 0.0D - y1);
		float f2 = (float)(z2 - z1);
		float f3 = MathHelper.sqrt(f * f + f2 * f2);
		float f4 = MathHelper.sqrt(f * f + f1 * f1 + f2 * f2);
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)x1, (float)y1, (float)z1 );

		GlStateManager.rotate((float)(-Math.atan2((double)f2, (double)f)) * (180F / (float)Math.PI) - 90.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate((float)(-Math.atan2((double)f3, (double)f1)) * (180F / (float)Math.PI) - 90.0F, 1.0F, 0.0F, 0.0F);

		GlStateManager.enableAlpha();

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableCull();
		GlStateManager.enableNormalize();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

		float f5 = 0.0F - ((float)ticks + partialTIcks) * speed;
		float f6 = MathHelper.sqrt(f * f + f1 * f1 + f2 * f2) / 5 - ((float)ticks + partialTIcks) * speed;
		bufferbuilder.begin(5, DefaultVertexFormats.POSITION_TEX_COLOR);

		for (int j = 0; j <= sides; ++j)
		{
			float f7 = MathHelper.sin((float)(j % sides) * ((float)Math.PI * 2F) / sides) * size;
			float f8 = MathHelper.cos((float)(j % sides) * ((float)Math.PI * 2F) / sides) * size;
			float f9 = (float)(j % sides) / sides;

			bufferbuilder.pos((double)(f7 * 0.2F), (double)(f8 * 0.2F), 0.0D).tex((double)f9, (double)f5).color(255, 255, 255, alpha).endVertex();
			bufferbuilder.pos((double)f7 * 0.2F, (double)f8 * 0.2F, (double)f4).tex((double)f9, (double)f6).color(255, 255, 255, alpha).endVertex();
		}

		tessellator.draw();
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.enableCull();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.popMatrix();
	}

}
