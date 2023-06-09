package moe.echo.bramblingnote.auth;

import feign.FeignException;
import jakarta.servlet.http.HttpSession;
import moe.echo.bramblingnote.user.UserForReturn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class Controller {
    @Autowired
    private UserClient userClient;

    @RequestMapping("/health")
    public MessageJson health() {
        MessageJson message = new MessageJson();
        message.setMessage("ok");
        return message;
    }

    @GetMapping("/{email}")
    public UserForReturn login(@PathVariable String email, @RequestParam String password, HttpSession session) {
        try {
            UserForReturn result = userClient.getByEmailAndPassword(email, password);
            session.setAttribute("user", result);
            return result;
        } catch (FeignException e) {
            throw new ResponseStatusException(
                    HttpStatusCode.valueOf(e.status()), e.contentUTF8()
            );
        }
    }

    @DeleteMapping("/")
    public MessageJson logout(HttpSession session) {
        session.invalidate();
        MessageJson message = new MessageJson();
        message.setMessage("ok");
        return message;
    }
}
