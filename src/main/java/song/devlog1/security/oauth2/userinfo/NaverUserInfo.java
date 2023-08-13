package song.devlog1.security.oauth2.userinfo;

import java.util.Map;

public class NaverUserInfo implements UserInfo {

    private Map<String, Object> attributes;


    public NaverUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    public String getProvider() {
        return "naver";
    }

    public String getName() {
        return (String) attributes.get("name");
    }

    public String getEmail() {
        return (String) attributes.get("email");
    }

    public String getSex() {
        return (String) attributes.get("sex");
    }

    public String getBirth() {
        return (String) attributes.get("birth");
    }

}
