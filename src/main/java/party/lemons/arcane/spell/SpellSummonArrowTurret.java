package party.lemons.arcane.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import party.lemons.arcane.api.spell.Spell;
import party.lemons.arcane.api.spell.SpellPage;
import party.lemons.arcane.entity.EntityArrowTurret;

/**
 * Created by Sam on 16/12/2017.
 */
public class SpellSummonArrowTurret extends Spell
{
	double vel;

	public SpellSummonArrowTurret(SpellPage elemental)
	{
		super(elemental);
	}

	@Override
	public void castSpellPress(Spell spell, EntityPlayer player)
	{
		if(!canCast(player))
			return;

		if(!player.world.isRemote)
		{
			Vec3d vec3 = player.getPositionEyes(1F);
			Vec3d look = player.getLook(1.0F);
			Vec3d vec3b = vec3.addVector(look.x * getSpellReach(), look.y * getSpellReach(), look.z * getSpellReach());

			RayTraceResult ray = player.world.rayTraceBlocks(vec3, vec3b, false, true, false);
			if(ray != null)
			{
				BlockPos pos = ray.getBlockPos().up();
				EntityArrowTurret turret = new EntityArrowTurret(player.world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
				player.world.spawnEntity(turret);
			}
			}
		useMana(player);
	}

	@Override
	public float getSpellReach()
	{
		return 6;
	}

}
