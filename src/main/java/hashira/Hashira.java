package hashira;

import hashira.items.Cell;
import hashira.items.Module;
import hashira.items.Panel;
import hashira.items.SolarFuel;
import hashira.blockentities.MountBlockEntity;
import hashira.blocks.Mount;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class Hashira implements ModInitializer {

    public static final String HASHIRA_GROUP_NAME = "Hashira";

    public static final Cell CELL = new Cell(new FabricItemSettings());
    public static final Module MODULE = new Module(new FabricItemSettings());
    public static final Panel PANEL = new Panel(new FabricItemSettings().maxCount(1));
    public static final SolarFuel SOLAR_FUEL = new SolarFuel(new FabricItemSettings());

    public static final Mount MOUNT_BLOCK = new Mount(FabricBlockSettings.create());
    public static final BlockItem MOUNT_ITEM = new BlockItem(MOUNT_BLOCK, new Item.Settings());

    public static final BlockEntityType<MountBlockEntity> MOUNT_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier("hashira", "mount_block_entity"),
            FabricBlockEntityTypeBuilder.create(MountBlockEntity::new, MOUNT_BLOCK).build());

    private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(PANEL))
            .displayName(Text.translatable(HASHIRA_GROUP_NAME))
            .entries((context, entries) -> {
                entries.add(CELL);
                entries.add(MODULE);
                entries.add(PANEL);
                entries.add(MOUNT_ITEM);
            })
            .build();

    @Override
    public void onInitialize() {
        Registry.register(Registries.ITEM, new Identifier("hashira", "cell"), CELL);
        Registry.register(Registries.ITEM, new Identifier("hashira", "module"), MODULE);
        Registry.register(Registries.ITEM, new Identifier("hashira", "panel"), PANEL);
        Registry.register(Registries.ITEM, new Identifier("hashira", "solar_fuel"), SOLAR_FUEL);

        Registry.register(Registries.BLOCK, new Identifier("hashira", "mount"), MOUNT_BLOCK);
        Registry.register(Registries.ITEM, new Identifier("hashira", "mount"), MOUNT_ITEM);

        Registry.register(Registries.ITEM_GROUP, new Identifier("hashira", "hashira_group"), ITEM_GROUP);

        FuelRegistry.INSTANCE.add(SOLAR_FUEL, SolarFuel.BURN_TIME);
    }

}
