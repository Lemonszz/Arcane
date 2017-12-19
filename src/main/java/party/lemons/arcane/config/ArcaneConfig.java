package party.lemons.arcane.config;

import net.minecraft.block.Block;
import net.minecraftforge.common.config.Config;

import java.util.Arrays;

/**
 * Created by Sam on 19/12/2017.
 */
@Config(modid = ArcaneConstants.MODID)
public class ArcaneConfig
{
	@Config.Comment("Blocks effected by earth spells")
	public static String[] earth_blocks =
			{
					"minecraft:dirt",
					"minecraft:stone",
					"minecraft:grass",
					"minecraft:cobblestone",
					"minecraft:sand",
					"minecraft:gravel",
					"minecraft:sandstone",
					"minecraft:coal_ore",
					"minecraft:iron_ore",
					"minecraft:gold_ore",
					"minecraft:emerald_ore",
					"minecraft:diamond_ore",
					"minecraft:redstone_ore",
					"minecraft:lapis_ore",
					"minecraft:netherrack",
					"minecraft:soul_sand",
					"minecraft:quartz_ore",
					"minecraft:mossy_cobblestone",
					"minecraft:clay",
					"minecraft:endstone",
					"minecraft:magma",
					"minecraft:cactus",
					"minecraft:mycelium",
					"minecraft:stained_hardened_clay",
					"minecraft:hardened_clay",
					"minecraft:concrete",
					"minecraft:concrete_powder"
			};


	public static boolean isEarthBlock(Block block)
	{
		return Arrays.stream(earth_blocks).anyMatch(x -> block.getRegistryName().toString().equalsIgnoreCase(x));
	}

}
