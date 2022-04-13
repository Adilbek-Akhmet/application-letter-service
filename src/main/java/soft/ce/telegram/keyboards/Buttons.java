package soft.ce.telegram.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import soft.ce.telegram.dto.BotState;
import soft.ce.telegram.dto.LanguageTypes;

import java.util.ArrayList;
import java.util.List;

public class Buttons {

    private Buttons() {
    }

    public static ReplyKeyboard getLanguagePickButtons() {
        List<InlineKeyboardButton> buttons = new ArrayList<>();

        buttons.add(
                InlineKeyboardButton.builder()
                        .text(LanguageTypes.KAZAKH.getName())
                        .callbackData(LanguageTypes.KAZAKH.name())
                        .build()
        );
        buttons.add(
                InlineKeyboardButton.builder()
                        .text(LanguageTypes.RUSSIA.getName())
                        .callbackData(LanguageTypes.RUSSIA.name())
                        .build()
        );

        return InlineKeyboardMarkup.builder()
                .keyboardRow(buttons)
                .build();
    }

    public static ReplyKeyboard getComplaintTypePickButtons() {
        List<InlineKeyboardButton> button1 = new ArrayList<>();
        List<InlineKeyboardButton> button2 = new ArrayList<>();
        List<InlineKeyboardButton> button3 = new ArrayList<>();

        String studentComplaint = "Заявление по здоровью";
        String teacherComplaint = "Допуск к экзамену";
        String adminComplaint = "Другое";

        button1.add(
                InlineKeyboardButton.builder()
                        .text(studentComplaint)
                        .callbackData(BotState.APPLICATION_HEALTH.name())
                        .build()
        );
        button2.add(
                InlineKeyboardButton.builder()
                        .text(teacherComplaint)
                        .callbackData(BotState.APPLICATION_PASS_EXAM.name())
                        .build()
        );
        button3.add(
                InlineKeyboardButton.builder()
                        .text(adminComplaint)
                        .callbackData(BotState.APPLICATION_OTHER.name())
                        .build()
        );

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(button1, button2, button3))
                .build();
    }
}
