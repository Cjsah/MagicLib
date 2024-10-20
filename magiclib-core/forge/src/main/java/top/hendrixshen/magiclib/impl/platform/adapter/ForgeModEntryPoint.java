package top.hendrixshen.magiclib.impl.platform.adapter;

import com.google.common.collect.Lists;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import top.hendrixshen.magiclib.api.entrypoint.ModInitializer;
import top.hendrixshen.magiclib.api.platform.adapter.ModEntryPointAdapter;
import top.hendrixshen.magiclib.util.ASMUtil;
import top.hendrixshen.magiclib.util.ReflectionUtil;
import top.hendrixshen.magiclib.util.collect.ValueContainer;
import top.hendrixshen.magiclib.util.mixin.MixinUtil;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ForgeModEntryPoint implements ModEntryPointAdapter {
    private final List<ClassNode> entryPoints;

    public ForgeModEntryPoint(@NotNull ForgeModContainer container) {
        this.entryPoints = ((ModInfo) container.get().getModInfo()).getOwningFile()
                .getFile()
                .getScanResult()
                .getClasses()
                .stream()
                .map(classData -> ReflectionUtil.<Type>getDeclaredFieldValue(classData.getClass(),
                        "clazz", classData))
                .map(ValueContainer::get)
                .map(type -> MixinUtil.getClassNode(type.getClassName()))
                .filter(Objects::nonNull)
                .filter(classNode -> ASMUtil.getVisibleAnnotations(classNode, Mod.class).isPresent())
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ClassNode> getEntryPoints() {
        return Lists.newArrayList(this.entryPoints);
    }

    @Override
    public Collection<ClassNode> getMagicEntryPoints() {
        return this.entryPoints.stream()
                .filter(classNode -> classNode.interfaces.contains(Type.getType(ModInitializer.class)
                        .getInternalName()))
                .collect(Collectors.toList());
    }
}
