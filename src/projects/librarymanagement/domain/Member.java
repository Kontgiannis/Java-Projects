package projects.librarymanagement.domain;

public class Member {
    private final long id; // PK immutable
    private final String name;
    private String email;
    public Member(long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public long getId() {return id;}
    public String getName() {return name;}
    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}
}
