package com.blindkiosk.server.Model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@Entity
@Document(collection = "Food")
public class FoodModel {
   // @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private String _id;//고유번호
    private String name;

    @Field("subcategory_id")
    private String subcategoryId;

    @Field("store_id")
    private String storeId;
    
    private boolean temp;//icee,hot여부

    @Field("custom_id")
    private List<String> customId;

    //@ElementCollection
    private List<String> material;
    private List<String> size;
    private List<String> price;

}
