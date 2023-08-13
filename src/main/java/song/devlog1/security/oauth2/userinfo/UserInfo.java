package song.devlog1.security.oauth2.userinfo;

public interface UserInfo {
    String getProviderId();
    String getProvider();
    String getName();
    String getEmail();
    String getSex();
    String getBirth();
}
