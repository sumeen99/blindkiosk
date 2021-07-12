package com.blindkiosk.server.Model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@Entity
@Document(collection = "SubCategory")
public class SubCategoryModel {
    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private String _id;//고유번호
    private String name;
    @Field("category_id")
    private String categoryId;

    //@OneToMany(mappedBy="foodModel")
    //private Map<String, FoodModel> food=new HashMap<>();
}
