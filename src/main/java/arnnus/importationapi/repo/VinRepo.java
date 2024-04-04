package arnnus.importationapi.repo;

import arnnus.importationapi.domain.VinList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VinRepo extends JpaRepository<VinList, Long> {
    List<VinList> findAllByImportateurId(String importateurId);
}
