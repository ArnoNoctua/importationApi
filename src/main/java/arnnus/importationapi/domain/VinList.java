package arnnus.importationapi.domain;

import jakarta.persistence.*;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "vin_list")
@Setter
public class VinList {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "importateur_id", insertable = false, updatable = false)
        private String importateurId;

        @Column(name = "Nom")
        private String nom;

        @Column(name = "Millesime")
        private String millesime;

        @Column(name = "Pays")
        private String pays;

        @Column(name = "Region")
        private String region;

        @Column(name = "Prix")
        private Double prix;

        @Column(name = "Quantite")
        private Integer quantite;

        @ManyToOne
        @JoinColumn(name = "importateur_id")
        private Importateur importateur;

        @Override
        public String toString() {
                return "VinList{" +
                        "id=" + id +
                        ", importateurId='" + importateurId + '\'' +
                        ", nom='" + nom + '\'' +
                        ", millesime='" + millesime + '\'' +
                        ", pays='" + pays + '\'' +
                        ", region='" + region + '\'' +
                        ", prix=" + prix +
                        ", quantite=" + quantite +
                        ", importateur=" + (importateur != null ? importateur.getId() : null) +
                        '}';
        }
}
