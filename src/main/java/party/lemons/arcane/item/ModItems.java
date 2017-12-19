package party.lemons.arcane.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import party.lemons.arcane.config.ArcaneConstants;

/**
 * Created by Sam on 12/12/2017.
 */
@Mod.EventBusSubscriber(modid = ArcaneConstants.MODID)
public class ModItems
{
	public static Item spellTome, manaPotion;

	@SubscribeEvent
	public static void initItem(RegistryEvent.Register<Item> event)
	{
		spellTome = new ItemSpellTome().setRegistryName("spell_tome").setUnlocalizedName("spell_tome");
		manaPotion = new ItemManaPotion().setRegistryName("mana_potion").setUnlocalizedName("mana_potion");
		event.getRegistry().registerAll(spellTome, manaPotion);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void initModels(ModelRegistryEvent event)
	{
		ModelLoader.setCustomModelResourceLocation(spellTome, 0, new ModelResourceLocation(ArcaneConstants.MODID + ":spell_tome", "inventory"));
		ModelLoader.setCustomModelResourceLocation(manaPotion, 0, new ModelResourceLocation(ArcaneConstants.MODID + ":mana_potion", "inventory"));
	}
}
