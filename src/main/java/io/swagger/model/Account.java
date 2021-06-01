package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Account
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-31T10:47:35.905Z[GMT]")


public class Account   {
  @JsonProperty("balance")
  private Float balance = null;

  @JsonProperty("IBAN")
  private String IBAN = null;

  @JsonProperty("minimumLimit")
  private Float minimumLimit = null;

  @JsonProperty("userId")
  private Long userId = null;

  public Account balance(Float balance) {
    this.balance = balance;
    return this;
  }

  /**
   * Get balance
   * @return balance
   **/
  @Schema(example = "123.45", description = "")
  
    public Float getBalance() {
    return balance;
  }

  public void setBalance(Float balance) {
    this.balance = balance;
  }

  public Account IBAN(String IBAN) {
    this.IBAN = IBAN;
    return this;
  }

  /**
   * Get IBAN
   * @return IBAN
   **/
  @Schema(example = "NL01INHO0000000001", description = "")
  
    public String getIBAN() {
    return IBAN;
  }

  public void setIBAN(String IBAN) {
    this.IBAN = IBAN;
  }

  public Account minimumLimit(Float minimumLimit) {
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

  public Account userId(Long userId) {
    this.userId = userId;
    return this;
  }

  /**
   * Get userId
   * @return userId
   **/
  @Schema(example = "42", description = "")
  
    public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Account account = (Account) o;
    return Objects.equals(this.balance, account.balance) &&
        Objects.equals(this.IBAN, account.IBAN) &&
        Objects.equals(this.minimumLimit, account.minimumLimit) &&
        Objects.equals(this.userId, account.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(balance, IBAN, minimumLimit, userId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Account {\n");
    
    sb.append("    balance: ").append(toIndentedString(balance)).append("\n");
    sb.append("    IBAN: ").append(toIndentedString(IBAN)).append("\n");
    sb.append("    minimumLimit: ").append(toIndentedString(minimumLimit)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
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
