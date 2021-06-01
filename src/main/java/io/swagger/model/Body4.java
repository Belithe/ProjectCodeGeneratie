package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Body4
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-31T10:47:35.905Z[GMT]")


public class Body4   {
  @JsonProperty("transferTo")
  private String transferTo = null;

  @JsonProperty("transferFrom")
  private String transferFrom = null;

  @JsonProperty("amount")
  private Float amount = null;

  public Body4 transferTo(String transferTo) {
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

  public Body4 transferFrom(String transferFrom) {
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

  public Body4 amount(Float amount) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Body4 body4 = (Body4) o;
    return Objects.equals(this.transferTo, body4.transferTo) &&
        Objects.equals(this.transferFrom, body4.transferFrom) &&
        Objects.equals(this.amount, body4.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(transferTo, transferFrom, amount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Body4 {\n");
    
    sb.append("    transferTo: ").append(toIndentedString(transferTo)).append("\n");
    sb.append("    transferFrom: ").append(toIndentedString(transferFrom)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
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
