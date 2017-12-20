package party.lemons.arcane.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import party.lemons.arcane.entity.EntityArrowTurret;

import javax.annotation.Nullable;

/**
 * Created by Sam on 20/12/2017.
 */
public class RenderArrowTurret extends RenderLiving<EntityArrowTurret>
{
	public RenderArrowTurret(RenderManager renderManager)
	{
		super(renderManager, new ModelArrowTurret(), 0.5F);
		this.addLayer(new LayerHeldItemTurret(this));
	}

	@Override
	public void doRender(EntityArrowTurret entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		double offset = -0.10 + ((y + Math.sin(entity.age * 0.05)) / 4);
		super.doRender(entity, x, y + offset, z, entityYaw, partialTicks);
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(EntityArrowTurret entity)
	{
		return null;
	}


	public static class ModelArrowTurret extends ModelBase
	{
		public ModelRenderer anchor;

		public ModelArrowTurret()
		{
			this.anchor = new ModelRenderer(this, 40, 16);
			this.anchor.setRotationPoint(0.0F, 4.0F, 0.0F);
			this.anchor.addBox(-1.0F, -2.0F, -1.0F, 2, 3, 2, 0.0F);
		}

		@Override
		public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
		{
			anchor.render(f5);
		}
	}

	public static class LayerHeldItemTurret implements LayerRenderer<EntityArrowTurret>
	{
		protected final RenderArrowTurret livingEntityRenderer;

		public LayerHeldItemTurret(RenderArrowTurret livingEntityRendererIn)
		{
			this.livingEntityRenderer = livingEntityRendererIn;
		}

		public void doRenderLayer(EntityArrowTurret entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
		{
			ItemStack itemstack = entitylivingbaseIn.getHeldItemMainhand();

			if(!itemstack.isEmpty())
			{
				this.renderHeldItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, EnumHandSide.RIGHT);
			}
		}

		private void renderHeldItem(EntityArrowTurret en, ItemStack p_188358_2_, ItemCameraTransforms.TransformType p_188358_3_, EnumHandSide handSide)
		{
			if(!p_188358_2_.isEmpty())
			{
				GlStateManager.pushMatrix();
				GlStateManager.translate((float) (1) / 16.0F, 0.125F, -0.625F);
				GlStateManager.rotate((float) en.getLookVec().x, 1, 0, 0);
				GlStateManager.rotate((float) en.getLookVec().y, 0, 1, 0);
				GlStateManager.rotate((float) en.getLookVec().z, 0, 0, 1);
				Minecraft.getMinecraft().getItemRenderer().renderItemSide(en, p_188358_2_, p_188358_3_, false);
				GlStateManager.popMatrix();
			}
		}
		public boolean shouldCombineTextures()
		{
			return false;
		}
	}
}
