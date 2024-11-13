package uk.ac.sheffield.team28.team28.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import uk.ac.sheffield.team28.team28.model.MemberType;

public class MemberRegistrationDto {

    @NotBlank(message = "Please enter a valid email address")
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty(message = "Please enter a password")
    @NotBlank(message = "Please enter a password")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    private MemberType memberType;

    private String phone;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    public MemberRegistrationDto() {}

    public MemberRegistrationDto(String email, String password, MemberType memberType, String phone, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.memberType = memberType;
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public MemberType getMemberType() {
        return memberType;
    }

    public void setMemberType(MemberType memberType) {
        this.memberType = memberType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
}
