package top.hendrixshen.magiclib.impl.compat.modmenu;

import top.hendrixshen.magiclib.MagicLibReference;

public class WrapperModMenuApiImpl extends ModMenuApiImpl {
    @Override
    public String getModIdCompat() {
        return MagicLibReference.getModIdentifier();
    }
}
