package me.voidxwalker.worldpreview.mixin.client;

import me.voidxwalker.worldpreview.WorldPreview;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.options.AccessibilityOptionsScreen;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.Option;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AccessibilityOptionsScreen.class)
public class AccessibilityOptionsScreenMixin {

    private static boolean bl = true;

    @Mutable
    @Shadow @Final private static Option[] OPTIONS;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addAutoFreezeOption(Screen parent, GameOptions gameOptions, CallbackInfo ci){
        if(bl){
            Option[] safekeeping = OPTIONS;
            OPTIONS = new Option[safekeeping.length+1];
            int i = 0;
            for(Option option : safekeeping){
                OPTIONS[i] = option;
                i++;
            }
            OPTIONS[safekeeping.length] = WorldPreview.autoFreezeAtOption;
            bl = false;
        }
    }

}
