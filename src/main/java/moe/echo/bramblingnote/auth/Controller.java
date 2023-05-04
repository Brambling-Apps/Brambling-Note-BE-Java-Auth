package moe.echo.bramblingnote.auth;

import feign.FeignException;
import jakarta.servlet.http.HttpSession;
import moe.echo.bramblingnote.user.UserForReturn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class Controller {
    @Autowired
    private UserClient userClient;

    @RequestMapping("/health")
    public ResponseEntity<Object> health() {
        return ResponseEntity.ok().build();
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
    public ResponseEntity<Object> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().build();
    }
}
