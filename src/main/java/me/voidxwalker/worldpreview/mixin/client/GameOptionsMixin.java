package me.voidxwalker.worldpreview.mixin.client;

import me.voidxwalker.worldpreview.KeyBindingHelper;
import me.voidxwalker.worldpreview.WorldPreview;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.KeyBinding;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

@Mixin(GameOptions.class)
public class GameOptionsMixin {
        @Mutable @Final
        @Shadow public KeyBinding[] keysAll;
        @Shadow @Final private File optionsFile;

        @Inject(at = @At("HEAD"), method = "load()V")
        public void loadHook(CallbackInfo info) {
            keysAll = KeyBindingHelper.process(keysAll);
        }

        @Inject(method = "load", at = @At("TAIL"))
        private void loadAutoFreezeAt(CallbackInfo ci){
                try {
                        if (!this.optionsFile.exists()) {
                                return;
                        }

                        try (Scanner scanner = new Scanner(optionsFile)) {
                                String string;
                                while ((string = scanner.nextLine()) != null) {
                                        String[] string_split = string.split(":");
                                        if(string_split[0].equals("AutoFreezePreviewAt")){
                                                WorldPreview.autoFreezeAt = Double.parseDouble(string_split[1]);
                                        }
                                }
                        } catch (FileNotFoundException ignored) {
                        }
                } catch (RuntimeException ignored) {
                }
        }

        @Inject(method = "write", at = @At("TAIL"))
        private void saveAutoFreezeAt(CallbackInfo ci){
                try{
                        Files.write(Paths.get("options.txt"),("AutoFreezePreviewAt:"+WorldPreview.autoFreezeAt).getBytes(), StandardOpenOption.APPEND);
                } catch (IOException e) {
                        throw new RuntimeException(e);
                }
        }

}
