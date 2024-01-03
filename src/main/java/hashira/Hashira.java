package hashira;

import hashira.items.Cell;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class Hashira implements ModInitializer {

    public static final String HASHIRA_GROUP_NAME = "Hashira";

    public static final Cell SOLAR_CELL = new Cell(new FabricItemSettings());

    private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(SOLAR_CELL))
            .displayName(Text.translatable(HASHIRA_GROUP_NAME))
            .entries((context, entries) -> {
                entries.add(SOLAR_CELL);
            })
            .build();

    @Override
    public void onInitialize() {
        Registry.register(Registries.ITEM, new Identifier("hashira", "solar_cell"), SOLAR_CELL);
        Registry.register(Registries.ITEM_GROUP, new Identifier("hashira", "hashira_group"), ITEM_GROUP);
    }

}
