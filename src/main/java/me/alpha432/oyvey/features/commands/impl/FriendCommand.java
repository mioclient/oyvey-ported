package me.alpha432.oyvey.features.commands.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.commands.Command;
import me.alpha432.oyvey.manager.CommandManager;

import java.util.List;
import java.util.StringJoiner;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.word;

public class FriendCommand extends Command {
    public FriendCommand() {
        super(new String[]{"friend", "friends"}, "Adds or removes friends");
    }

    @Override
    public void createArgumentBuilder(LiteralArgumentBuilder<CommandManager> builder) {
        builder.then(literal("list")
                        .executes((ctx) -> {
                            List<String> friends = OyVey.friendManager.getFriends();
                            if (friends.isEmpty()) {
                                return success("You have no friends :(");
                            }
                            StringJoiner joiner = new StringJoiner(",");
                            friends.forEach(joiner::add);
                            return success("Friends (%s): %s", friends.size(), joiner);
                        }))
                .then(literal("clear")
                        .executes((ctx) -> {
                            OyVey.friendManager.getFriends().clear();
                            return success("Cleared friends list");
                        }))
                .then(literal("add")
                        .then(argument("username", word())
                                .executes((ctx) -> {
                                    String username = getString(ctx, "username");
                                    if (OyVey.friendManager.isFriend(username)) {
                                        return success("You already have {green} %s {reset} friended.", username);
                                    }
                                    OyVey.friendManager.addFriend(username);
                                    return success("Added {green} %s {reset} as a friend", username);
                                })))
                .then(literal("remove")
                        .then(argument("username", word())
                                .executes((ctx) -> {
                                    String username = getString(ctx, "username");
                                    if (!OyVey.friendManager.isFriend(username)) {
                                        return success("You do not have {green} %s {reset} friended.", username);
                                    }
                                    OyVey.friendManager.removeFriend(username);
                                    return success("Removed {green} %s {reset} from your friends", username);
                                })));
    }
}
