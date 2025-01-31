package maxhyper.dtphc2.blocks;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.trees.Species;
import maxhyper.dtphc2.genfeatures.DTPHC2GenFeatures;
import maxhyper.dtphc2.genfeatures.SyrupGenFeature;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class MapleSpileCommon extends HorizontalBlock {

    public static final BooleanProperty FILLED = BooleanProperty.create("filled");
    public static final int maxFilling = 3;
    public static final IntegerProperty FILLING = IntegerProperty.create("filling", 0, maxFilling);

    protected VoxelShape SHAPE_N;
    protected VoxelShape SHAPE_E;
    protected VoxelShape SHAPE_S;
    protected VoxelShape SHAPE_W;

    static VoxelShape makeShape() {
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.4375, 0.625, -0.0625, 0.5625, 0.75, 0.25), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.4375, 0.625, 0.25, 0.5625, 0.6875, 0.375), IBooleanFunction.OR);
        return shape;
    }

    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{shape, VoxelShapes.empty()};

        int times = (to.ordinal() - from.get2DDataValue() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = VoxelShapes.or(buffer[1], VoxelShapes.box(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = VoxelShapes.empty();
        }

        return buffer[0];
    }

    public MapleSpileCommon() {
        super(Properties.of(Material.METAL).sound(SoundType.METAL).strength(0.5f).randomTicks());
    }

    protected abstract boolean giveSyrup(World world, BlockPos pos, BlockState state, PlayerEntity player, BlockPos treePos);

    public static Item getSyrupItem (Species species){
        if (species == null) return Items.AIR;
        GenFeatureConfiguration featureConfig = species.getGenFeatures().stream().filter(fc -> fc.getGenFeature() == DTPHC2GenFeatures.SYRUP_GEN).findFirst().orElse(null);
        if (featureConfig == null) return Items.AIR;
        return ((SyrupGenFeature)DTPHC2GenFeatures.SYRUP_GEN).getSyrupItem(featureConfig);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext pContext) {
        BlockState blockstate = this.defaultBlockState();
        Direction direction = pContext.getClickedFace();
        if (direction.getAxis().isHorizontal())
            return blockstate.setValue(FACING, direction);
        return blockstate;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
        BlockPos offsetPos = pos.relative(state.getValue(FACING).getOpposite());
        BlockState offsetState = world.getBlockState(offsetPos);
        return TreeHelper.isBranch(offsetState) && TreeHelper.getRadius(world, pos) >= 7;
    }

    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, IWorld pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        return pFacing == pState.getValue(FACING).getOpposite() && !pState.canSurvive(pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        //noinspection DuplicatedCode
        switch (state.getValue(FACING)) {
            case EAST:
                return SHAPE_E;
            case SOUTH:
                return SHAPE_S;
            case WEST:
                return SHAPE_W;
            default:
                return SHAPE_N;
        }
        //return rotateShape(state.getValue(FACING), defaultBlockState().getValue(FACING), SHAPE);
    }

}
