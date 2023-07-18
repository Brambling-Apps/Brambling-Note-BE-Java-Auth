package moe.echo.bramblingnote.auth;

import com.fasterxml.jackson.annotation.JsonView;
import moe.echo.bramblingnote.user.UserDto;
import moe.echo.bramblingnote.user.View;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("brambling-note-user")
public interface UserClient {
    @GetMapping("/{email}")
    @JsonView(View.ViewOnly.class)
    UserDto getByEmailAndPassword(@PathVariable String email, @RequestParam String password);
}
