package party.lemons.arcane.misc;

import java.util.Random;

/**
 * Created by Sam on 19/12/2017.
 */
public class ArcaneUtils
{
	@SafeVarargs
	public static <T> T choose(Random random, T... items)
	{
		return items[random.nextInt(items.length)];
	}
}