package moe.echo.bramblingnote.auth;

import moe.echo.bramblingnote.user.UserWithoutPassword;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("brambling-note-user")
public interface UserClient {
    @GetMapping("/{email}")
    UserWithoutPassword getByEmailAndPassword(@PathVariable String email, @RequestParam String password);
}
