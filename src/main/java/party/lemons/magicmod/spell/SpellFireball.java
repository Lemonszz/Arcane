package party.lemons.magicmod.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.util.math.Vec3d;
import party.lemons.magicmod.api.spell.Spell;
import party.lemons.magicmod.api.spell.SpellPage;

/**
 * Created by Sam on 15/12/2017.
 */
public abstract class SpellFireball extends Spell
{
	public SpellFireball(SpellPage elemental)
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
			EntityFireball ball = createFireball(spell, player);
			Vec3d look = player.getLookVec();
			Vec3d eye = player.getPositionEyes(1.0F);
			Vec3d pos = eye.addVector(look.x * 2, look.y * 2, look.z * 2);
			ball.posX = pos.x;
			ball.posY = pos.y;
			ball.posZ = pos.z;

		//	ball.setVelocity(look.x / 2, look.y / 2, look.z / 2);

			ball.accelerationX = look.x / 5;
			ball.accelerationY = look.y / 5;
			ball.accelerationZ = look.z / 5;
			player.world.spawnEntity(ball);
		}

		useMana(player);
	}

	abstract EntityFireball createFireball(Spell spell, EntityPlayer player);
}
