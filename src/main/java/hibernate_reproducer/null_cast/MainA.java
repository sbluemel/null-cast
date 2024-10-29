package hibernate_reproducer.null_cast;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "main_a")
public class MainA extends Main<SubA> {

  @OneToMany(mappedBy = "main", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<SubA> subs = new ArrayList<>();

  @Override
  public List<SubA> getSubs() {
    return subs;
  }
}
