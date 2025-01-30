package de.levin.antiend.Data;

import lombok.Data;

import java.util.List;

@Data
public class MessagesRepository {
    String prefix;
    String noPerms;
    String reload;
    String preventMessage;
    String help;
    List<String> helpList;
}

