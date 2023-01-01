package top.hendrixshen.magiclib;

import carpet.CarpetServer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import top.hendrixshen.magiclib.api.rule.WrapperSettingManager;
import top.hendrixshen.magiclib.config.ConfigHandler;
import top.hendrixshen.magiclib.config.ConfigManager;
import top.hendrixshen.magiclib.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.annotation.Dependency;

public class MagicLib implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer {
    private static final int CONFIG_VERSION = 1;

    @Dependencies(and = @Dependency(value = "carpet"))
    @Override
    public void onInitialize() {
        WrapperSettingManager.register(MagicLibReference.getModId(), new MagicLibSettingManager(
                MagicLibReference.getModVersion(), MagicLibReference.getModId(), MagicLibReference.getCurrentModName()));
        CarpetServer.manageExtension(new MagicLibAddition());
        MagicLibReference.LOGGER.info("[{}]: Mod initialized - Version: {}", MagicLibReference.getModName(),
                MagicLibReference.getModVersion());
    }

    @Dependencies(and = @Dependency(value = "malilib"))
    @Override
    public void onInitializeClient() {
        ConfigManager cm = ConfigManager.get(MagicLibReference.getModId());
        cm.parseConfigClass(MagicLibConfigs.class);
        ConfigHandler configHandler = new ConfigHandler(MagicLibReference.getModId(), cm, CONFIG_VERSION);
        configHandler.postDeserializeCallback = MagicLibConfigs::postDeserialize;
        ConfigHandler.register(configHandler);
        MagicLibConfigs.init(cm);
    }

    @Override
    public void onInitializeServer() {
    }
}
