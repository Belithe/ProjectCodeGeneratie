package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.model.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Body5
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-06-03T10:00:39.046Z[GMT]")


public class Body5   {
  @JsonProperty("transferTo")
  private String transferTo = null;

  @JsonProperty("transferFrom")
  private String transferFrom = null;

  @JsonProperty("amount")
  private Float amount = null;

  @JsonProperty("transactionType")
  private TransactionType transactionType = null;

  public Body5 transferTo(String transferTo) {
    this.transferTo = transferTo;
    return this;
  }

  /**
   * Get transferTo
   * @return transferTo
   **/
  @Schema(example = "NL14RABO0987654321", required = true, description = "")
  @NotNull

  public String getTransferTo() {
    return transferTo;
  }

  public void setTransferTo(String transferTo) {
    this.transferTo = transferTo;
  }

  public Body5 transferFrom(String transferFrom) {
    this.transferFrom = transferFrom;
    return this;
  }

  /**
   * Get transferFrom
   * @return transferFrom
   **/
  @Schema(example = "NL91ABNA1234567890", required = true, description = "")
  @NotNull

  public String getTransferFrom() {
    return transferFrom;
  }

  public void setTransferFrom(String transferFrom) {
    this.transferFrom = transferFrom;
  }

  public Body5 amount(Float amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * @return amount
   **/
  @Schema(example = "9000.01", description = "")

  public Float getAmount() {
    return amount;
  }

  public void setAmount(Float amount) {
    this.amount = amount;
  }

  public Body5 transactionType(TransactionType transactionType) {
    this.transactionType = transactionType;
    return this;
  }

  /**
   * Get transactionType
   * @return transactionType
   **/
  @Schema(description = "")

  @Valid
  public TransactionType getTransactionType() {
    return transactionType;
  }

  public void setTransactionType(TransactionType transactionType) {
    this.transactionType = transactionType;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Body5 body5 = (Body5) o;
    return Objects.equals(this.transferTo, body5.transferTo) &&
            Objects.equals(this.transferFrom, body5.transferFrom) &&
            Objects.equals(this.amount, body5.amount) &&
            Objects.equals(this.transactionType, body5.transactionType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(transferTo, transferFrom, amount, transactionType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Body5 {\n");

    sb.append("    transferTo: ").append(toIndentedString(transferTo)).append("\n");
    sb.append("    transferFrom: ").append(toIndentedString(transferFrom)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    transactionType: ").append(toIndentedString(transactionType)).append("\n");
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
