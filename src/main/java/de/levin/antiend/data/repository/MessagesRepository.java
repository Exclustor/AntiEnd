package de.levin.antiend.data.repository;

import java.util.List;
import lombok.Data;

@Data
public class MessagesRepository {

  private String prefix;
  private String noPerms;
  private String reload;
  private String preventMessage;
  private String help;
  private List<String> helpList;
  private List<String> hologramText;

  private String hologramCreatedSuccess;
  private String hologramCreateFailed;
  private String hologramCreateError;

  private String hologramTeleportedSuccess;
  private String hologramTeleportFailed;
  private String hologramTeleportError;

  private String hologramDeletedSuccess;
  private String hologramDeleteFailed;
  private String hologramDeleteError;

  private String hologramCreatedStatusEnabled;
  private String hologramCreatedStatusDisabled;

  private String commandCanNotBeExecutedInConsole;
}

