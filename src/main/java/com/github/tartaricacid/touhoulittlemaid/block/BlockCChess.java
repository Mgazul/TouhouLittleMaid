package com.github.tartaricacid.touhoulittlemaid.block;

import com.github.tartaricacid.touhoulittlemaid.advancements.maid.TriggerType;
import com.github.tartaricacid.touhoulittlemaid.api.block.IBoardGameBlock;
import com.github.tartaricacid.touhoulittlemaid.api.game.xqwlight.Position;
import com.github.tartaricacid.touhoulittlemaid.block.properties.GomokuPart;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.favorability.Type;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySit;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.init.InitTrigger;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.CChessToClientMessage;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityCChess;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityJoy;
import com.github.tartaricacid.touhoulittlemaid.util.CChessUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BlockCChess extends BlockJoy implements IBoardGameBlock {
    public static final EnumProperty<GomokuPart> PART = EnumProperty.create("part", GomokuPart.class);
    public static final VoxelShape AABB = Block.box(0, 0, 0, 16, 2, 16);

    public BlockCChess() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 3.0F).forceSolidOn().noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(PART, GomokuPart.CENTER).setValue(FACING, Direction.NORTH));
    }

    private static void handleCChessRemove(Level world, BlockPos pos, BlockState state) {
        if (!world.isClientSide) {
            GomokuPart part = state.getValue(PART);
            BlockPos centerPos = pos.subtract(new Vec3i(part.getPosX(), 0, part.getPosY()));
            BlockEntity te = world.getBlockEntity(centerPos);
            popResource(world, centerPos, InitItems.CCHESS.get().getDefaultInstance());
            if (te instanceof TileEntityCChess) {
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        world.setBlockAndUpdate(centerPos.offset(i, 0, j), Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }
    }

    public static void maidMove(ServerPlayer player, Level level, BlockPos pos, int move, boolean maidLost, boolean playerLost) {
        if (level.getBlockEntity(pos) instanceof TileEntityCChess chess) {
            if (chess.isPlayerTurn()) {
                return;
            }

            Position chessData = chess.getChessData();
            UUID sitId = chess.getSitId();
            // 女仆输，以防作弊，再检查一次
            if (maidLost && CChessUtil.isMaid(chessData) && chessData.isMate()) {
                chess.setCheckmate(true);
                chess.refresh();

                if (level instanceof ServerLevel serverLevel && serverLevel.getEntity(sitId) instanceof EntitySit sit
                    && sit.getFirstPassenger() instanceof EntityMaid maid && maid.isOwnedBy(player)) {
                    // TODO: 暂时不加段位系统
                    maid.getFavorabilityManager().apply(Type.CCHESS_WIN);
                    InitTrigger.MAID_EVENT.trigger(player, TriggerType.WIN_CCHESS);
                }

                return;
            }

            boolean notChecked = chessData.makeMove(move);
            // 如果吃子了，那么重置计数器（该计数器用于判断自然限着和长将）
            if (notChecked && chessData.captured()) {
                chessData.setIrrev();
            }
            chess.setSelectChessPoint(Position.DST(move));
            chess.setCheckmate(playerLost);

            // 如果玩家没输，那么检查其他和局情况
            if (!playerLost) {
                if (CChessUtil.reachMoveLimit(chessData)) {
                    // 判断是否六十回自然限着
                    chess.setMoveNumberLimit(true);
                } else if (CChessUtil.isRepeat(chessData)) {
                    // 判断是否长打
                    chess.setRepeat(true);
                }
            }

            if (level instanceof ServerLevel serverLevel && serverLevel.getEntity(sitId) instanceof EntitySit sit && sit.getFirstPassenger() instanceof EntityMaid maid) {
                maid.swing(InteractionHand.MAIN_HAND);
            }
            level.playSound(null, pos, InitSounds.GOMOKU.get(), SoundSource.BLOCKS, 1.0f, 0.8F + level.random.nextFloat() * 0.4F);
            chess.refresh();
        }
    }

    @Override
    public void startMaidSit(EntityMaid maid, BlockState state, Level worldIn, BlockPos pos) {
        if (worldIn instanceof ServerLevel serverLevel && worldIn.getBlockEntity(pos) instanceof TileEntityJoy joy) {
            Entity oldSitEntity = serverLevel.getEntity(joy.getSitId());
            if (oldSitEntity != null && oldSitEntity.isAlive()) {
                return;
            }
            Direction face = state.getValue(FACING).getOpposite();
            Vec3 position = new Vec3(0.5 + face.getStepX() * 2, 0.1, 0.5 + face.getStepZ() * 2);
            EntitySit newSitEntity = new EntitySit(worldIn, Vec3.atLowerCornerWithOffset(pos, position.x, position.y, position.z), this.getTypeName(), pos);
            newSitEntity.setYRot(face.getOpposite().toYRot() + this.sitYRot());
            worldIn.addFreshEntity(newSitEntity);
            joy.setSitId(newSitEntity.getUUID());
            joy.setChanged();
            maid.startRiding(newSitEntity);
        }
    }

    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        handleCChessRemove(world, pos, state);
        super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public void onBlockExploded(BlockState state, Level world, BlockPos pos, Explosion explosion) {
        handleCChessRemove(world, pos, state);
        super.onBlockExploded(state, world, pos, explosion);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos centerPos = context.getClickedPos();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                BlockPos searchPos = centerPos.offset(i, 0, j);
                if (!context.getLevel().getBlockState(searchPos).canBeReplaced(context)) {
                    return null;
                }
            }
        }
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @javax.annotation.Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        if (worldIn.isClientSide) {
            return;
        }
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                BlockPos searchPos = pos.offset(i, 0, j);
                GomokuPart part = GomokuPart.getPartByPos(i, j);
                if (part != null && !part.isCenter()) {
                    worldIn.setBlock(searchPos, state.setValue(PART, part), Block.UPDATE_ALL);
                }
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level instanceof ServerLevel serverLevel && hand == InteractionHand.MAIN_HAND && player.getMainHandItem().isEmpty()) {
            GomokuPart part = state.getValue(PART);
            BlockPos centerPos = pos.subtract(new Vec3i(part.getPosX(), 0, part.getPosY()));
            BlockEntity te = level.getBlockEntity(centerPos);

            if (!(te instanceof TileEntityCChess chess)) {
                return InteractionResult.FAIL;
            }
            if (!chess.isPlayerTurn() && !chess.isCheckmate()) {
                return InteractionResult.FAIL;
            }

            // 检查女仆
            Entity sitEntity = serverLevel.getEntity(chess.getSitId());
            if (sitEntity == null || !sitEntity.isAlive() || !(sitEntity.getFirstPassenger() instanceof EntityMaid maid)) {
                player.sendSystemMessage(TComponent.translatable("message.touhou_little_maid.gomoku.no_maid"));
                return InteractionResult.FAIL;
            }
            // 检查是不是自己的女仆
            if (MaidConfig.MAID_GOMOKU_OWNER_LIMIT.get() && !maid.isOwnedBy(player)) {
                player.sendSystemMessage(TComponent.translatable("message.touhou_little_maid.gomoku.not_owner"));
                return InteractionResult.FAIL;
            }

            // 点击坐标的转换
            Direction facing = state.getValue(FACING);
            Vec3 clickPos = hit.getLocation()
                    .subtract(pos.getX(), pos.getY(), pos.getZ())
                    .add(part.getPosX() - 0.5, 0, part.getPosY() - 0.5)
                    .yRot(facing.toYRot() * Mth.DEG_TO_RAD);

            // 重置棋盘
            boolean clickResetArea = CChessUtil.isClickResetArea(clickPos);
            if (clickResetArea) {
                chess.reset();
                chess.refresh();
                level.playSound(null, centerPos, InitSounds.GOMOKU_RESET.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
            }

            // 没有点击到棋盘上，返回
            int nowClick = CChessUtil.getClickPosition(clickPos);
            if (nowClick < 0 || !Position.IN_BOARD(nowClick)) {
                return InteractionResult.PASS;
            }

            // 玩家已经输了，不能下棋
            if (chess.isCheckmate() && chess.isPlayerTurn()) {
                return InteractionResult.FAIL;
            }

            // 60 回合自然限着、长将不能下棋
            if (chess.isMoveNumberLimit() || chess.isRepeat()) {
                return InteractionResult.FAIL;
            }

            // 处理点击棋子的逻辑
            Position chessData = chess.getChessData();
            byte[] squares = chessData.squares;
            int preClick = chess.getSelectChessPoint();
            if (preClick < 0 || squares.length <= preClick) {
                preClick = 0;
            }
            byte prePiece = squares[preClick];
            byte nowPiece = squares[nowClick];

            // 如果前一个选择为空，或者选中的是黑方，说明没有选中棋子
            if (prePiece <= 0 || CChessUtil.isBlack(prePiece)) {
                // 当前点击的是红方棋子
                if (CChessUtil.isRed(nowPiece)) {
                    chess.setSelectChessPoint(nowClick);
                    chess.refresh();
                    level.playSound(null, pos, InitSounds.GOMOKU.get(), SoundSource.BLOCKS, 1.0f, 0.8F + level.random.nextFloat() * 0.4F);
                }
                return InteractionResult.SUCCESS;
            }

            // 如果选的都是红方棋子，重选
            if (CChessUtil.isRed(prePiece) && CChessUtil.isRed(nowPiece)) {
                chess.setSelectChessPoint(nowClick);
                chess.refresh();
                level.playSound(null, pos, InitSounds.GOMOKU.get(), SoundSource.BLOCKS, 1.0f, 0.8F + level.random.nextFloat() * 0.4F);
                return InteractionResult.SUCCESS;
            }

            // 判断移动是否合法
            int move = Position.MOVE(preClick, nowClick);
            if (!chessData.legalMove(move)) {
                return InteractionResult.FAIL;
            }

            // 没有将军，正常移动
            boolean notChecked = chessData.makeMove(move);
            if (notChecked) {
                // 如果吃子了，那么重置计数器（该计数器用于判断自然限着和长将）
                if (chessData.captured()) {
                    chessData.setIrrev();
                }
                chess.addChessCounter();
                chess.setSelectChessPoint(nowClick);
                chess.refresh();
                level.playSound(null, pos, InitSounds.GOMOKU.get(), SoundSource.BLOCKS, 1.0f, 0.8F + level.random.nextFloat() * 0.4F);
                NetworkHandler.sendToClientPlayer(new CChessToClientMessage(centerPos, chessData.toFen()), player);
                return InteractionResult.SUCCESS;
            }

            // 如果将军，那么给予提示
            player.sendSystemMessage(TComponent.translatable("message.touhou_little_maid.cchess.check"));
            level.playSound(null, pos, SoundEvents.NOTE_BLOCK_BELL.get(), SoundSource.BLOCKS, 1.0f, 0.8F + level.random.nextFloat() * 0.4F);
            return InteractionResult.FAIL;
        }
        return InteractionResult.PASS;
    }

    @Override
    protected Vec3 sitPosition() {
        return Vec3.ZERO;
    }

    @Override
    protected String getTypeName() {
        return Type.GOMOKU.getTypeName();
    }

    @Override
    protected int sitYRot() {
        return 0;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PART, FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if (state.getValue(PART).isCenter()) {
            return new TileEntityCChess(pos, state);
        }
        return null;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AABB;
    }
}
