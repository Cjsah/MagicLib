package top.hendrixshen.magiclib.api.platform;

import com.google.common.base.Preconditions;
import lombok.Getter;
import top.hendrixshen.magiclib.MagicLib;

import java.util.concurrent.atomic.AtomicBoolean;

public final class PlatformManager {
    @Getter
    private final MagicLib magicLib;
    private IPlatform currentPlatform;
    private final AtomicBoolean initialized = new AtomicBoolean();

    public PlatformManager(MagicLib magicLib) {
        Preconditions.checkNotNull(magicLib);
        this.magicLib = magicLib;
        this.initialized.set(true);
    }

    public void register(IPlatform platform) {
        Preconditions.checkNotNull(platform);
        this.currentPlatform = platform;
        this.initialized.set(true);
    }

    public void unregister() {
        this.currentPlatform = null;
        this.initialized.set(false);
    }

    public IPlatform getCurrentPlatform() {
        if (this.initialized()) {
            return this.currentPlatform;
        }

        throw new IllegalStateException("Platform is not present!");
    }

    public boolean initialized() {
        return this.initialized.get();
    }
}
