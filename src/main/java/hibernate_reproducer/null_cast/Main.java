package hibernate_reproducer.null_cast;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Main<T extends Sub> {
  @Id
  Integer id;

  @Column
  String objectId;

  public abstract List<T> getSubs();
}
