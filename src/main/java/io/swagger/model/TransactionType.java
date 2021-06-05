package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonValue;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets TransactionType
 */
public enum TransactionType {
  TRANSFER("transfer"),
  DEPOSIT("deposit"),
  WITHDRAW("withdraw");

  private String value;

  TransactionType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static TransactionType fromValue(String text) {
    for (TransactionType b : TransactionType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
