package pl.zpapierski.stupidcrud;

public class UserWithId extends User {
    private final Long id;

    public UserWithId(Long id, String name, String surname, String email) {
        super(name, surname, email);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
