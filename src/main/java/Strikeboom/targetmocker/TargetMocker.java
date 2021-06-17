package Strikeboom.targetmocker;

import Strikeboom.targetmocker.listeners.text.MessageListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class TargetMocker {
    public static void main(String[] args) {
        String token = null;
        File tokenFile = new File("token.txt");
        if (!tokenFile.exists()) {
            try {
                System.out.println("Fill out token.txt with bot token");
                tokenFile.createNewFile();
                System.exit(0);
            } catch (IOException ignored) {}
        }
        try {
            Scanner reader = new Scanner(tokenFile);
            token = reader.nextLine();
            reader.close();
        } catch (FileNotFoundException ignored) {}
        try {
            JDA jda = JDABuilder.createDefault(token).build();
            jda.updateCommands().queue();
            jda.upsertCommand("target","Targets Your Person!").addOption(OptionType.USER,"user","The User You Want Targeted",true).queue();
            jda.addEventListener(new MessageListener());
            jda.getPresence().setActivity(Activity.playing("/target @[user]"));
        } catch (LoginException e) {
            e.printStackTrace();
        }

    }
}

