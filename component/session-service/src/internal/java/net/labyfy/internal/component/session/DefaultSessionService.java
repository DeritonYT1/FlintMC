package net.labyfy.internal.component.session;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.authlib.Agent;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.util.UUIDTypeAdapter;
import net.labyfy.component.eventbus.EventBus;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.player.gameprofile.GameProfile;
import net.labyfy.component.player.serializer.gameprofile.GameProfileSerializer;
import net.labyfy.component.session.AuthenticationResult;
import net.labyfy.component.session.AuthenticationResult.Type;
import net.labyfy.component.session.RefreshTokenResult;
import net.labyfy.component.session.SessionService;
import net.labyfy.component.session.event.SessionAccountLogInEvent;
import net.labyfy.component.session.event.SessionTokenRefreshEvent;
import net.labyfy.internal.component.session.refresh.RefreshableBaseUserAuthentication;
import net.labyfy.internal.component.session.refresh.RefreshableYggdrasilUserAuthentication;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class DefaultSessionService implements SessionService {

  private static final String REFRESH_TOKEN_URL = "https://authserver.mojang.com/refresh";
  private static final String VALIDATE_TOKEN_URL = "https://authserver.mojang.com/validate";

  protected final Logger logger;

  protected final Supplier<Proxy> minecraftProxySupplier;

  protected final RefreshTokenResult.Factory refreshTokenResultFactory;
  protected final AuthenticationResult.Factory authResultFactory;

  private UserAuthentication authentication;
  private String clientToken;

  protected final GameProfileSerializer<com.mojang.authlib.GameProfile> profileSerializer;

  protected final EventBus eventBus;
  protected final SessionAccountLogInEvent.Factory logInEventFactory;
  protected final SessionTokenRefreshEvent.Factory tokenRefreshEventFactory;

  protected final Consumer<SessionService> sessionRefresher;

  protected DefaultSessionService(Logger logger, RefreshTokenResult.Factory refreshTokenResultFactory,
                                  GameProfileSerializer profileSerializer,
                                  SessionAccountLogInEvent.Factory logInEventFactory,
                                  SessionTokenRefreshEvent.Factory tokenRefreshEventFactory,
                                  AuthenticationResult.Factory authResultFactory,
                                  EventBus eventBus,
                                  Supplier<Proxy> minecraftProxySupplier,
                                  Consumer<SessionService> sessionRefresher) {
    this.logger = logger;
    this.refreshTokenResultFactory = refreshTokenResultFactory;
    this.minecraftProxySupplier = minecraftProxySupplier;
    this.profileSerializer = profileSerializer;
    this.logInEventFactory = logInEventFactory;
    this.tokenRefreshEventFactory = tokenRefreshEventFactory;
    this.authResultFactory = authResultFactory;
    this.eventBus = eventBus;
    this.sessionRefresher = sessionRefresher;
  }

  private void refreshSession() {
    if (this.sessionRefresher != null) {
      this.sessionRefresher.accept(this);
    }
  }

  private UserAuthentication ensureAuthenticationAvailable() {
    if (this.authentication == null) {
      if (this.clientToken == null) {
        this.clientToken = UUID.randomUUID().toString().replace("-", "");
      }

      this.authentication = new YggdrasilAuthenticationService(this.minecraftProxySupplier.get(), this.clientToken).createUserAuthentication(Agent.MINECRAFT);
    }

    return this.authentication;
  }

  @Override
  public void setClientToken(String clientToken) {
    this.clientToken = clientToken;
    this.authentication = null;
    this.refreshSession();
  }

  @Override
  public String getClientToken() {
    this.ensureAuthenticationAvailable(); // generate the client token if necessary
    return this.clientToken;
  }

  @Override
  public UUID getUniqueId() {
    if (this.authentication == null) {
      return null;
    }
    com.mojang.authlib.GameProfile profile = this.authentication.getSelectedProfile();
    return profile != null ? profile.getId() : null;
  }

  @Override
  public String getUsername() {
    if (this.authentication == null) {
      return null;
    }
    com.mojang.authlib.GameProfile profile = this.authentication.getSelectedProfile();
    return profile != null ? profile.getName() : null;
  }

  @Override
  public GameProfile getProfile() {
    if (this.authentication == null) {
      return null;
    }
    com.mojang.authlib.GameProfile profile = this.authentication.getSelectedProfile();
    return profile != null ? this.profileSerializer.deserialize(profile) : null;
  }

  @Override
  public String getAccessToken() {
    return this.authentication != null ? this.authentication.getAuthenticatedToken() : null;
  }

  @Override
  public boolean isAccessTokenValid() {
    String accessToken = this.getAccessToken();
    if (accessToken == null) {
      return false;
    }

    try {
      return this.validateAccessToken(accessToken);
    } catch (IOException e) {
      this.logger.error("An error occurred while validating access token", e);
      return false;
    }
  }

  @Override
  public boolean isLoggedIn() {
    return this.authentication != null && this.authentication.isLoggedIn();
  }

  @Override
  public RefreshTokenResult refreshToken() {
    String accessToken = this.getAccessToken();
    if (accessToken == null) {
      return this.refreshTokenResultFactory.createUnknown(RefreshTokenResult.ResultType.NOT_LOGGED_IN);
    }
    UserAuthentication authentication = this.ensureAuthenticationAvailable();

    if (!(authentication instanceof RefreshableYggdrasilUserAuthentication)) {
      // this can only happen if shadow has failed which basically never happens
      return this.refreshTokenResultFactory.create(RefreshTokenResult.ResultType.OTHER, "Not supported");
    }

    try {
      RefreshableYggdrasilUserAuthentication refreshable = (RefreshableYggdrasilUserAuthentication) authentication;

      JsonObject result = this.requestNewToken(accessToken);

      if (result == null) {
        return this.refreshTokenResultFactory.createUnknown(RefreshTokenResult.ResultType.OTHER);
      }

      if (result.has("accessToken")) {
        String newToken = result.get("accessToken").getAsString();

        if (this.tokenRefreshEventFactory != null) {
          this.eventBus.fireEvent(this.tokenRefreshEventFactory.create(accessToken, newToken), Subscribe.Phase.POST);
        }

        refreshable.setAccessToken(newToken);
        this.refreshSession();
        return this.refreshTokenResultFactory.createUnknown(RefreshTokenResult.ResultType.SUCCESS);
      }

      if (result.has("errorMessage")) {
        return this.refreshTokenResultFactory.create(RefreshTokenResult.ResultType.OTHER, result.get("errorMessage").getAsString());
      }

      return this.refreshTokenResultFactory.createUnknown(RefreshTokenResult.ResultType.OTHER);
    } catch (IOException | JsonParseException e) {
      return this.refreshTokenResultFactory.create(RefreshTokenResult.ResultType.OTHER, e.getMessage());
    }
  }

  private byte[] generateAccessTokenBody(String accessToken) {
    JsonObject body = new JsonObject();
    body.addProperty("clientToken", this.clientToken);
    body.addProperty("accessToken", accessToken);
    return body.toString().getBytes(StandardCharsets.UTF_8);
  }

  private boolean validateAccessToken(String accessToken) throws IOException {
    byte[] body = this.generateAccessTokenBody(accessToken);

    HttpURLConnection connection = (HttpURLConnection) new URL(VALIDATE_TOKEN_URL).openConnection();
    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
    connection.setRequestProperty("Content-Length", String.valueOf(body.length));
    connection.setDoOutput(true);

    try (OutputStream outputStream = connection.getOutputStream()) {
      outputStream.write(body);
    }

    return connection.getResponseCode() == 204;
  }

  private JsonObject requestNewToken(String accessToken) throws IOException {
    byte[] body = this.generateAccessTokenBody(accessToken);

    HttpURLConnection connection = (HttpURLConnection) new URL(REFRESH_TOKEN_URL).openConnection();
    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
    connection.setRequestProperty("Content-Length", String.valueOf(body.length));
    connection.setDoOutput(true);
    connection.setDoInput(true);

    try (OutputStream outputStream = connection.getOutputStream()) {
      outputStream.write(body);
    }

    if (connection.getResponseCode() < 200 || connection.getResponseCode() > 299) {
      return null;
    }

    try (InputStream inputStream = connection.getInputStream();
         Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
      return JsonParser.parseReader(reader).getAsJsonObject();
    }
  }

  @Override
  public AuthenticationResult logIn(String email, String password) {
    GameProfile currentProfile = this.getProfile();
    UserAuthentication authentication = this.ensureAuthenticationAvailable();

    if (authentication.isLoggedIn()) {
      authentication.logOut();
    }

    authentication.setUsername(email);
    authentication.setPassword(password);

    try {
      authentication.logIn();

      this.fireLoginEvent(currentProfile);

      return this.authResultFactory.createSuccess(this.getProfile());
    } catch (AuthenticationUnavailableException e) {
      return this.authResultFactory.createFailed(Type.AUTH_SERVER_OFFLINE);
    } catch (InvalidCredentialsException e) {
      return this.authResultFactory.createFailed(Type.INVALID_CREDENTIALS);
    } catch (AuthenticationException e) {
      this.logger.error("An error occurred while logging into an account", e);
      return this.authResultFactory.createFailed(Type.UNKNOWN_ERROR);
    }
  }

  @Override
  public AuthenticationResult logIn(String accessToken) {
    UserAuthentication authentication = this.ensureAuthenticationAvailable();

    if (!(authentication instanceof RefreshableYggdrasilUserAuthentication) ||
        !(authentication instanceof RefreshableBaseUserAuthentication)) {
      // this can only happen if shadow has failed which basically never happens
      return this.authResultFactory.createFailed(Type.UNKNOWN_ERROR);
    }

    RefreshableYggdrasilUserAuthentication refreshable = (RefreshableYggdrasilUserAuthentication) authentication;
    GameProfile currentProfile = this.getProfile();

    try {
      JsonObject object = this.requestNewToken(accessToken);
      if (object == null || !object.has("accessToken")) {
        return this.authResultFactory.createFailed(Type.INVALID_CREDENTIALS);
      }
      JsonObject profile = object.get("selectedProfile").getAsJsonObject();

      this.authentication.logOut();

      refreshable.setAccessToken(object.get("accessToken").getAsString());
      ((RefreshableBaseUserAuthentication) authentication).setPublicSelectedProfile(new com.mojang.authlib.GameProfile(
          UUIDTypeAdapter.fromString(profile.get("id").getAsString()),
          profile.get("name").getAsString()
      ));

      this.fireLoginEvent(currentProfile);

      return this.authResultFactory.createSuccess(this.getProfile());
    } catch (IOException e) {
      this.logger.error("Failed to log in using access token", e);
      return this.authResultFactory.createFailed(Type.AUTH_SERVER_OFFLINE);
    }
  }

  private void fireLoginEvent(GameProfile previousProfile) {
    GameProfile newProfile = this.getProfile();

    if (this.logInEventFactory != null) {
      SessionAccountLogInEvent event = previousProfile != null ?
          this.logInEventFactory.create(previousProfile, newProfile) :
          this.logInEventFactory.create(newProfile);
      this.eventBus.fireEvent(event, Subscribe.Phase.POST);
    }

    this.refreshSession();
  }

  @Override
  public void logOut() {
    if (this.authentication != null && this.authentication.isLoggedIn()) {
      this.authentication.logOut();
    }
  }

  @Override
  public boolean isMain() {
    return this.tokenRefreshEventFactory != null && this.logInEventFactory != null && this.sessionRefresher != null;
  }

}
