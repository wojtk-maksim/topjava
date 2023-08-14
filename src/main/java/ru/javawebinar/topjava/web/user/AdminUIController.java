package ru.javawebinar.topjava.web.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.List;

@RestController
@RequestMapping(value = AdminUIController.ADMIN_UI_REST, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminUIController extends AbstractUserController {
    public static final String ADMIN_UI_REST = "/admin/users";

    @Override
    @GetMapping
    public List<User> getAll() {
        return super.getAll();
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void create(@RequestParam String name,
                       @RequestParam String email,
                       @RequestParam String password) {
        super.create(new User(null, name, email, password, Role.USER));
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable int id, @RequestBody boolean enabled) {
        super.enable(id, enabled);
    }
}
