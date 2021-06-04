package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.threeten.bp.LocalDate;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Body1
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-31T10:47:35.905Z[GMT]")


public class CreateUserPostBody {
  @JsonProperty("emailAddress")
  private String emailAddress = null;

  @JsonProperty("firstName")
  private String firstName = null;

  @JsonProperty("lastName")
  private String lastName = null;

  @JsonProperty("role")
  @Valid
  private List<UserRole> role = null;

  @JsonProperty("birthDate")
  private LocalDate birthDate = null;

  @JsonProperty("phone")
  private String phone = null;

  @JsonProperty("password")
  private String password = null;

  @JsonProperty("dayLimit")
  private Float dayLimit = null;

  @JsonProperty("transactionLimit")
  private BigDecimal transactionLimit = new BigDecimal(50);

  public CreateUserPostBody emailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
    return this;
  }

  /**
   * Get emailAddress
   * @return emailAddress
   **/
  @Schema(example = "alice@example.com", required = true, description = "")
      @NotNull

    public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public CreateUserPostBody firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * Get firstName
   * @return firstName
   **/
  @Schema(example = "Alice", required = true, description = "")
      @NotNull

    public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public CreateUserPostBody lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  /**
   * Get lastName
   * @return lastName
   **/
  @Schema(example = "Alixon", required = true, description = "")
      @NotNull

    public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public CreateUserPostBody role(List<UserRole> role) {
    this.role = role;
    return this;
  }

  public CreateUserPostBody addRoleItem(UserRole roleItem) {
    if (this.role == null) {
      this.role = new ArrayList<UserRole>();
    }
    this.role.add(roleItem);
    return this;
  }

  /**
   * Get role
   * @return role
   **/
  @Schema(description = "")
      @Valid
    public List<UserRole> getRole() {
    return role;
  }

  public void setRole(List<UserRole> role) {
    this.role = role;
  }

  public CreateUserPostBody birthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
    return this;
  }

  /**
   * Get birthDate
   * @return birthDate
   **/
  @Schema(example = "Mon May 29 00:00:00 GMT 2000", required = true, description = "")
      @NotNull

    @Valid
    public LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  public CreateUserPostBody phone(String phone) {
    this.phone = phone;
    return this;
  }

  /**
   * Get phone
   * @return phone
   **/
  @Schema(example = "+31 6 12345678", required = true, description = "")
      @NotNull

    public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public CreateUserPostBody password(String password) {
    this.password = password;
    return this;
  }

  /**
   * Get password
   * @return password
   **/
  @Schema(example = "12345", required = true, description = "")
      @NotNull

    public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public CreateUserPostBody dayLimit(Float dayLimit) {
    this.dayLimit = dayLimit;
    return this;
  }

  /**
   * Get dayLimit
   * @return dayLimit
   **/
  @Schema(description = "")
  
    public Float getDayLimit() {
    return dayLimit;
  }

  public void setDayLimit(Float dayLimit) {
    this.dayLimit = dayLimit;
  }

  public CreateUserPostBody transactionLimit(BigDecimal transactionLimit) {
    this.transactionLimit = transactionLimit;
    return this;
  }

  /**
   * Get transactionLimit
   * @return transactionLimit
   **/
  @Schema(example = "100", description = "")
  
    @Valid
    public BigDecimal getTransactionLimit() {
    return transactionLimit;
  }

  public void setTransactionLimit(BigDecimal transactionLimit) {
    this.transactionLimit = transactionLimit;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateUserPostBody createUserPostBody = (CreateUserPostBody) o;
    return Objects.equals(this.emailAddress, createUserPostBody.emailAddress) &&
        Objects.equals(this.firstName, createUserPostBody.firstName) &&
        Objects.equals(this.lastName, createUserPostBody.lastName) &&
        Objects.equals(this.role, createUserPostBody.role) &&
        Objects.equals(this.birthDate, createUserPostBody.birthDate) &&
        Objects.equals(this.phone, createUserPostBody.phone) &&
        Objects.equals(this.password, createUserPostBody.password) &&
        Objects.equals(this.dayLimit, createUserPostBody.dayLimit) &&
        Objects.equals(this.transactionLimit, createUserPostBody.transactionLimit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(emailAddress, firstName, lastName, role, birthDate, phone, password, dayLimit, transactionLimit);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Body1 {\n");
    
    sb.append("    emailAddress: ").append(toIndentedString(emailAddress)).append("\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    birthDate: ").append(toIndentedString(birthDate)).append("\n");
    sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    dayLimit: ").append(toIndentedString(dayLimit)).append("\n");
    sb.append("    transactionLimit: ").append(toIndentedString(transactionLimit)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
