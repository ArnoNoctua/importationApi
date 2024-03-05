package arnnus.importationapi.repo;

import arnnus.importationapi.domain.Importateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImportateurRepo extends JpaRepository<Importateur, String>{
    //Passer string car le id est défini comme string
    Optional<Importateur> findById(String id);
}
