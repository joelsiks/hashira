package hashira.items;

import java.util.List;
import hashira.Hashira;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class SolarFuel extends Item {

    public static final int BURN_TIME = 100;

    public SolarFuel(Settings settings) {
        super(new Item.Settings());
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        // Make the item invisible in the player's inventory
        return false;
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        // Do not add any tooltip information to prevent the item tooltip from being
        // displayed
    }

}
