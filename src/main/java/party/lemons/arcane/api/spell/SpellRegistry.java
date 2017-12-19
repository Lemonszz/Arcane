package party.lemons.arcane.api.spell;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

/**
 * Created by Sam on 11/12/2017.
 */
@Mod.EventBusSubscriber
public class SpellRegistry
{
	public static IForgeRegistry<Spell> SPELL_REGISTRY;


	@SubscribeEvent
	public static void createRegistry(RegistryEvent.NewRegistry event)
	{
		SPELL_REGISTRY =
				new RegistryBuilder<Spell>()
				.setType(Spell.class)
				.setName(new ResourceLocation("arcane", "spells"))
				.create();
	}
}
