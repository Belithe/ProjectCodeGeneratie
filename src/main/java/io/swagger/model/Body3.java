package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Body3
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-12T14:50:34.731Z[GMT]")


public class Body3   {
  @JsonProperty("minimumLimit")
  private Float minimumLimit = null;

  public Body3 minimumLimit(Float minimumLimit) {
    this.minimumLimit = minimumLimit;
    return this;
  }

  /**
   * Get minimumLimit
   * @return minimumLimit
   **/
  @Schema(example = "235.5", description = "")
  
    public Float getMinimumLimit() {
    return minimumLimit;
  }

  public void setMinimumLimit(Float minimumLimit) {
    this.minimumLimit = minimumLimit;
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
    return Objects.equals(this.minimumLimit, body3.minimumLimit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(minimumLimit);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Body3 {\n");
    
    sb.append("    minimumLimit: ").append(toIndentedString(minimumLimit)).append("\n");
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
