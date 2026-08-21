package almacen;
import modelo.Identificable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//Almacen generico que almacenara diferentes valores. Principalmente libro y ejemplar
public class Almacen<T extends Identificable<ID>, ID> {
      private final List<T> items = new ArrayList<>();

      public void guardar(T item) {
        items.add(item);
      }

      //Filtra los que tengan el mismo id y encuentra el primero que coincida.
      public Optional<T> buscarPorId(ID id ) {
        return items.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst();
      }

      public List<T> listarTodos() {
          return new ArrayList<>(items);
      }

      public boolean existe(T item) {
          return items.contains(item);
      }
  }
