package dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VinListDto {

    private String id;
    private String importateurId;
    private String nom;
    private String millesime;
    private String pays;
    private String region;
    private Double prix;
    private Integer quantite;
    private String importateur;

}
