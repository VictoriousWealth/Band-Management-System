package uk.ac.sheffield.team28.team28.dto;

public class MemberLoginDto {
    String email;
    String password;

    public MemberLoginDto() {}

    public MemberLoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
