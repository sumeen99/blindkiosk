package com.blindkiosk.server.Model;

import lombok.*;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class SubCategoryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//고유번호

    @OneToMany(mappedBy="foodModel")
    private Map<String, FoodModel> food=new HashMap<>();
}
