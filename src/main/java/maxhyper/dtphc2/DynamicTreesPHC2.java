package maxhyper.dtphc2;

import com.ferreusveritas.dynamictrees.api.GatherDataHelper;
import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.block.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.block.rooty.SoilProperties;
import com.ferreusveritas.dynamictrees.init.DTConfigs;
import com.ferreusveritas.dynamictrees.systems.fruit.Fruit;
import com.ferreusveritas.dynamictrees.systems.pod.Pod;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.pam.pamhc2trees.config.EnableConfig;
import com.pam.pamhc2trees.init.ItemRegistry;
import maxhyper.dtphc2.init.DTPHC2Blocks;
import maxhyper.dtphc2.compat.DTPConfig;
import maxhyper.dtphc2.compat.DTPConfigProxy;
import maxhyper.dtphc2.compat.DefaultConfig;
import maxhyper.dtphc2.event.SpilePlacementEvent;
import maxhyper.dtphc2.init.DTPHC2Client;
import maxhyper.dtphc2.init.DTPHC2Registries;
import maxhyper.dtphc2.init.DTPHC2Items;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod(DynamicTreesPHC2.MOD_ID)
@Mod.EventBusSubscriber(modid = DynamicTreesPHC2.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DynamicTreesPHC2 {
    public static final String MOD_ID = "dtphc2";

    public static DTPConfigProxy DTPlusConfig;

    public DynamicTreesPHC2() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);
        bus.addListener(this::gatherData);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(SpilePlacementEvent.class);

        RegistryHandler.setup(MOD_ID);

        DTPHC2Blocks.register(bus);
        DTPHC2Items.register(bus);
        DTPHC2Registries.setup();
        DTPHC2Registries.SOUNDS.register(bus);

        if (ModList.get().isLoaded("dynamictreesplus"))
            DTPlusConfig = new DTPConfig();
        else
            DTPlusConfig = new DefaultConfig();
    }

    private void commonSetup(final FMLConstructModEvent event) {
        if (DTConfigs.WORLD_GEN.get()) {
            ForgeConfigSpec.BooleanValue[] test = {
                    EnableConfig.apple_worldgen,
                    EnableConfig.avocado_worldgen,
                    EnableConfig.candlenut_worldgen,
                    EnableConfig.cherry_worldgen,
                    EnableConfig.chestnut_worldgen,
                    EnableConfig.gooseberry_worldgen,
                    EnableConfig.lemon_worldgen,
                    EnableConfig.nutmeg_worldgen,
                    EnableConfig.orange_worldgen,
                    EnableConfig.peach_worldgen,
                    EnableConfig.pear_worldgen,
                    EnableConfig.plum_worldgen,
                    EnableConfig.walnut_worldgen,
                    EnableConfig.spiderweb_worldgen,
                    EnableConfig.hazelnut_worldgen,
                    EnableConfig.pawpaw_worldgen,
                    EnableConfig.soursop_worldgen,
                    EnableConfig.almond_worldgen,
                    EnableConfig.apricot_worldgen,
                    EnableConfig.banana_worldgen,
                    EnableConfig.cashew_worldgen,
                    EnableConfig.cinnamon_worldgen,
                    EnableConfig.coconut_worldgen,
                    EnableConfig.date_worldgen,
                    EnableConfig.dragonfruit_worldgen,
                    EnableConfig.durian_worldgen,
                    EnableConfig.fig_worldgen,
                    EnableConfig.grapefruit_worldgen,
                    EnableConfig.lime_worldgen,
                    EnableConfig.mango_worldgen,
                    EnableConfig.olive_worldgen,
                    EnableConfig.papaya_worldgen,
                    EnableConfig.paperbark_worldgen,
                    EnableConfig.pecan_worldgen,
                    EnableConfig.peppercorn_worldgen,
                    EnableConfig.persimmon_worldgen,
                    EnableConfig.pistachio_worldgen,
                    EnableConfig.pomegranate_worldgen,
                    EnableConfig.starfruit_worldgen,
                    EnableConfig.vanillabean_worldgen,
                    EnableConfig.breadfruit_worldgen,
                    EnableConfig.guava_worldgen,
                    EnableConfig.jackfruit_worldgen,
                    EnableConfig.lychee_worldgen,
                    EnableConfig.passionfruit_worldgen,
                    EnableConfig.rambutan_worldgen,
                    EnableConfig.tamarind_worldgen,
                    EnableConfig.maple_worldgen,
                    EnableConfig.pinenut_worldgen,
            };

            for (ForgeConfigSpec.BooleanValue v : test) {
                //v.set(false);
                //v.save();
            }
        }
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        DTPHC2Client.setup();
        DTPHC2Blocks.PASSION_FRUIT_VINE.get().setFruitStack(new ItemStack(ItemRegistry.passionfruititem.get()));
        DTPHC2Blocks.VANILLA_VINE.get().setFruitStack(new ItemStack(ItemRegistry.vanillabeanitem.get()));
        DTPHC2Blocks.PEPPERCORN_VINE.get().setFruitStack(new ItemStack(ItemRegistry.peppercornitem.get())).setOverripeFruitStack(new ItemStack(DTPHC2Items.RIPE_PEPPERCORN_ITEM.get()));
    }

    private void gatherData(final GatherDataEvent event) {
        GatherDataHelper.gatherAllData(MOD_ID, event,
                SoilProperties.REGISTRY,
                Family.REGISTRY,
                Species.REGISTRY,
                LeavesProperties.REGISTRY,
                Fruit.REGISTRY,
                Pod.REGISTRY
        );
    }

    public static ResourceLocation resLoc(final String path) {
        return new ResourceLocation(MOD_ID, path);
    }

}
