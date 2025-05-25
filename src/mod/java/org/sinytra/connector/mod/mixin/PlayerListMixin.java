package org.sinytra.connector.mod.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.List;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {
    @Shadow
    @Final
    private List<ServerPlayer> players;

    // See: https://github.com/Sinytra/Connector/issues/392
    @Inject(method = "getPlayers", at = @At("HEAD"), cancellable = true)
    private void onGetPlayers(CallbackInfoReturnable<List<ServerPlayer>> cir) {
        // Ensure returned view is up-to-date
        // Mods may replace the value of players, invalidating the view initially created by Neo
        cir.setReturnValue(Collections.unmodifiableList(players));
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(CallbackInfoReturnable<Void> cir) {
        // 确保类被正确初始化
        try {
            Class.forName("net.minecraft.server.players.PlayerList", true, this.getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            // 记录错误但不中断执行
            System.err.println("Warning: Could not initialize PlayerList class: " + e.getMessage());
        }
    }
}
