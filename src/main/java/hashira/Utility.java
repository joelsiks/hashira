package hashira;

import java.util.ArrayList;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Utility {

    public static ArrayList<FurnaceBlockEntity> getPotentialNeighboringFurnaces(World world, BlockPos origin) {
        ArrayList<FurnaceBlockEntity> neighbors = getNeighboringFurnaces(world, origin);
        neighbors.removeIf(n -> furnaceIsLit(world, n.getPos()) || !furnaceHasItemsToBurn(n));
        return neighbors;
    }

    public static ArrayList<FurnaceBlockEntity> getNeighboringFurnaces(World world, BlockPos origin) {
        ArrayList<FurnaceBlockEntity> neighbors = new ArrayList<>();

        BlockPos[] neighborOffsets = {
                origin.up(), origin.down(), origin.north(), origin.south(), origin.east(), origin.west()
        };

        for (BlockPos offset : neighborOffsets) {
            BlockEntity blockEntity = world.getBlockEntity(offset);
            if (blockEntity != null && blockEntity instanceof FurnaceBlockEntity) {
                neighbors.add((FurnaceBlockEntity) blockEntity);
            }
        }

        return neighbors;
    }

    public static boolean furnaceIsLit(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.isOf(Blocks.FURNACE) && state.get(Properties.LIT);
    }

    public static boolean furnaceHasItemsToBurn(FurnaceBlockEntity furanceEntity) {
        return furanceEntity != null && furanceEntity.getStack(0).getCount() > 0;
    }

    public static boolean isDayTime(World world) {
        long time = world.getTime();
        return time >= 0 && time < 12000;
    }

    public static boolean isNightTime(World world) {
        long time = world.getTime();
        return time >= 12000 && time < 24000;
    }

}
