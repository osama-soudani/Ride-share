package tn.esprit.spring.ratingservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.Instant;

@Document("signalements")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Signalement {

    @Id
    private String id;

    @Indexed
    @Field(targetType = FieldType.OBJECT_ID)
    private String idAuteur;

    @Indexed
    @Field(targetType = FieldType.OBJECT_ID)
    private String idCible;

    private String motif;

    @Indexed
    private StatutSignalement statut;

    private Instant dateCreation;
}
