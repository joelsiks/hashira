package hashira.blocks;

import com.mojang.serialization.MapCodec;

import java.util.List;

import hashira.Hashira;
import hashira.blockentities.MountBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class Mount extends BlockWithEntity {

    public static final BooleanProperty MOUNTED = BooleanProperty.of("mounted");

    public Mount(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(MOUNTED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(MOUNTED);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, BlockView world, List<Text> tooltip, TooltipContext tooltipContext) {
        itemStack.setCustomName(Text.translatable("block.hashira.mount.name"));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state,
            BlockEntityType<T> type) {
        return world.isClient ? null : validateTicker(type, Hashira.MOUNT_ENTITY, MountBlockEntity::serverTick);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult hit) {

        if (world.isClient)
            return ActionResult.SUCCESS;

        BlockEntity blockEntity = world.getBlockEntity(pos);
        Inventory mountInventory = (Inventory) blockEntity;
        MountBlockEntity mountBlockEntity = (MountBlockEntity) blockEntity;

        if (!player.getStackInHand(hand).isEmpty()) {
            if (mountInventory.getStack(0).isEmpty()) {
                // If the item is not a panel, ignore it.
                if (!player.getStackInHand(hand).getTranslationKey().equals("item.hashira.panel"))
                    return ActionResult.PASS;

                mountInventory.setStack(0, player.getStackInHand(hand).copy());
                player.getStackInHand(hand).setCount(0);
                mountBlockEntity.mounted = true;
                world.setBlockState(pos, state.with(MOUNTED, true));
                world.markDirty(pos);
            }
        } else {
            if (!mountInventory.getStack(0).isEmpty()) {
                player.getInventory().offerOrDrop(mountInventory.getStack(0));
                mountInventory.removeStack(0);
                mountBlockEntity.mounted = false;
                world.setBlockState(pos, state.with(MOUNTED, false));
                world.markDirty(pos);
            }
        }

        return ActionResult.PASS;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MountBlockEntity(pos, state);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        throw new UnsupportedOperationException("Unimplemented method 'getCodec'");
    }

}
