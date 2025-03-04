package com.darcosse.battlemons.mixin;

import com.cobblemon.mod.common.api.battles.interpreter.BattleContext;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.battles.BattleRegistry;
import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.client.gui.battle.BattleOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.DeltaTracker;
import java.util.UUID;


@Mixin(BattleOverlay.class)
public class RenderWeather {

    private String weather = "none";

    @Inject(method = "render", at = @At("TAIL"))
    private void findAndShowWeather(GuiGraphics context, DeltaTracker tickCounter, CallbackInfo ci) {

        this.weather = "none";

        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        int iconSize = 32;
        int x = (screenWidth / 2) - (iconSize / 2);
        int y = screenHeight - 40 - iconSize;

        UUID playerUUID = Minecraft.getInstance().player.getUUID();
        PokemonBattle battle = BattleRegistry.INSTANCE.getBattleByParticipatingPlayerId(playerUUID);


        if(battle != null) {
            if(battle.getContextManager().get(BattleContext.Type.WEATHER) != null) {
                if(!battle.getContextManager().get(BattleContext.Type.WEATHER).isEmpty()) {
                    this.weather = battle.getContextManager().get(BattleContext.Type.WEATHER).iterator().next().getId();
                } else {
                    this.weather = "none";
                }
            }
        }

        if(this.weather != null && CobblemonClient.INSTANCE.getBattle() != null && !CobblemonClient.INSTANCE.getBattle().getMinimised() && CobblemonClient.INSTANCE.getBattle().getMustChoose()) {
            context.blit(
                    ResourceLocation.parse("battlemons:textures/gui/weather/"+this.weather+".png"),
                    12, screenHeight - 113,
                    0, 0,
                    iconSize, iconSize,
                    iconSize, iconSize
            );
        }
    }
}