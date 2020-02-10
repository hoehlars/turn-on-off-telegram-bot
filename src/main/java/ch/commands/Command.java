package ch.commands;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {

    String ON_CMD = "/on";
    String OFF_CMD = "/off";

    void execute(Update update);
}
