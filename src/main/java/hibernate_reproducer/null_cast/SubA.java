package hibernate_reproducer.null_cast;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "sub_a")
public class SubA extends Sub {

  @Column(name = "field_a")
  private String fieldA;

  public String getFieldA() {
    return fieldA;
  }

  public void setFieldA(String fieldA) {
    this.fieldA = fieldA;
  }
}
