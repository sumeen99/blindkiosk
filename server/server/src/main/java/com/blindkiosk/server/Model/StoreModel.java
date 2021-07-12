package com.blindkiosk.server.Model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
//import org.springframework.data.annotation.Id;

//import javax.persistence.*;


@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@Entity
@Document(collection = "Store")
public class StoreModel {
  //  @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private String _id;//가게 고유번호

    //@Id

    private String name; //가게명


}
