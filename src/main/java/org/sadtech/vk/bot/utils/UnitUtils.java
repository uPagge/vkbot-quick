package org.sadtech.vk.bot.utils;

import org.sadtech.social.bot.domain.unit.AnswerCheck;
import org.sadtech.social.bot.domain.unit.AnswerTimer;
import org.sadtech.social.bot.domain.unit.AnswerValidity;
import org.sadtech.social.bot.domain.unit.MainUnit;
import org.sadtech.social.bot.utils.TypeUnit;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UnitUtils {

    private UnitUtils() {
        throw new IllegalStateException("Утилитарный класс");
    }

    public static Collection<MainUnit> nextUnits(MainUnit unit) {
        switch (unit.getType()) {
            case TypeUnit.VALIDITY:
                final AnswerValidity answerValidity = (AnswerValidity) unit;
                return Stream.of(
                        answerValidity.getUnitNo(),
                        answerValidity.getUnitNull(),
                        answerValidity.getUnitYes()
                ).collect(Collectors.toSet());
            case TypeUnit.TIMER:
                return Collections.singleton(((AnswerTimer) unit).getUnitAnswer());
            case TypeUnit.CHECK:
                final AnswerCheck answerCheck = (AnswerCheck) unit;
                return Stream.of(
                        answerCheck.getUnitFalse(),
                        answerCheck.getUnitTrue()
                ).collect(Collectors.toSet());
            default:
                return unit.getNextUnits();
        }
    }

}
