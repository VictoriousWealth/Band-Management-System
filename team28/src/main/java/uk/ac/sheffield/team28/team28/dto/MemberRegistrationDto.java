package uk.ac.sheffield.team28.team28.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import uk.ac.sheffield.team28.team28.model.MemberType;

public class MemberRegistrationDto {

    @NotBlank(message = "Please enter a valid email address")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Please enter a password")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    // Optional: Add regex for complexity
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, and one digit"
    )
    private String password;

    // Optional: Password confirmation field
    @NotBlank(message = "Please confirm your password")
    private String confirmPassword;


    @NotNull(message = "Member type is required")
    private MemberType memberType;

    @Pattern(
            regexp = "^\\+?\\d{10,15}$",
            message = "Please enter a valid phone number"
    )
    private String phone;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    // Default constructor
    public MemberRegistrationDto() {}

    // Constructor with fields
    public MemberRegistrationDto(String email, String password, String confirmPassword, MemberType memberType, String phone, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.memberType = memberType;
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Getters and Setters
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Custom method to check if passwords match
    public boolean isPasswordMatching() {
        return this.password != null && this.password.equals(this.confirmPassword);
    }
}
