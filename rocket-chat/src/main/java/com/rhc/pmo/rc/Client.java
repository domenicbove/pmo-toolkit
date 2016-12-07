package com.rhc.pmo.rc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.baloise.rocketchatrestclient.model.Message;
import com.github.baloise.rocketchatrestclient.model.Room;
import com.github.baloise.rocketchatrestclient.model.Rooms;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.MultipartBody;
import com.mashape.unirest.request.body.RequestBodyEntity;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.json.JSONObject;

public class Client {
  private final String serverUrl;
  private final String user;
  private final String password;
  private String xAuthToken;
  private String xUserId;
  private ObjectMapper jacksonObjectMapper;

  public Client(String serverUrl, String user, String password) {
    this.serverUrl = serverUrl;
    this.user = user;
    this.password = password;
    jacksonObjectMapper = new ObjectMapper();
    jacksonObjectMapper.configure(
      com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  Map<String, Room> roomCache = new HashMap();

  // TODO sort this out
  // public java.util.Set<Room> getPublicRooms() throws IOException {
  // Rooms rooms = (Rooms) authenticatedGet("publicRooms", Rooms.class);
  // HashSet<Room> ret = new HashSet();
  // roomCache.clear();
  //
  // Room r = rooms[i];
  //
  // for (Room r : rooms) {
  // ret.add(r);
  // roomCache.put(name, r);
  // }
  // return ret;
  // }


  private <T> T authenticatedGet(String method, Class<T> reponseClass) throws IOException {
    try {
      HttpResponse<String> ret = Unirest.get(serverUrl + method).header("X-Auth-Token", xAuthToken)
        .header("X-User-Id", xUserId).asString();
      if (ret.getStatus() == 401) {
        login();
        return (T) authenticatedGet(method, reponseClass);
      }
      return (T) jacksonObjectMapper.readValue((String) ret.getBody(), reponseClass);
    } catch (UnirestException e) {
      throw new IOException(e);
    }
  }

  private void authenticatedPost(String method, Object request) throws IOException {
    authenticatedPost(method, request, null);
  }


  JSONObject lazyVersions;

  private <T> T authenticatedPost(String method, Object request, Class<T> reponseClass)
    throws IOException {
    try {
      HttpResponse<String> ret = Unirest.post(serverUrl + method).header("X-Auth-Token", xAuthToken)
        .header("X-User-Id", xUserId).header("Content-Type", "application/json")
        .body(jacksonObjectMapper.writeValueAsString(request)).asString();
      if (ret.getStatus() == 401) {
        login();
        return (T) authenticatedPost(method, request, reponseClass);
      }
      return reponseClass == null ? null
        : jacksonObjectMapper.readValue((String) ret.getBody(), reponseClass);
    } catch (UnirestException e) {
      throw new IOException(e);
    }
  }


  void login() throws UnirestException {
    HttpResponse<JsonNode> asJson =
      Unirest.post(serverUrl + "login").field("user", user).field("password", password).asJson();
    if (asJson.getStatus() == 401) {
      throw new UnirestException("401 - Unauthorized");
    }
    JSONObject data = ((JsonNode) asJson.getBody()).getObject().getJSONObject("data");
    xAuthToken = data.getString("authToken");
    xUserId = data.getString("userId");
  }

  public void logout() throws IOException {
    try {
      Unirest.post(serverUrl + "logout").header("X-Auth-Token", xAuthToken)
        .header("X-User-Id", xUserId).asJson();
    } catch (UnirestException e) {
      throw new IOException(e);
    }
  }

  public String getApiVersion() throws IOException {
    return getVersions().getString("api");
  }

  public String getRocketChatVersion() throws IOException {
    return getVersions().getString("rocketchat");
  }

  private JSONObject getVersions() throws IOException {
    if (lazyVersions == null) {
      try {
        lazyVersions = ((JsonNode) Unirest.get(serverUrl + "version").asJson().getBody())
          .getObject().getJSONObject("versions");
      } catch (UnirestException e) {
        throw new IOException(e);
      }
    }
    return lazyVersions;
  }

  public void send(String roomName, String message) throws IOException {
    Room room = getRoom(roomName);
    if (room == null) {
      throw new IOException(String.format("unknown room : %s", new Object[] {roomName}));
    }
    send(room, message);
  }

  public void send(Room room, String message) throws IOException {
    authenticatedPost("rooms/" + room._id + "/send", new Message(message));
  }

  public Room getRoom(String room) throws IOException {
    Room ret = (Room) roomCache.get(room);
    if (ret == null) {
      // TODO fix here
      // getPublicRooms();
      ret = (Room) roomCache.get(room);
    }
    return ret;
  }
}
