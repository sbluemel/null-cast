package hibernate_reproducer.null_cast;

import static jakarta.persistence.InheritanceType.TABLE_PER_CLASS;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.ManyToOne;

@Entity
@Inheritance(strategy = TABLE_PER_CLASS)
public abstract class Sub {

  @ManyToOne
  protected Main<?> main;

  @Id
  private Integer id;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

}
