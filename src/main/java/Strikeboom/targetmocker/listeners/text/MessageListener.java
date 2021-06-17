package Strikeboom.targetmocker.listeners.text;

import Strikeboom.targetmocker.listeners.audio.AudioReceivingHandler;
import Strikeboom.targetmocker.listeners.audio.AudioSendingHandler;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if (event.getName().equals("target")) {
            event.deferReply().queue();
            Member targetee = event.getOption("user").getAsMember();
            if (targetee.getVoiceState().inVoiceChannel()) {
                VoiceChannel channel = targetee.getVoiceState().getChannel();
                AudioManager manager = event.getGuild().getAudioManager();
                manager.openAudioConnection(channel);

                manager.setReceivingHandler(new AudioReceivingHandler(targetee));
                manager.setSendingHandler(new AudioSendingHandler());
                event.getHook().sendMessage("Mocking...").queue();
            } else {
                event.getHook().sendMessage("The User is Not in a Voice Channel").queue();
            }
        }
    }
}
