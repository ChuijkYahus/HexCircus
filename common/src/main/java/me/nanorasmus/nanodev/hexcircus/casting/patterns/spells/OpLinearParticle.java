package me.nanorasmus.nanodev.hexcircus.casting.patterns.spells;

import at.petrak.hexcasting.api.spell.ConstMediaAction;
import at.petrak.hexcasting.api.spell.OperationResult;
import at.petrak.hexcasting.api.spell.OperatorUtils;
import at.petrak.hexcasting.api.spell.casting.CastingContext;
import at.petrak.hexcasting.api.spell.casting.eval.SpellContinuation;
import at.petrak.hexcasting.api.spell.iota.*;
import dev.architectury.utils.GameInstance;
import me.nanorasmus.nanodev.hexcircus.networking.NetworkingHandler;
import me.nanorasmus.nanodev.hexcircus.networking.custom.SpawnBezierParticle;
import me.nanorasmus.nanodev.hexcircus.networking.custom.SpawnLinearParticle;
import me.nanorasmus.nanodev.hexcircus.particle.CastingParticles;
import net.minecraft.client.particle.Particle;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OpLinearParticle implements ConstMediaAction {
    @NotNull
    @Override
    public Text getDisplayName() {
        return DefaultImpls.getDisplayName(this);
    }

    @Override
    public boolean getAlwaysProcessGreatSpell() { return false; }

    @Override
    public boolean getCausesBlindDiversion() { return false; }

    @Override
    public boolean isGreat() { return false; }

    @Override
    public int getArgc() { return 4; }

    @Override
    public int getMediaCost() { return 1; }

    @NotNull
    @Override
    public List<Iota> execute(@NotNull List<? extends Iota> args, @NotNull CastingContext ctx) {
        // Get player in question
        Vec3d a = OperatorUtils.getVec3(args, 0, getArgc());
        ctx.assertVecInRange(a);
        Vec3d b = OperatorUtils.getVec3(args, 1, getArgc());
        ctx.assertVecInRange(b);
        Vec3d color = OperatorUtils.getVec3(args, 2, getArgc());
        Double speed = OperatorUtils.getDoubleBetween(args, 3, 0.1, 20, getArgc());

        // Send to all players
        ArrayList<ServerPlayerEntity> players = new ArrayList<>();
        players.addAll(GameInstance.getServer().getPlayerManager().getPlayerList());

        for (int i = 0; i < players.size(); i++) {
            ServerPlayerEntity p = players.get(i);
            NetworkingHandler.CHANNEL.sendToPlayer(p, new SpawnLinearParticle(a, b, speed.floatValue(), color));
        }

        return new ArrayList<>();
    }

    private float clamp01(double value) {
        return (float) Math.min(1, Math.max(0, value));
    }

    @NotNull
    @Override
    public OperationResult operate(SpellContinuation continuation, List<Iota> stack, Iota ravenmind, CastingContext castingContext){
        return ConstMediaAction.DefaultImpls.operate(this, continuation, stack, ravenmind, castingContext);
    }
}
