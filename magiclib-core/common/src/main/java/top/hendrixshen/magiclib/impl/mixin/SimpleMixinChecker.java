package top.hendrixshen.magiclib.impl.mixin;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.util.Annotations;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.dependency.DependencyCheckException;
import top.hendrixshen.magiclib.api.mixin.MixinDependencyChecker;
import top.hendrixshen.magiclib.api.mixin.MixinDependencyCheckFailureCallback;
import top.hendrixshen.magiclib.api.mixin.annotation.MagicMixinConfig;
import top.hendrixshen.magiclib.impl.dependency.DependenciesContainer;

import java.util.List;
import java.util.stream.Collectors;

public class SimpleMixinChecker implements MixinDependencyChecker {
    private MixinDependencyCheckFailureCallback failureCallback;
    public static final String OR = "Or:";

    @Override
    public boolean check(String targetClassName, String mixinClassName) {
        ClassNode targetClassNode;
        ClassNode mixinClassNode;

        try {
            targetClassNode = MixinService.getService().getBytecodeProvider().getClassNode(targetClassName);
            mixinClassNode = MixinService.getService().getBytecodeProvider().getClassNode(mixinClassName);
        } catch (Exception e) {
            MagicLib.getLogger().throwing(e);
            return true;
        }

        AnnotationNode mixinConfigNode = Annotations.getVisible(mixinClassNode, MagicMixinConfig.class);
        List<AnnotationNode> nodes = Annotations.getValue(mixinConfigNode, "dependencies", true);
        List<DependenciesContainer<ClassNode>> dependencies = nodes
                .stream()
                .map(node -> DependenciesContainer.of(node, targetClassNode))
                .collect(Collectors.toList());
        StringBuilder result = new StringBuilder();
        List<String> resultList = Lists.newArrayList();
        boolean ret = true;
        boolean first = true;

        for (DependenciesContainer<ClassNode> dependenciesContainer : dependencies) {
            boolean conflictSatisfied = dependenciesContainer.isConflictSatisfied();
            boolean requireSatisfied = dependenciesContainer.isRequireSatisfied();

            if ((!conflictSatisfied || !requireSatisfied) && !resultList.isEmpty()) {
                SimpleMixinChecker.checkNextLine(resultList);
                resultList.add(SimpleMixinChecker.OR);
                first = false;
            }

            if (!conflictSatisfied) {
                SimpleMixinChecker.checkNextLine(resultList);
                resultList.add("Conflicts:");
                dependenciesContainer.checkConflict().forEach(r -> {
                    resultList.add("\n");
                    resultList.add(String.format("\t%s", r.getReason()));
                });
            }

            if (!requireSatisfied) {
                SimpleMixinChecker.checkNextLine(resultList);
                resultList.add("Require:");
                dependenciesContainer.checkRequire().forEach(r -> {
                    resultList.add("\n");
                    resultList.add(String.format("\t%s", r.getReason()));
                });
            }

            ret = conflictSatisfied && requireSatisfied && ret;
        }

        for (String s: resultList) {
            result.append(!first && !s.equals(SimpleMixinChecker.OR) ? "\t" + s : s);
        }

        if (!ret) {
            this.onCheckFailure(targetClassName, mixinClassName, new DependencyCheckException(result.toString()));
        }

        return ret;
    }

    private static void checkNextLine(@NotNull List<String> list) {
        if (!list.isEmpty()) {
            list.add("\n");
        }
    }

    private void onCheckFailure(String targetClassName, String mixinClassName, DependencyCheckException result) {
        if (this.failureCallback != null) {
            this.failureCallback.callback(targetClassName, mixinClassName, result);
        }
    }

    @Override
    public void setCheckFailureCallback(MixinDependencyCheckFailureCallback callback) {
        this.failureCallback = callback;
    }
}
