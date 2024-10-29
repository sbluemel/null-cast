package hibernate_reproducer.null_cast;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "sub_b")
public class SubB extends Sub {

  @Column(name = "field_b")
  private String fieldB;

  public String getFieldB() {
    return fieldB;
  }

  public void setFieldB(String fieldB) {
    this.fieldB = fieldB;
  }
}
