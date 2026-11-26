package tn.esprit.spring.ratingservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.Instant;

/** Un auteur ne peut évaluer une même cible qu'une seule fois par course. */
@Document("evaluations")
@CompoundIndex(name = "uniq_auteur_cible_course",
        def = "{'idAuteur': 1, 'idCible': 1, 'idCourseRef': 1}", unique = true)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Evaluation {

    @Id
    private String id;

    @Indexed
    @Field(targetType = FieldType.OBJECT_ID)
    private String idAuteur;

    @Indexed
    @Field(targetType = FieldType.OBJECT_ID)
    private String idCible;

    /** Référence vers GroupeTrajet (matching-service). */
    @Indexed
    @Field(targetType = FieldType.OBJECT_ID)
    private String idCourseRef;

    private int note;          // 1..5
    private String commentaire;
    private Instant date;
}
