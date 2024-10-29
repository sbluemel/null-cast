package hibernate_reproducer.null_cast;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "main_b")
public class MainB extends Main<SubB> {

  @OneToMany(mappedBy = "main", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<SubB> subs = new ArrayList<>();

  @Override
  public List<SubB> getSubs() {
    return subs;
  }
}
