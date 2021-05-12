package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.threeten.bp.OffsetDateTime;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Transaction
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-12T14:50:34.731Z[GMT]")


public class Transaction   {
  @JsonProperty("transactionId")
  private Integer transactionId = null;

  @JsonProperty("timestamp")
  private OffsetDateTime timestamp = null;

  @JsonProperty("userPerforming")
  @Valid
  private Map<String, User> userPerforming = null;

  @JsonProperty("transferTo")
  private String transferTo = null;

  @JsonProperty("transferFrom")
  private String transferFrom = null;

  public Transaction transactionId(Integer transactionId) {
    this.transactionId = transactionId;
    return this;
  }

  /**
   * Get transactionId
   * @return transactionId
   **/
  @Schema(example = "123567890", description = "")
  
    public Integer getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(Integer transactionId) {
    this.transactionId = transactionId;
  }

  public Transaction timestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   **/
  @Schema(example = "2021-01-01T08:00:01Z", description = "")
  
    @Valid
    public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public Transaction userPerforming(Map<String, User> userPerforming) {
    this.userPerforming = userPerforming;
    return this;
  }

  public Transaction putUserPerformingItem(String key, User userPerformingItem) {
    if (this.userPerforming == null) {
      this.userPerforming = new HashMap<String, User>();
    }
    this.userPerforming.put(key, userPerformingItem);
    return this;
  }

  /**
   * Get userPerforming
   * @return userPerforming
   **/
  @Schema(description = "")
      @Valid
    public Map<String, User> getUserPerforming() {
    return userPerforming;
  }

  public void setUserPerforming(Map<String, User> userPerforming) {
    this.userPerforming = userPerforming;
  }

  public Transaction transferTo(String transferTo) {
    this.transferTo = transferTo;
    return this;
  }

  /**
   * Get transferTo
   * @return transferTo
   **/
  @Schema(example = "NL02ABNA0123456789", description = "")
  
    public String getTransferTo() {
    return transferTo;
  }

  public void setTransferTo(String transferTo) {
    this.transferTo = transferTo;
  }

  public Transaction transferFrom(String transferFrom) {
    this.transferFrom = transferFrom;
    return this;
  }

  /**
   * Get transferFrom
   * @return transferFrom
   **/
  @Schema(example = "NL02ABNA0123456789", description = "")
  
    public String getTransferFrom() {
    return transferFrom;
  }

  public void setTransferFrom(String transferFrom) {
    this.transferFrom = transferFrom;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Transaction transaction = (Transaction) o;
    return Objects.equals(this.transactionId, transaction.transactionId) &&
        Objects.equals(this.timestamp, transaction.timestamp) &&
        Objects.equals(this.userPerforming, transaction.userPerforming) &&
        Objects.equals(this.transferTo, transaction.transferTo) &&
        Objects.equals(this.transferFrom, transaction.transferFrom);
  }

  @Override
  public int hashCode() {
    return Objects.hash(transactionId, timestamp, userPerforming, transferTo, transferFrom);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Transaction {\n");
    
    sb.append("    transactionId: ").append(toIndentedString(transactionId)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    userPerforming: ").append(toIndentedString(userPerforming)).append("\n");
    sb.append("    transferTo: ").append(toIndentedString(transferTo)).append("\n");
    sb.append("    transferFrom: ").append(toIndentedString(transferFrom)).append("\n");
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
