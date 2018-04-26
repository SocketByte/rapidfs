package io.rapidfs.core.command;

import io.rapidfs.core.command.exceptions.ArgumentRequiredException;

import java.util.Map;
import java.util.Objects;

public class CommandHandler {

    @SuppressWarnings("unchecked")
    public static void executeCommand(String syntax) throws ArgumentRequiredException {
        Locker.INSTANCE.enable();
        String[] spaces = syntax.split(" ");
        Command command = CommandDatabase.INSTANCE.getCommand(spaces[0]);

        String[] args = new String[spaces.length - 1];
        System.arraycopy(spaces, 1, args, 0, spaces.length - 1);
        for (int place = 0; place < args.length; place++) {
            String arg = args[place];

            for (Map.Entry<String, Argument> entry : command.getArguments().entrySet()) {
                String name = entry.getKey();
                Argument argument = entry.getValue();

                String alias = "";
                boolean valid = arg.equalsIgnoreCase(name);
                if (!valid) {
                    for (String str : argument.getAliases()) {
                        valid = arg.equalsIgnoreCase(str);

                        if (valid) {
                            alias = str;
                            break;
                        }
                    }
                }

                if (valid) {
                    int places = 1;
                    for (int i = place + 1; i < args.length; i++) {
                        String a = args[i];

                        if (!Objects.equals(a, arg) && command.getArguments().containsKey(a))
                            break;

                        places++;
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int v = 0; v < places; v++) {
                        String t = args[place + v];
                        if (t.equals(name))
                            continue;

                        stringBuilder.append(" ").append(t);
                    }
                    String content = stringBuilder.toString().replace(alias, "")
                            .replaceFirst(" ", "");

                    argument.setContent(content.replaceFirst(" ", ""));
                }
            }
        }
        for (Argument argument : command.getArguments().values()) {
            if (argument.getContent() == null && argument.isRequired()) {
                throw new ArgumentRequiredException(argument.getName());
            }
        }

        command.execute();
        command.clearContents();
    }
}
