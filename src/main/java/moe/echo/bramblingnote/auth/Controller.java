package moe.echo.bramblingnote.auth;

import feign.FeignException;
import jakarta.servlet.http.HttpSession;
import moe.echo.bramblingnote.user.UserWithoutPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;

@RestController
public class Controller {
    @Autowired
    private UserClient userClient;

    @RequestMapping("/health")
    public ResponseEntity<Object> health() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{email}")
    public UserWithoutPassword get(@PathVariable String email, @RequestParam String password, HttpSession session) {
        try {
            UserWithoutPassword result = userClient.getByEmailAndPassword(email, password);
            session.setAttribute("user", result);
            return result;
        } catch (FeignException e) {
            throw new ResponseStatusException(
                    HttpStatusCode.valueOf(e.status()), e.contentUTF8()
            );
        }
    }
}
