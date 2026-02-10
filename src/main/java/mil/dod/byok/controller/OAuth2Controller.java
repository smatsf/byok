package mil.dod.dha.byok.controller;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuth2Controller {

    @GetMapping("/")
    public String index(@AuthenticationPrincipal OAuth2User user) {
        if (user != null) {
            return "Hello, " + user.getAttribute("name");
        } else {
            return "Please sign in with GitHub";
        }
    }
}
