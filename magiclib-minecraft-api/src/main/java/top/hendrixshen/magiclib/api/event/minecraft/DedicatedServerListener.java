package top.hendrixshen.magiclib.api.event.minecraft;

import top.hendrixshen.magiclib.api.event.Listener;

public interface DedicatedServerListener extends Listener {
    void postServerInit();
}
