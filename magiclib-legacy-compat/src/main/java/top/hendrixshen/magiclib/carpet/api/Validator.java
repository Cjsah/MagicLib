package top.hendrixshen.magiclib.carpet.api;

import carpet.settings.ParsedRule;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import top.hendrixshen.magiclib.carpet.impl.RuleHelper;
import top.hendrixshen.magiclib.carpet.impl.RuleOption;
import top.hendrixshen.magiclib.compat.minecraft.api.network.chat.ComponentCompatApi;
import top.hendrixshen.magiclib.impl.carpet.CarpetEntrypoint;
import top.hendrixshen.magiclib.util.MessageUtil;

//#if MC >= 11901
import carpet.api.settings.CarpetRule;
//#endif

//#if MC >= 11901
@SuppressWarnings("removal")
public abstract class Validator<T> extends carpet.api.settings.Validator<T> {
//#else
//$$ public abstract class Validator<T> extends carpet.settings.Validator<T> {
//#endif
    @Override
    //#if MC >= 11901
    public T validate(CommandSourceStack source, CarpetRule<T> carpetRule, T newValue, String userInput) {
        return this.validateCompat(source, RuleHelper.getSettingManager((ParsedRule<T>) carpetRule).getRuleOption((ParsedRule<T>) carpetRule), newValue, userInput);
    //#else
    //$$ public T validate(CommandSourceStack source, ParsedRule<T> carpetRule, T newValue, String userInput) {
    //$$     return this.validateCompat(source, RuleHelper.getSettingManager((ParsedRule<T>) carpetRule).getRuleOption(carpetRule), newValue, userInput);
    //#endif
    }

    //#if MC > 11502
    @Override
    //#if MC >= 11901
    public void notifyFailure(CommandSourceStack source, CarpetRule<T> carpetRule, String providedValue) {
    //#else
    //$$ public void notifyFailure(CommandSourceStack source, ParsedRule<T> carpetRule, String providedValue) {
    //#endif
    }
    //#endif

    public Component getDescription() {
        return null;
    }

    public T validateCompat(CommandSourceStack source, RuleOption ruleOption, T newValue, String userInput) {
        if (this.getValidValue(source, ruleOption, newValue, userInput) != null) {
            return this.getValidValue(source, ruleOption, newValue, userInput);
        }

        MessageUtil.sendMessage(source, ComponentCompatApi.literal(CarpetEntrypoint.getSettingManager().trUI("could_not_set",
                CarpetEntrypoint.getSettingManager().getTranslatedRuleName(ruleOption.getName()))).withStyle(style -> style.withColor(ChatFormatting.RED)));

        if (this.getDescription() != null) {
            MessageUtil.sendMessage(source, this.getDescription());
        }

        return null;
    }

    public abstract T getValidValue(CommandSourceStack source, RuleOption ruleOption, T newValue, String userInput);
}
