package io.swagger.model.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

/**
 * InlineResponse200
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-31T10:47:35.905Z[GMT]")


public class LoginResponseDTO {
  @JsonProperty("authToken")
  private String authToken = null;

  public LoginResponseDTO authToken(String authToken) {
    this.authToken = authToken;
    return this;
  }

  /**
   * Get authToken
   * @return authToken
   **/
  @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI0MiIsIm5hbWUiOiJKb2huIERvZSJ9.H6fO18Jm8UE9Tk5-L84RG5XFMWVkilgngp1wpkO4tcM", description = "")
  
    public String getAuthToken() {
    return authToken;
  }

  public void setAuthToken(String authToken) {
    this.authToken = authToken;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LoginResponseDTO loginResponseDTO = (LoginResponseDTO) o;
    return Objects.equals(this.authToken, loginResponseDTO.authToken);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authToken);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponse200 {\n");
    
    sb.append("    authToken: ").append(toIndentedString(authToken)).append("\n");
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
