package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets UserRole
 */
public enum UserRole implements GrantedAuthority {
  CUSTOMER("customer"),
    EMPLOYEE("employee");

  private String value;

  UserRole(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static UserRole fromValue(String text) {
    for (UserRole b : UserRole.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }


  @Override
  public String getAuthority() {
    return name();
  }
}
