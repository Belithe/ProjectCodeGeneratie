package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.model.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Body3
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-06-02T13:47:48.293Z[GMT]")


public class Body3   {
  @JsonProperty("IBAN")
  private String IBAN = null;

  @JsonProperty("minimumLimit")
  private Float minimumLimit = null;

  @JsonProperty("userId")
  private Integer userId = null;

  @JsonProperty("accountType")
  private AccountType accountType = null;

  public Body3 IBAN(String IBAN) {
    this.IBAN = IBAN;
    return this;
  }

  /**
   * Get IBAN
   * @return IBAN
   **/
  @Schema(example = "NL02ABNA0123456789", description = "")
  
    public String getIBAN() {
    return IBAN;
  }

  public void setIBAN(String IBAN) {
    this.IBAN = IBAN;
  }

  public Body3 minimumLimit(Float minimumLimit) {
    this.minimumLimit = minimumLimit;
    return this;
  }

  /**
   * Get minimumLimit
   * @return minimumLimit
   **/
  @Schema(example = "200", description = "")
  
    public Float getMinimumLimit() {
    return minimumLimit;
  }

  public void setMinimumLimit(Float minimumLimit) {
    this.minimumLimit = minimumLimit;
  }

  public Body3 userId(Integer userId) {
    this.userId = userId;
    return this;
  }

  /**
   * Get userId
   * @return userId
   **/
  @Schema(example = "42", description = "")
  
    public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public Body3 accountType(AccountType accountType) {
    this.accountType = accountType;
    return this;
  }

  /**
   * Get accountType
   * @return accountType
   **/
  @Schema(description = "")
  
    @Valid
    public AccountType getAccountType() {
    return accountType;
  }

  public void setAccountType(AccountType accountType) {
    this.accountType = accountType;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Body3 body3 = (Body3) o;
    return Objects.equals(this.IBAN, body3.IBAN) &&
        Objects.equals(this.minimumLimit, body3.minimumLimit) &&
        Objects.equals(this.userId, body3.userId) &&
        Objects.equals(this.accountType, body3.accountType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(IBAN, minimumLimit, userId, accountType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Body3 {\n");
    
    sb.append("    IBAN: ").append(toIndentedString(IBAN)).append("\n");
    sb.append("    minimumLimit: ").append(toIndentedString(minimumLimit)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    accountType: ").append(toIndentedString(accountType)).append("\n");
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
