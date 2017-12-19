package party.lemons.arcane.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import party.lemons.arcane.api.spell.Spell;
import party.lemons.arcane.api.spell.SpellPage;

/**
 * Created by Sam on 18/12/2017.
 */
public class SpellBonemeal extends Spell
{
	private int range;

	public SpellBonemeal(SpellPage page, int range)
	{
		super(page);
		this.range = range;
	}

	public void castSpellPress(Spell spell, EntityPlayer player)
	{
		if(!canCast(player))
			return;

		Vec3d vec3 = player.getPositionEyes(1F);
		Vec3d look = player.getLook(1.0F);
		Vec3d vec3b = vec3.addVector(look.x * getSpellReach(), look.y * getSpellReach(), look.z * getSpellReach());

		RayTraceResult ray = player.world.rayTraceBlocks(vec3, vec3b, false, false, false);
		if(ray != null)
		{
			BlockPos rayPos = ray.getBlockPos();
			for(int x = -range / 2; x < (range / 2)  + 1; x++)
				for(int z =- range / 2; z < (range / 2) + 1; z++)
				{
					BlockPos offsetPos = new BlockPos(rayPos.getX() + x, rayPos.getY(), rayPos.getZ() + z);
					if(!player.world.isRemote)
						ItemDye.applyBonemeal(ItemStack.EMPTY, player.world, offsetPos);
					ItemDye.spawnBonemealParticles(player.world, offsetPos, 15);
				}
			}
		useMana(player);
	}

	@Override
	public float getSpellReach()
	{
		return 3 + range;
	}
}
