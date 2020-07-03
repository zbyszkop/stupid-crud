package pl.zpapierski.stupidcrud;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
public class StupidCrudResource {
    private final AtomicLong idGenerator = new AtomicLong();
    private static ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>();

    @GetMapping("/users")
    public Collection<UserWithId> getAll() {
        return users.entrySet().stream()
                .map(entry -> {
                    User user = entry.getValue();
                    return new UserWithId(entry.getKey(), user.getName(), user.getSurname(), user.getEmail());
                })
                .collect(Collectors.toList());
    }

    @PostMapping("/users")
    public ResponseEntity putUser(@RequestBody User user) {
        Long newId = idGenerator.incrementAndGet();
         users.put(newId, user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newId)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable("id") Long id) {
        if (!users.containsKey(id)) throw new ResponseStatusException(NOT_FOUND, "Unable to find resource");
        User user = users.get(id);
        return user;
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") Long id) {
        if (!users.containsKey(id)) throw new ResponseStatusException(NOT_FOUND, "Unable to find resource");
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{id}")
    public ResponseEntity updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        users.put(id, user);
        return ResponseEntity.noContent().build();
    }
}
