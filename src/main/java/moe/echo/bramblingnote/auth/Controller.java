package moe.echo.bramblingnote.auth;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import feign.FeignException;
import jakarta.servlet.http.HttpSession;
import moe.echo.bramblingnote.user.UserDto;
import moe.echo.bramblingnote.user.View;
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
    @JsonView(View.ViewOnly.class)
    public UserDto login(@PathVariable String email, @RequestParam String password, HttpSession session) {
        try {
            UserDto result = userClient.getByEmailAndPassword(email, password);
            ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
            session.setAttribute("user", writer.writeValueAsString(result));
            return result;
        } catch (FeignException e) {
            throw new ResponseStatusException(
                    HttpStatusCode.valueOf(500), e.contentUTF8()
            );
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(
                    HttpStatusCode.valueOf(500), e.getMessage()
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
